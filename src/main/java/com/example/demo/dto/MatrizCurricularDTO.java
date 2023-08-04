package com.example.demo.dto;

import java.util.List;
import lombok.Data;

@Data
public class MatrizCurricularDTO {

    private Long id;
    private Long numeroSemestres;
    private Long cursoId;
    private List<Long> disciplinaId;

}
