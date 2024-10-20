package com.exemplo.services;

import java.util.List;
import java.util.HashMap;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.exemplo.util.HibernateUtil;
import com.exemplo.models.Student;
import com.exemplo.models.Course;
import java.time.Instant;

public class StudentService extends Service {

    @Override
    public void create(HashMap<String, String> data) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Student student = new Student();
        student.setName(data.get("name"));
        student.setNickname(data.get("nickname"));
        student.setIdentityCard(data.get("identityCard"));
        student.setStudentCode(Long.parseLong(data.get("studentCode")));
        student.setEmail(data.get("email"));

        Course course = session.get(Course.class, Integer.parseInt(data.get("course_id")));
        student.setCourse(course);

        student.setCreatedAt(Instant.now());
        student.setUpdatedAt(Instant.now());
        session.save(student);

        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
    }

    @Override
    public Boolean update(HashMap<String, String> data) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Student student = session.get(Student.class, Integer.parseInt(data.get("id")));

            for (String field : data.keySet()) {
                switch (field) {
                    case "name":
                        student.setName(data.get(field));
                        break;
                    case "nickname":
                        student.setNickname(data.get(field));
                        break;
                    case "identityCard":
                        student.setIdentityCard(data.get(field));
                        break;
                    case "studentCode":
                        student.setStudentCode(Long.parseLong(data.get(field)));
                        break;
                    case "email":
                        student.setEmail(data.get(field));
                        break;
                    case "course_id":
                        Course course = session.get(Course.class, Integer.parseInt(data.get(field)));
                        student.setCourse(course);
                        break;
                }
            }

            student.setUpdatedAt(Instant.now());
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
            Student student = session.get(Student.class, Integer.parseInt(data.get("id")));
            session.delete(student);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Student> getAll() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            List<Student> students = null;
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            students = query.list();
            session.getTransaction().commit();
            session.close();
            return students;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
