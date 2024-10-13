package com.exemplo.models;

import javax.persistence.*;
import java.time.Instant;
import com.exemplo.models.enums.RegimeEnum;

@Entity
@Table(name = "course", schema = "public")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "regime", nullable = false)
    private RegimeEnum regime; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculity_id", nullable = false)
    private Faculity faculity; // Relacionamento com a entidade Faculity

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RegimeEnum getRegime() {
        return regime;
    }

    public void setRegime(RegimeEnum regime) {
        this.regime = regime;
    }

    public Faculity getFaculity() {
        return faculity;
    }

    public void setFaculity(Faculity faculity) {
        this.faculity = faculity;
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