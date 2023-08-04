package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Exception.ProfessorException.ProfessorInvalidDataException;
import com.example.demo.Exception.ProfessorException.ProfessorNotFoundException;
import com.example.demo.Exception.ProfessorException.ProfessorDeletionException;
import com.example.demo.dto.ProfessorDTO;
import com.example.demo.model.Professor;
import com.example.demo.repository.ProfessorRepository;

@Service
public class ProfessorService {

    private static final Logger logger = LoggerFactory.getLogger(ProfessorService.class);

    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public List<ProfessorDTO> readAll() {
        List<Professor> professores = professorRepository.findAll();
        return professores.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProfessorDTO readById(Long id) {
        return professorRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com o ID " + id + " não encontrado."));
    }

    public ProfessorDTO create(ProfessorDTO professorDTO) {
        if (professorDTO.getNome() == null || professorDTO.getNome().isEmpty()
                || professorDTO.getDepartamento() == null || professorDTO.getDepartamento().isEmpty()) {
            throw new ProfessorInvalidDataException("Os dados do professor estão incompletos ou inválidos.");
        }
        Professor professor = convertToEntity(professorDTO);
        return convertToDto(professorRepository.save(professor));
    }

    public ProfessorDTO update(Long id, ProfessorDTO professorDTO) {
        Professor professorExistente = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com o ID " + id + " não encontrado."));
        if (professorDTO.getNome() == null || professorDTO.getNome().isEmpty()
                || professorDTO.getDepartamento() == null || professorDTO.getDepartamento().isEmpty()) {
            throw new ProfessorInvalidDataException("Os dados do professor estão incompletos ou inválidos.");
        }
        professorExistente.setNome(professorDTO.getNome());
        professorExistente.setDepartamento(professorDTO.getDepartamento());
        return convertToDto(professorRepository.save(professorExistente));
    }

    public boolean delete(Long id) {
        Professor professorExistente = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com o ID " + id + " não encontrado."));

        logger.info("Professor com ID " + id + " será deletado: " + professorExistente.getNome());
        try {
            professorRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new ProfessorDeletionException("Erro ao deletar o professor com o ID " + id + ".");
        }
    }

    private Professor convertToEntity(ProfessorDTO professorDTO) {
        Professor professor = new Professor();
        professor.setId(professorDTO.getId());
        professor.setNome(professorDTO.getNome());
        professor.setDepartamento(professorDTO.getDepartamento());
        return professor;
    }

    private ProfessorDTO convertToDto(Professor professor) {
        ProfessorDTO professorDTO = new ProfessorDTO();
        professorDTO.setId(professor.getId());
        professorDTO.setNome(professor.getNome());
        professorDTO.setDepartamento(professor.getDepartamento());
        return professorDTO;
    }

}
