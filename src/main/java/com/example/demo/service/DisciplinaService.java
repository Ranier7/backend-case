package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Exception.DisciplinaException.DisciplinaInvalidDataException;
import com.example.demo.Exception.DisciplinaException.DisciplinaNotFoundException;
import com.example.demo.Exception.DisciplinaException.DisciplinaDeletionException;
import com.example.demo.dto.DisciplinaDTO;
import com.example.demo.model.Disciplina;
import com.example.demo.model.Professor;
import com.example.demo.model.Semestre;
import com.example.demo.repository.DisciplinaRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.SemestreRepository;

@Service
public class DisciplinaService {

    private static final Logger logger = LoggerFactory.getLogger(DisciplinaService.class);

    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;
    private final SemestreRepository semestreRepository;

    @Autowired
    public DisciplinaService(DisciplinaRepository disciplinaRepository,
            ProfessorRepository professorRepository, SemestreRepository semestreRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
        this.semestreRepository = semestreRepository;
    }

    public List<DisciplinaDTO> readAll() {
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        return disciplinas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DisciplinaDTO readById(Long id) {
        return disciplinaRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com o ID " + id + " não encontrado."));
    }

    public DisciplinaDTO create(DisciplinaDTO disciplinaDTO) {
        if (disciplinaDTO.getProfessorId() == null || disciplinaDTO.getProfessorId().isEmpty()) {
            throw new IllegalArgumentException("A disciplina deve ter pelo menos um professor.");
        }
        if (disciplinaDTO.getSemestreId() == null) {
            throw new IllegalArgumentException("A disciplina deve estar associada a um semestre válido.");
        }
        Disciplina disciplina = convertToEntity(disciplinaDTO);
        Disciplina savedDisciplina = disciplinaRepository.save(disciplina);
        return convertToDto(savedDisciplina);
    }

    public DisciplinaDTO update(Long id, DisciplinaDTO disciplinaDTO) {
        Disciplina disciplinaExistente = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com o ID " + id + " não encontrado."));
        if (disciplinaDTO.getNome() == null || disciplinaDTO.getNome().isEmpty()
                || disciplinaDTO.getCodigo() == null || disciplinaDTO.getCodigo().toString().isEmpty()
                || disciplinaDTO.getCargaHoraria() == null || disciplinaDTO.getCargaHoraria().isEmpty()
                || disciplinaDTO.getProfessorId() == null || disciplinaDTO.getProfessorId().isEmpty()
                || disciplinaDTO.getSemestreId() == null || disciplinaDTO.getSemestreId().toString().isEmpty()) {
            throw new DisciplinaInvalidDataException("Os dados da disciplina estão incompletos ou inválidos.");
        }
        Disciplina disciplinaAtualizada = convertToEntity(disciplinaDTO);
        disciplinaExistente.setNome(disciplinaAtualizada.getNome());
        disciplinaExistente.setCodigo(disciplinaAtualizada.getCodigo());
        disciplinaExistente.setCargaHoraria(disciplinaAtualizada.getCargaHoraria());
        disciplinaExistente.setProfessor(disciplinaAtualizada.getProfessor());
        disciplinaExistente.setSemestre(disciplinaAtualizada.getSemestre());
        Disciplina updatedDisciplina = disciplinaRepository.save(disciplinaExistente);
        return convertToDto(updatedDisciplina);
    }

    public boolean delete(Long id) {
        Disciplina disciplinaExistente = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com o ID " + id + " não encontrado."));

        logger.info("Disciplina com ID " + id + " será deletada: " + disciplinaExistente.getNome());

        try {
            disciplinaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DisciplinaDeletionException("Erro ao deletar a disciplina com o ID " + id + ".");
        }
    }

    private DisciplinaDTO convertToDto(Disciplina disciplina) {
        DisciplinaDTO disciplinaDTO = new DisciplinaDTO();
        disciplinaDTO.setId(disciplina.getId());
        disciplinaDTO.setNome(disciplina.getNome());
        disciplinaDTO.setCodigo(disciplina.getCodigo());
        disciplinaDTO.setCargaHoraria(disciplina.getCargaHoraria());

        List<Long> professorIds = disciplina.getProfessor().stream()
                .map(Professor::getId)
                .collect(Collectors.toList());
        disciplinaDTO.setProfessorId(professorIds);

        disciplinaDTO.setSemestreId(disciplina.getSemestre().getId());

        return disciplinaDTO;
    }

    private Disciplina convertToEntity(DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(disciplinaDTO.getId());
        disciplina.setNome(disciplinaDTO.getNome());
        disciplina.setCodigo(disciplinaDTO.getCodigo());
        disciplina.setCargaHoraria(disciplinaDTO.getCargaHoraria());

        Set<Professor> professores = new HashSet<>();
        for (Long professorId : disciplinaDTO.getProfessorId()) {
            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new DisciplinaInvalidDataException(
                            "Professor com o ID " + professorId + " não encontrado."));
            professores.add(professor);
        }
        disciplina.setProfessor(professores);

        Semestre semestre = new Semestre();
        semestre.setId(disciplinaDTO.getSemestreId());
        disciplina.setSemestre(semestreRepository.findById(disciplinaDTO.getSemestreId())
                .orElseThrow(() -> new DisciplinaInvalidDataException(
                        "Semestre com o ID " + disciplinaDTO.getSemestreId() + " não encontrado.")));

        return disciplina;
    }

}
