package com.exemplo;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import com.exemplo.services.RequirementService;
import com.exemplo.services.UserService;

public class App {

    public static void main(String[] args) {
        int port = 8080; // Porta do servidor
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado na porta " + port);
            RequirementService requirementService = new RequirementService();

            while (true) {
                // Aceita a conexão do cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Cria um writer para enviar mensagens ao cliente
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    System.out.println("Mensagem recebida do cliente: " + clientMessage);
                    var request = clientMessage.split("\\|");
                    switch (request[0]) {
                        case "login"  : {
                            var credentials = request[1].split(",");
                            var username = credentials[0];
                            var passwd = credentials[1];

                            out.println(UserService.isUser(username, passwd));
                        }
                            break;
                        case "ALL": {

                            ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());
                            Integer userId = Integer.parseInt(request[1]);
                            System.out.println("Cebola " + userId);
                            if (userId != null)
                                objOut.writeObject(requirementService.getAll(userId));
                        }
                            break;
                        case "create": {
                            ObjectInputStream objIn = new ObjectInputStream(clientSocket.getInputStream());
                            HashMap<String, String> req;
                            try {
                                req = (HashMap<String, String>) objIn.readObject();
                                for(String key: req.keySet()) {
                                    System.out.println(req.get(key));
                                }
                            } catch (ClassNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
break;        
                        default:
                            break;
                    }

                    UserService.isUser(clientMessage, clientMessage);
                }

                // Fecha o socket do client++++++
                clientSocket.close();
                System.out.println("Conexão com o cliente encerrada.");
            }

        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// User user = new User();
// user.setName("Eloide Simao");
// user.setNickname("Novela");
// user.setEmail("eloide.novela@outlook.com");
// user.setPasswd("10134456");
// user.setUsername("eloide-novela");
// // session.save(user);
// Session session = HibernateUtil.getSessionFactory().openSession();
// session.beginTransaction();
// session.getTransaction().commit();
// session.close();
// HibernateUtil.shutdown();
// System.out.println("sucesso!");