package com.exemplo.services;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.exemplo.models.Requirement;
import com.exemplo.models.Student;
import com.exemplo.models.User;
import com.exemplo.models.enums.RequirementStatus;
import com.exemplo.models.enums.RequirementType;
import com.exemplo.util.HibernateUtil;

public class requirementService extends Service {
    @Override
    public void create(HashMap<String, String> data) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Requirement requirement = new Requirement();
            User user = session.get(User.class, Integer.parseInt(data.get("user_id")));
            Student student = session.get(Student.class, Integer.parseInt(data.get("student_id")));
            
            requirement.setDescription(data.get("description"));
            requirement.setDestinationEmail(data.get("destination_email"));
            requirement.setStatus(RequirementStatus.fromValue(data.get("status")));
            requirement.setType(RequirementType.fromValue(data.get("type")));
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
                        existingRequirement.setStatus(RequirementStatus.fromValue(data.get("status")));
                        break;
                    case "type":
                        existingRequirement.setType(RequirementType.fromValue(data.get("type")));
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
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Requirement> query = session.createQuery("FROM Requirement", Requirement.class);
            List<Requirement> requirements = query.list();
            session.getTransaction().commit();
            session.close();
            return requirements;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
