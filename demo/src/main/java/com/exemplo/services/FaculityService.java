package com.exemplo.services;

import com.exemplo.models.Faculity;
import com.exemplo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class FaculityService extends Service {
    @Override
    public void create(HashMap<String, String> data) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Faculity faculity = new Faculity();
            faculity.setName(data.get("name"));
            faculity.setEmail(data.get("email"));
            faculity.setCreatedAt(Instant.now());
            faculity.setUpdatedAt(Instant.now());

            session.save(faculity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Boolean update(HashMap<String, String> data) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Faculity existingFaculity = session.get(Faculity.class, Integer.parseInt(data.get("id")));

            if (existingFaculity != null) {
                existingFaculity.setName(data.get("name"));
                existingFaculity.setEmail(data.get("email"));
                existingFaculity.setUpdatedAt(Instant.now());
                session.update(existingFaculity);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(HashMap<String, String> data) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Faculity faculity = session.get(Faculity.class, Integer.parseInt(data.get("id")));

            if (faculity != null) {
                session.delete(faculity);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Faculity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Faculity> query = session.createQuery("FROM Faculity", Faculity.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
