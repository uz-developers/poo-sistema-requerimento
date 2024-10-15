package com.exemplo.models;

import javax.persistence.*;
import java.time.Instant;

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

    @Column(name = "updated_at", columnDefinition = "timestamp with time zone default now()")
    private Instant updatedAt;

   
    public StudentContact() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public StudentContact(Student student, String contact) {
        this.student = student;
        this.contact = contact;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    
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

    
    // Criar novo contato
    public static StudentContact create(Student student, String contact) {
        return new StudentContact(student, contact);
    }

    // Ler informações do contato
    public void read() {
        System.out.println("ID: " + this.getId());
        System.out.println("Estudante: " + this.getStudent().getName());
        System.out.println("Contato: " + this.getContact());
        System.out.println("Criado em: " + this.getCreatedAt());
        System.out.println("Atualizado em: " + this.getUpdatedAt());
    }

    // Atualizar contato
    public void update(String newContact) {
        this.setContact(newContact);
        this.setUpdatedAt(Instant.now());
    }

    // Deletar contato
    public void delete() {
        System.out.println("Contato deletado: " + this.getContact());
    }
}
