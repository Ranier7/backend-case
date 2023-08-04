package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import com.example.demo.Exception.CursoException.CursoInvalidDataException;
import com.example.demo.Exception.CursoException.CursoNotFoundException;
import com.example.demo.Exception.CursoException.CursoDeletionException;
import com.example.demo.dto.CursoDTO;
import com.example.demo.model.Curso;
import com.example.demo.repository.CursoRepository;

@Service
public class CursoService {

    private static final Logger logger = LoggerFactory.getLogger(CursoService.class);

    private final CursoRepository cursoRepository;

    @Autowired
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<CursoDTO> readAll() {
        List<Curso> cursos = cursoRepository.findAll();
        return cursos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CursoDTO readById(Long id) {
        return cursoRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new CursoNotFoundException("Curso com o ID " + id + " não encontrado."));
    }

    public CursoDTO create(CursoDTO cursoDTO) {
        if (cursoDTO.getNome() == null || cursoDTO.getNome().isEmpty()
                || cursoDTO.getCodigo() == null || cursoDTO.getCodigo().toString().isEmpty()
                || cursoDTO.getDepartamento() == null || cursoDTO.getDepartamento().isEmpty()
                || cursoDTO.getCargaHorariaTotal() == null || cursoDTO.getCargaHorariaTotal().toString().isEmpty()) {
            throw new CursoInvalidDataException("Os dados do curso estão incompletos ou inválidos.");
        }
        Curso curso = convertToEntity(cursoDTO);
        return convertToDto(cursoRepository.save(curso));
    }

    public CursoDTO update(Long id, CursoDTO cursoDTO) {
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException("Curso com o ID " + id + " não encontrado."));
        if (cursoDTO.getNome() == null || cursoDTO.getNome().isEmpty()
                || cursoDTO.getCodigo() == null || cursoDTO.getCodigo().toString().isEmpty()
                || cursoDTO.getDepartamento() == null || cursoDTO.getDepartamento().isEmpty()
                || cursoDTO.getCargaHorariaTotal() == null || cursoDTO.getCargaHorariaTotal().toString().isEmpty()) {
            throw new CursoInvalidDataException("Os dados do curso estão incompletos ou inválidos.");
        }
        cursoExistente.setNome(cursoDTO.getNome());
        cursoExistente.setCodigo(cursoDTO.getCodigo());
        cursoExistente.setDepartamento(cursoDTO.getDepartamento());
        cursoExistente.setCargaHorariaTotal(cursoDTO.getCargaHorariaTotal());
        return convertToDto(cursoRepository.save(cursoExistente));
    }

    public boolean delete(Long id) {
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException("Aluno com o ID " + id + " não encontrado."));

        logger.info("Curso com ID " + id + " será deletado: " + cursoExistente.getNome());
        try {
            cursoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new CursoDeletionException("Erro ao deletar o curso com o ID " + id + ".");
        }
    }

    private Curso convertToEntity(CursoDTO cursoDTO) {
        Curso curso = new Curso();
        curso.setId(cursoDTO.getId());
        curso.setNome(cursoDTO.getNome());
        curso.setCodigo(cursoDTO.getCodigo());
        curso.setDepartamento(cursoDTO.getDepartamento());
        curso.setCargaHorariaTotal(cursoDTO.getCargaHorariaTotal());
        return curso;
    }

    private CursoDTO convertToDto(Curso curso) {
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setId(curso.getId());
        cursoDTO.setNome(curso.getNome());
        cursoDTO.setCodigo(curso.getCodigo());
        cursoDTO.setDepartamento(curso.getDepartamento());
        cursoDTO.setCargaHorariaTotal(curso.getCargaHorariaTotal());
        return cursoDTO;
    }

}
