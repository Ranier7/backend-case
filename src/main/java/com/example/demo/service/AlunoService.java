package com.example.demo.service;

import com.example.demo.Exception.AlunoException.AlunoDeletionException;
import com.example.demo.Exception.AlunoException.AlunoInvalidDataException;
import com.example.demo.Exception.AlunoException.AlunoNotFoundException;
import com.example.demo.dto.AlunoDTO;
import com.example.demo.model.Aluno;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.CursoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private static final Logger logger = LoggerFactory.getLogger(AlunoService.class);

    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    @Autowired
    public AlunoService(AlunoRepository alunoRepository, CursoRepository cursoRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<AlunoDTO> readAll() {
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AlunoDTO readById(Long id) {
        return alunoRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new AlunoNotFoundException("Aluno com o ID " + id + " não encontrado."));
    }

    public AlunoDTO create(AlunoDTO alunoDTO) {
        if (alunoDTO.getNome() == null || alunoDTO.getNome().isEmpty()
                || alunoDTO.getMatricula() == null || alunoDTO.getMatricula().toString().isEmpty()
                || alunoDTO.getCursoId() == null || alunoDTO.getSemestreAtual() == null) {
            throw new AlunoInvalidDataException("Os dados do aluno estão incompletos ou inválidos.");
        }

        Aluno aluno = convertToEntity(alunoDTO);
        return convertToDto(alunoRepository.save(aluno));
    }

    public AlunoDTO update(Long id, AlunoDTO alunoDTO) {
        Aluno alunoExistente = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNotFoundException("Aluno com o ID " + id + " não encontrado."));

        if (alunoDTO.getNome() == null || alunoDTO.getNome().isEmpty()
                || alunoDTO.getMatricula() == null || alunoDTO.getMatricula().toString().isEmpty()
                || alunoDTO.getCursoId() == null || alunoDTO.getSemestreAtual() == null) {
            throw new AlunoInvalidDataException("Os dados do aluno estão incompletos ou inválidos.");
        }

        alunoExistente.setNome(alunoDTO.getNome());
        alunoExistente.setMatricula(alunoDTO.getMatricula());
        alunoExistente.setCurso(cursoRepository.findById(alunoDTO.getCursoId())
                .orElseThrow(() -> new AlunoInvalidDataException(
                        "Curso com o ID " + alunoDTO.getCursoId() + " não encontrado.")));
        alunoExistente.setSemestreAtual(alunoDTO.getSemestreAtual());
        return convertToDto(alunoRepository.save(alunoExistente));
    }

    public boolean delete(Long id) {
        Aluno alunoExistente = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNotFoundException("Aluno com o ID " + id + " não encontrado."));

        logger.info("Aluno com ID " + id + " será deletado: " + alunoExistente.getNome());

        try {
            alunoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new AlunoDeletionException("Erro ao deletar o aluno com o ID " + id + ".");
        }
    }

    private Aluno convertToEntity(AlunoDTO alunoDTO) {
        Aluno aluno = new Aluno();
        aluno.setId(alunoDTO.getId());
        aluno.setNome(alunoDTO.getNome());
        aluno.setMatricula(alunoDTO.getMatricula());
        aluno.setCurso(cursoRepository.findById(alunoDTO.getCursoId()).orElse(null));
        aluno.setSemestreAtual(alunoDTO.getSemestreAtual());
        return aluno;
    }

    private AlunoDTO convertToDto(Aluno aluno) {
        AlunoDTO alunoDTO = new AlunoDTO();
        alunoDTO.setId(aluno.getId());
        alunoDTO.setNome(aluno.getNome());
        alunoDTO.setMatricula(aluno.getMatricula());
        alunoDTO.setCursoId(aluno.getCurso() != null ? aluno.getCurso().getId() : null);
        alunoDTO.setSemestreAtual(aluno.getSemestreAtual());
        return alunoDTO;
    }
}
