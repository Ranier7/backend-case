package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Exception.SemestreException.SemestreInvalidDataException;
import com.example.demo.Exception.SemestreException.SemestreNotFoundException;
import com.example.demo.Exception.SemestreException.SemestreDeletionException;
import com.example.demo.dto.SemestreDTO;
import com.example.demo.model.Semestre;
import com.example.demo.repository.SemestreRepository;

@Service
public class SemestreService {

    private static final Logger logger = LoggerFactory.getLogger(SemestreService.class);

    private final SemestreRepository semestreRepository;

    @Autowired
    public SemestreService(SemestreRepository semestreRepository) {
        this.semestreRepository = semestreRepository;
    }

    public List<SemestreDTO> readAll() {
        List<Semestre> semestres = semestreRepository.findAll();
        return semestres.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SemestreDTO readById(Long id) {
        return semestreRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new SemestreNotFoundException("Semestre com o ID " + id + " não encontrado."));
    }

    public SemestreDTO create(SemestreDTO semestreDTO) {
        if (semestreDTO.getPeriodo() == null || semestreDTO.getPeriodo().toString().isEmpty()) {
            throw new SemestreInvalidDataException("Os dados do semestre estão incompletos ou inválidos.");
        }
        Semestre semestre = convertToEntity(semestreDTO);
        return convertToDto(semestreRepository.save(semestre));
    }

    public SemestreDTO update(Long id, SemestreDTO semestreDTO) {
        Semestre semestreExistente = semestreRepository.findById(id)
                .orElseThrow(() -> new SemestreNotFoundException("Semestre com o ID " + id + " não encontrado."));
        if (semestreDTO.getPeriodo() == null || semestreDTO.getPeriodo().toString().isEmpty()) {
            throw new SemestreInvalidDataException("Os dados do semestre estão incompletos ou inválidos.");
        }
        semestreExistente.setPeriodo(semestreDTO.getPeriodo());
        return convertToDto(semestreRepository.save(semestreExistente));
    }

    public boolean delete(Long id) {
        Semestre semestreExistente = semestreRepository.findById(id)
                .orElseThrow(() -> new SemestreNotFoundException("Semestre com o ID " + id + " não encontrado."));

        logger.info("Semestre com ID " + id + " será deletado: " + semestreExistente.getPeriodo());
        try {
            semestreRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new SemestreDeletionException("Erro ao deletar o professor com o ID " + id + ".");
        }
    }

    private Semestre convertToEntity(SemestreDTO semestreDTO) {
        Semestre semestre = new Semestre();
        semestre.setId(semestreDTO.getId());
        semestre.setPeriodo(semestreDTO.getPeriodo());
        return semestre;
    }

    private SemestreDTO convertToDto(Semestre semestre) {
        SemestreDTO semestreDTO = new SemestreDTO();
        semestreDTO.setId(semestre.getId());
        semestreDTO.setPeriodo(semestre.getPeriodo());
        return semestreDTO;
    }

}
