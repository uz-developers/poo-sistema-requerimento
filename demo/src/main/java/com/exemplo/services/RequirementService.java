package com.exemplo.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.exemplo.models.Course;
import com.exemplo.models.Requirement;
import com.exemplo.models.Student;
import com.exemplo.models.User;
import com.exemplo.util.HibernateUtil;

public class RequirementService extends Service {
    @Override
    public void create(HashMap<String, String> data) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Requirement requirement = new Requirement();
            User user = session.get(User.class, Integer.parseInt(data.get("user_id")));
            // Course course= session.get(Course.class, CourseService.getByName(data.get("courseName")).getId());
            Student student = session.get(Student.class, Integer.parseInt(data.get("student_id")));
            
            requirement.setDescription(data.get("description"));
            requirement.setStatus(data.get("status"));
            requirement.setType(data.get("type"));
            requirement.setAttachment(data.get("attachment"));
            requirement.setUser(user);
            requirement.setStudent(student);
            requirement.setEntryDate(Instant.now());
            requirement.setCreatedAt(Instant.now());
            requirement.setUpdatedAt(Instant.now());
            
            session.save(requirement);
            session.getTransaction().commit();
            session.close();
            HibernateUtil.shutdown(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean update(HashMap<String, String> data) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Requirement existingRequirement = session.get(Requirement.class, Integer.parseInt(data.get("id")));

            for (String field: data.keySet()) {
                switch (field) {
                    case "description":
                        existingRequirement.setDescription(data.get("description"));
                        break;
                    case "destination_email":
                        existingRequirement.setDestinationEmail(data.get("destination_email"));
                        break;
                    case "status":
                        existingRequirement.setStatus(data.get("status"));
                        break;
                    case "type":
                        existingRequirement.setType(data.get("type"));
                        break;
                    case "attachment":
                        existingRequirement.setAttachment(data.get("attachment"));
                        break;
                    case "user_id":{
                        User user = session.get(User.class, Integer.parseInt(data.get("user_id")));
                        existingRequirement.setUser(user);
                        break;
                    }
                    case "student_id": {
                        Student student = session.get(Student.class, Integer.parseInt(data.get(field)));
                        existingRequirement.setStudent(student);
                        break;
                    }
                }
            }

            existingRequirement.setUpdatedAt(Instant.now());
            session.getTransaction().commit();
            session.close();
            HibernateUtil.shutdown();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }

