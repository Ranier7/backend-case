package com.example.demo.dto;

import java.util.List;
import lombok.Data;

@Data
public class DisciplinaDTO {

    private Long id;
    private String nome;
    private Long codigo;
    private String cargaHoraria;
    private List<Long> professorId;
    private Long semestreId;

}
