package com.exemplo.services;

import java.util.List;
import java.util.HashMap;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.exemplo.util.HibernateUtil;
import com.exemplo.models.User;
import java.time.Instant;

public class UserService extends Service {
    @Override
    public void create(HashMap<String, String> data) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        User user = new User();
        user.setName(data.get("name"));
        user.setNickname(data.get("nickname"));
        user.setEmail(data.get("email"));
        user.setPasswd(data.get("passwd"));
        user.setUsername(data.get("username"));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        session.save(user);

        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
    }

    @Override
    public Boolean update(HashMap<String, String> data) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            User newUser = session.get(User.class, Integer.parseInt(data.get("id")));
    
            for (String field: data.keySet()) {
                switch (field) {
                    case "name":
                        newUser.setName(data.get(field));
                        break;
                    case "nickname":
                        newUser.setNickname(data.get(field));
                        break;
                    case "email":
                        newUser.setEmail(data.get(field));
                        break;
                    case "passwd":
                        newUser.setPasswd(data.get(field));
                    break;
                    case "username":
                        newUser.setUsername(data.get(field));
                        break;
                }
            }
    
            newUser.setUpdatedAt(Instant.now());
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
            User user = session.get(User.class, Integer.parseInt(data.get("id")));
            session.delete(user);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User> getAll() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            List<User> users = null;
            Query<User> query = session.createQuery("FROM User", User.class);
            users = query.list();
            session.getTransaction().commit();
            session.close();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}