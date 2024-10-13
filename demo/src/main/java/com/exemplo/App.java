package com.exemplo;
import com.exemplo.models.User;
import com.exemplo.util.HibernateUtil;
import org.hibernate.Session;

public class App 
{
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        // User user = new User();
        // user.setName("Eloide Simao");
        // user.setNickname("Novela");
        // user.setEmail("eloide.novela@outlook.com");
        // user.setPasswd("10134456");
        // user.setUsername("eloide-novela");
        // session.save(user);

        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
        System.out.println("sucesso!");
    }
}
