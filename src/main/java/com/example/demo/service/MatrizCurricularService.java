package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Exception.MatrizCurricularException.MatrizCurricularInvalidDataException;
import com.example.demo.Exception.MatrizCurricularException.MatrizCurricularNotFoundException;
import com.example.demo.Exception.MatrizCurricularException.MatrizCurricularDeletionException;
import com.example.demo.dto.MatrizCurricularDTO;
import com.example.demo.model.Curso;
import com.example.demo.model.Disciplina;
import com.example.demo.model.MatrizCurricular;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.DisciplinaRepository;
import com.example.demo.repository.MatrizCurricularRepository;

@Service
public class MatrizCurricularService {

    private static final Logger logger = LoggerFactory.getLogger(MatrizCurricularService.class);

    private final MatrizCurricularRepository matrizCurricularRepository;
    private final CursoRepository cursoRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Autowired
    public MatrizCurricularService(MatrizCurricularRepository matrizCurricularRepository,
            CursoRepository cursoRepository, DisciplinaRepository disciplinaRepository) {
        this.matrizCurricularRepository = matrizCurricularRepository;
        this.cursoRepository = cursoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<MatrizCurricularDTO> readAll() {
        List<MatrizCurricular> matrizes = matrizCurricularRepository.findAll();
        return matrizes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MatrizCurricularDTO readById(Long id) {
        return matrizCurricularRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new MatrizCurricularNotFoundException(
                        "MatrizCurricular com o ID " + id + " não encontrado."));
    }

    public MatrizCurricularDTO create(MatrizCurricularDTO matrizCurricularDTO) {
        if (matrizCurricularDTO.getDisciplinaId() == null || matrizCurricularDTO.getDisciplinaId().isEmpty()) {
            throw new IllegalArgumentException("A matriz curricular deve ter pelo menos uma disciplina.");
        }
        if (matrizCurricularDTO.getCursoId() == null || matrizCurricularDTO.getCursoId().toString().isEmpty()) {
            throw new IllegalArgumentException("A matriz curricular deve estar associada a um curso válido.");
        }
        MatrizCurricular matrizCurricular = convertToEntity(matrizCurricularDTO);
        MatrizCurricular savedMatrizCurricular = matrizCurricularRepository.save(matrizCurricular);
        return convertToDto(savedMatrizCurricular);
    }

    public MatrizCurricularDTO update(Long id, MatrizCurricularDTO matrizCurricularDTO) {
        MatrizCurricular matrizCurricularExistente = matrizCurricularRepository.findById(id)
                .orElseThrow(
                        () -> new MatrizCurricularNotFoundException(
                                "MatrizCurricular com o ID " + id + " não encontrado."));
        if (matrizCurricularDTO.getNumeroSemestres() == null
                || matrizCurricularDTO.getNumeroSemestres().toString().isEmpty()
                || matrizCurricularDTO.getCursoId() == null
                || matrizCurricularDTO.getCursoId().toString().isEmpty()
                || matrizCurricularDTO.getDisciplinaId() == null
                || matrizCurricularDTO.getDisciplinaId().toString().isEmpty()) {
            throw new MatrizCurricularInvalidDataException(
                    "Os dados da MatrizCurricular estão incompletos ou inválidos.");
        }
        MatrizCurricular matrizCurricularAtualizada = convertToEntity(matrizCurricularDTO);
        matrizCurricularExistente.setNumeroSemestres(matrizCurricularAtualizada.getNumeroSemestres());
        matrizCurricularExistente.setCurso(matrizCurricularAtualizada.getCurso());
        matrizCurricularExistente.setDisciplina(matrizCurricularAtualizada.getDisciplina());
        MatrizCurricular updatedMatrizCurricular = matrizCurricularRepository.save(matrizCurricularExistente);
        return convertToDto(updatedMatrizCurricular);
    }

    public boolean delete(Long id) {
        MatrizCurricular matrizCurricularExistente = matrizCurricularRepository.findById(id)
                .orElseThrow(() -> new MatrizCurricularNotFoundException(
                        "MatrizCurricular com o ID " + id + " não encontrado."));

        logger.info(
                "MatrizCurricular com ID " + id + " será deletada: " + matrizCurricularExistente.getNumeroSemestres());

        try {
            matrizCurricularRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new MatrizCurricularDeletionException("Erro ao deletar a MatrizCurricular com o ID " + id + ".");
        }
    }

    private MatrizCurricularDTO convertToDto(MatrizCurricular matrizCurricular) {
        MatrizCurricularDTO matrizCurricularDTO = new MatrizCurricularDTO();
        matrizCurricularDTO.setId(matrizCurricular.getId());
        matrizCurricularDTO.setNumeroSemestres(matrizCurricular.getNumeroSemestres());
        matrizCurricularDTO.setCursoId(matrizCurricular.getCurso().getId());

        List<Long> disciplinaIds = matrizCurricular.getDisciplina().stream()
                .map(Disciplina::getId)
                .collect(Collectors.toList());
        matrizCurricularDTO.setDisciplinaId(disciplinaIds);

        return matrizCurricularDTO;
    }

    private MatrizCurricular convertToEntity(MatrizCurricularDTO matrizCurricularDTO) {
        MatrizCurricular matrizCurricular = new MatrizCurricular();
        matrizCurricular.setId(matrizCurricularDTO.getId());
        matrizCurricular.setNumeroSemestres(matrizCurricularDTO.getNumeroSemestres());

        Curso curso = new Curso();
        curso.setId(matrizCurricularDTO.getCursoId());
        matrizCurricular.setCurso(cursoRepository.findById(matrizCurricularDTO.getCursoId())
                .orElseThrow(() -> new MatrizCurricularInvalidDataException(
                        "Curso com o ID " + matrizCurricularDTO.getCursoId() + " não encontrado.")));

        Set<Disciplina> disciplinas = new HashSet<>();
        for (Long disciplinaId : matrizCurricularDTO.getDisciplinaId()) {
            Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                    .orElseThrow(() -> new MatrizCurricularInvalidDataException(
                            "Disciplina com o ID " + disciplinaId + " não encontrado."));
            disciplinas.add(disciplina);
        }
        matrizCurricular.setDisciplina(disciplinas);

        return matrizCurricular;
    }
}
