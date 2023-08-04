package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Exception.ProfessorException.ProfessorNotFoundException;
import com.example.demo.dto.ProfessorDTO;
import com.example.demo.service.ProfessorService;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProfessorDTO>>> listarTodos() {

        List<ProfessorDTO> professores = professorService.readAll();
        List<EntityModel<ProfessorDTO>> professorModels = professores.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(ProfessorController.class).withSelfRel();
        CollectionModel<EntityModel<ProfessorDTO>> collectionModel = CollectionModel.of(professorModels, selfLink);
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProfessorDTO>> buscarPorId(@PathVariable Long id) {
        try {
            ProfessorDTO professor = professorService.readById(id);
            EntityModel<ProfessorDTO> professorModel = toEntityModel(professor);
            return ResponseEntity.ok(professorModel);
        } catch (ProfessorNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<ProfessorDTO>> salvar(@RequestBody ProfessorDTO professorDTO) {
        try {
            ProfessorDTO novoProfessor = professorService.create(professorDTO);
            EntityModel<ProfessorDTO> professorModel = toEntityModel(novoProfessor);
            return ResponseEntity.status(HttpStatus.CREATED).body(professorModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProfessorDTO>> atualizar(@PathVariable Long id,
            @RequestBody ProfessorDTO professorDTO) {
        try {
            ProfessorDTO professorAtualizado = professorService.update(id, professorDTO);
            EntityModel<ProfessorDTO> professorModel = toEntityModel(professorAtualizado);
            return ResponseEntity.ok(professorModel);
        } catch (ProfessorNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            boolean professorExcluido = professorService.delete(id);
            if (professorExcluido) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ProfessorNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private EntityModel<ProfessorDTO> toEntityModel(ProfessorDTO professorDTO) {
        Link selfLink = WebMvcLinkBuilder.linkTo(ProfessorController.class)
                .slash(professorDTO.getId())
                .withSelfRel();
        return EntityModel.of(professorDTO, selfLink);
    }
}