        return false;
    }

    @Override
    public Boolean delete(HashMap<String, String> data) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Requirement requirement = session.get(Requirement.class, Integer.parseInt(data.get("id")));
            session.delete(requirement);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }

        return true;
    }
    @Override
    public List<Requirement> getAll() {
        return null;
    }

    // @Override
    public List<HashMap<String, String>> getAll(Integer userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        // Query ajustada com join entre Requirement, Student e Course
        String hql = "SELECT r.id, s.name, r.type, c.name, r.status, s.studentCode, r.description, r.attachment, r.entryDate, r.conclusionDate " +
                     "FROM Requirement r " +
                     "JOIN r.student s " +
                     "JOIN s.course c " +  // Join com a entidade Course
                     "WHERE r.user.id = :user_id";
        
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("user_id", userId);
        List<Object[]> results = query.list();
        
        // Lista que irá armazenar os HashMaps
        List<HashMap<String, String>> result = new ArrayList<>();
        
        // Itera sobre os resultados da query para criar um HashMap para cada um
        for (Object[] row : results) {
            HashMap<String, String> map = new HashMap<>();
            
            map.put("id", String.valueOf(row[0]));
            map.put("studentName", (String) row[1]);
            map.put("type", row[2].toString());  // Assuming RequirementType is an enum
            map.put("courseName", (String) row[3]);  // Nome do curso
            map.put("status", row[4].toString());  // Assuming RequirementStatus is an enum
            map.put("studentCode", String.valueOf(row[5]));
            map.put("description", (String) row[6]);
            map.put("attachment", row[7] != null ? (String) row[7] : "N/A");
            map.put("entryDate", row[8].toString());
            map.put("conclusionDate", row[9] != null ? row[9].toString() : "N/A");
    
            // Adiciona o HashMap na lista de resultados
            result.add(map);
        }
        
        session.getTransaction().commit();
        session.close();
        
        // Retorna a lista de HashMaps
        return result;
    }
    
    public List<HashMap<String, String>> getAllByUserID(Integer userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        // Consulta HQL ajustada para realizar o join entre Requirement, Student e Course
        String hql = "SELECT r FROM Requirement r " +
                     "JOIN FETCH r.student s " +  // Realiza join com Student
                     "JOIN FETCH s.course c " +   // Realiza join com Course através de Student
                     "WHERE r.user.id = :userId"; // Filtra pelo ID do usuário
        
        // Executa a query utilizando o parâmetro userId
        Query<Requirement> query = session.createQuery(hql, Requirement.class);
        query.setParameter("userId", userId);
        
        // Obtém a lista de requisitos
        List<Requirement> requirements = query.list();
        
        // Lista que irá armazenar os HashMaps
        List<HashMap<String, String>> result = new ArrayList<>();
        
        // Itera sobre os requisitos para criar um HashMap para cada um
        for (Requirement requirement : requirements) {
            HashMap<String, String> map = new HashMap<>();
            
            // Preenche o HashMap com as propriedades do Requirement e das entidades relacionadas
            map.put("id", String.valueOf(requirement.getId()));
            map.put("studentName", requirement.getStudent().getName());
            map.put("courseName", requirement.getStudent().getCourse().getName());
            map.put("type", requirement.getType().toString());  // Assuming RequirementType is an enum
            map.put("status", requirement.getStatus().toString());  // Assuming RequirementStatus is an enum
            map.put("studentCode", String.valueOf(requirement.getStudent().getStudentCode()));
            map.put("description", requirement.getDescription());
            map.put("attachment", requirement.getAttachment() != null ? requirement.getAttachment() : "N/A");
            map.put("entryDate", requirement.getEntryDate().toString());
            map.put("conclusionDate", requirement.getConclusionDate() != null ? requirement.getConclusionDate().toString() : "N/A");
    
            // Adiciona o HashMap na lista de resultados
            result.add(map);
        }
        
        // Finaliza a transação
        session.getTransaction().commit();
        session.close();
        
        // Retorna a lista de HashMaps
        return result;
    }    

    public List<HashMap<String, String>> getAllStatus(Integer userId, String status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        // Consulta HQL ajustada para realizar o join entre Requirement, Student e Course
        String hql = "SELECT r FROM Requirement r " +
                     "JOIN FETCH r.student s " +  // Realiza join com Student
                     "JOIN FETCH s.course c " +   // Realiza join com Course através de Student
                     "WHERE r.user.id = :userId AND r.status = :status";  // Filtra pelo ID do usuário e status
        
        // Executa a query utilizando os parâmetros userId e status
        Query<Requirement> query = session.createQuery(hql, Requirement.class);
        query.setParameter("userId", userId);
        query.setParameter("status", status);
        
        // Obtém a lista de requisitos
        List<Requirement> requirements = query.list();
        
        // Lista que irá armazenar os HashMaps
        List<HashMap<String, String>> result = new ArrayList<>();
        
        // Itera sobre os requisitos para criar um HashMap para cada um
        for (Requirement requirement : requirements) {
            HashMap<String, String> map = new HashMap<>();
            
            // Preenche o HashMap com as propriedades do Requirement e das entidades relacionadas
            map.put("id", String.valueOf(requirement.getId()));
            map.put("studentName", requirement.getStudent().getName());
            map.put("courseName", requirement.getStudent().getCourse().getName());
            map.put("type", requirement.getType().toString());  // Assuming RequirementType is an enum
            map.put("status", requirement.getStatus().toString());  // Assuming RequirementStatus is an enum
            map.put("studentCode", String.valueOf(requirement.getStudent().getStudentCode()));
            map.put("description", requirement.getDescription());
            map.put("attachment", requirement.getAttachment() != null ? requirement.getAttachment() : "N/A");
            map.put("entryDate", requirement.getEntryDate().toString());
            map.put("conclusionDate", requirement.getConclusionDate() != null ? requirement.getConclusionDate().toString() : "N/A");
            map.put("destinationEmail", requirement.getDestinationEmail());
            map.put("createdAt", requirement.getCreatedAt().toString());
            map.put("updatedAt", requirement.getUpdatedAt() != null ? requirement.getUpdatedAt().toString() : "N/A");
    
            // Adiciona o HashMap na lista de resultados
            result.add(map);
        }
        
        // Finaliza a transação
        session.getTransaction().commit();
        session.close();
        
        // Retorna a lista de HashMaps
        return result;
    }    

    public List<HashMap<String, String>> getAllByType(Integer userId, String type) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        // Filtra os requisitos pelo status
        Query<Requirement> query = session.createQuery("FROM Requirement WHERE user_id =:user_id and type = :type", Requirement.class);
        query.setParameter("type", type);  // Define o parâmetro de status na consulta
        query.setParameter("user_id", userId);
        List<Requirement> requirements = query.list();
        
        // Lista que irá armazenar os HashMaps
        List<HashMap<String, String>> result = new ArrayList<>();
        
        // Itera sobre os requisitos para criar um HashMap para cada um
        for (Requirement requirement : requirements) {
            HashMap<String, String> map = new HashMap<>();
            
            // Preenche o HashMap com as propriedades do Requirement
            map.put("id", String.valueOf(requirement.getId()));
            map.put("type", requirement.getType().toString());  // Assuming RequirementType is an enum
            map.put("description", requirement.getDescription());
            map.put("entryDate", requirement.getEntryDate().toString());
            map.put("attachment", requirement.getAttachment() != null ? requirement.getAttachment() : "N/A");
            map.put("status", requirement.getStatus().toString());  // Assuming RequirementStatus is an enum
            map.put("destinationEmail", requirement.getDestinationEmail());
            map.put("createdAt", requirement.getCreatedAt().toString());
            map.put("updatedAt", requirement.getUpdatedAt() != null ? requirement.getUpdatedAt().toString() : "N/A");
            map.put("conclusionDate", requirement.getConclusionDate() != null ? requirement.getConclusionDate().toString() : "N/A");

            // Adiciona o HashMap na lista de resultados
            result.add(map);
        }
        
        session.getTransaction().commit();
        session.close();
        
        // Retorna a lista de HashMaps
        return result;
    }    
}
