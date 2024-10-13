package com.exemplo.services;

import com.exemplo.models.Course;
import com.exemplo.models.enums.RegimeEnum;
import com.exemplo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class CourseService extends Service {
    @Override
    public void create(HashMap<String, String> data) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Course course = new Course();
        course.setName(data.get("name"));
        course.setRegime(RegimeEnum.valueOf(data.get("regime"))); 
        course.setCreatedAt(Instant.now());
        course.setUpdatedAt(Instant.now());
        
        session.save(course);
        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
    }

    @Override
    public Boolean update(HashMap<String, String> data) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Course existingCourse = session.get(Course.class, Integer.parseInt(data.get("id")));

            if (existingCourse != null) {
                
                for (String field: data.keySet()) {
                    switch (field) {
                        case "name":
                            existingCourse.setName(data.get(field));
                            break;
                        case "regime":
                            existingCourse.setRegime(RegimeEnum.fromValue(data.get(field)));
                            break;
                    }
                }
                existingCourse.setUpdatedAt(Instant.now());
                session.getTransaction().commit();
                session.close();
                return true;
            }
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
            Course course = session.get(Course.class, Integer.parseInt(data.get("id")));
            
            if (course != null) {
                session.delete(course);
                session.getTransaction().commit();
                session.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Course> getAll() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Course> query = session.createQuery("FROM Course", Course.class);
            List<Course> courses = query.list();
            session.getTransaction().commit();
            session.close();
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
