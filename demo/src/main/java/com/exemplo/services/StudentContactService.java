package com.exemplo.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import java.time.Instant;
import com.exemplo.models.Student;

@Entity
@Table(name = "student_contact", schema = "public")
public class StudentContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "contact", nullable = false, unique = true)
    private String contact;

    @Column(name = "created_at", columnDefinition = "timestamp with time zone default now()")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp with time zone")
    private Instant updatedAt;

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

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

    // Método para criar um novo registro de StudentContact
    @Override
    public void create(HashMap<String, String> data) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            StudentContact studentContact = new StudentContact();
            Student student = session.get(Student.class, Integer.parseInt(data.get("student_id")));
            studentContact.setStudent(student);
            studentContact.setContact(data.get("contact"));
            studentContact.setCreatedAt(Instant.now());
            studentContact.setUpdatedAt(Instant.now());

            // Salva o novo contato de estudante
            session.save(studentContact);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Método para atualizar um registro existente de StudentContact
    @Override
    public Boolean update(HashMap<String, String> data) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
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
            session.update(studentContact);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    // Método para deletar um registro de StudentContact
    @Override
    public Boolean delete(HashMap<String, String> data) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            StudentContact studentContact = session.get(StudentContact.class, Integer.parseInt(data.get("id")));
            session.delete(studentContact);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    // Método para listar todos os registros de StudentContact
    @Override
    public List<StudentContact> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            Query<StudentContact> query = session.createQuery("FROM StudentContact", StudentContact.class);
            List<StudentContact> studentContacts = query.list();
            session.getTransaction().commit();
            return studentContacts;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
}

