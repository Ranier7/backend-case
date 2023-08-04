package com.example.demo.dto;

import lombok.Data;

@Data
public class AlunoDTO {

    private Long id;
    private String nome;
    private Long matricula;
    private Long cursoId;
    private Long semestreAtual;

}
