package com.example.demo.model;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Long codigo;

    private String cargaHoraria;

    @ManyToMany
    @JoinColumn(name = "professor_id")
    private Set<Professor> professor;

    @ManyToOne
    @JoinColumn(name = "semestre_id")
    private Semestre semestre;

}
