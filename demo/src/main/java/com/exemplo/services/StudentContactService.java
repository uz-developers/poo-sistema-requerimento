package com.exemplo.services;

import java.util.List;
import java.util.HashMap;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.exemplo.util.HibernateUtil;
import com.exemplo.models.StudentContact;
import com.exemplo.models.Student;
import java.time.Instant;

public class StudentContactService extends Service {

    @Override
    public void create(HashMap<String, String> data) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        StudentContact studentContact = new StudentContact();
        Student student = session.get(Student.class, Integer.parseInt(data.get("student_id")));
        studentContact.setStudent(student);
        studentContact.setContact(data.get("contact"));
        studentContact.setCreatedAt(Instant.now());
        studentContact.setUpdatedAt(Instant.now());
        session.save(studentContact);

        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
    }

    @Override
    public Boolean update(HashMap<String, String> data) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            StudentContact studentContact = session.get(StudentContact.class, Integer.parseInt(data.get("id")));

            for (String field : data.keySet()) {
                switch (field) {
                    case "student_id":
                        Student student = session.get(Student.class, Integer.parseInt(data.get(field)));
                        studentContact.setStudent(student);
                        break;
                    case "contact":
                        studentContact.setContact(data.get(field));
                        break;
                }
            }

            studentContact.setUpdatedAt(Instant.now());
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(HashMap<String, String> data) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            StudentContact studentContact = session.get(StudentContact.class, Integer.parseInt(data.get("id")));
            session.delete(studentContact);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<StudentContact> getAll() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            List<StudentContact> studentContacts = null;
            Query<StudentContact> query = session.createQuery("FROM StudentContact", StudentContact.class);
            studentContacts = query.list();
            session.getTransaction().commit();
            session.close();
            return studentContacts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
