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
import com.example.demo.Exception.DisciplinaException.DisciplinaNotFoundException;
import com.example.demo.dto.DisciplinaDTO;
import com.example.demo.service.DisciplinaService;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<DisciplinaDTO>>> listarTodas() {

        List<DisciplinaDTO> disciplinaDTOs = disciplinaService.readAll();
        List<EntityModel<DisciplinaDTO>> disciplinaModels = disciplinaDTOs.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(DisciplinaController.class).withSelfRel();
        CollectionModel<EntityModel<DisciplinaDTO>> collectionModel = CollectionModel.of(disciplinaModels, selfLink);
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DisciplinaDTO>> buscarPorId(@PathVariable Long id) {
        try {
            DisciplinaDTO disciplinaDTO = disciplinaService.readById(id);
            EntityModel<DisciplinaDTO> disciplinaModel = toEntityModel(disciplinaDTO);
            return ResponseEntity.ok(disciplinaModel);
        } catch (DisciplinaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<DisciplinaDTO>> salvar(@RequestBody DisciplinaDTO disciplinaDTO) {
        try {
            DisciplinaDTO novaDisciplinaDTO = disciplinaService.create(disciplinaDTO);
            EntityModel<DisciplinaDTO> disciplinaModel = toEntityModel(novaDisciplinaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<DisciplinaDTO>> atualizar(@PathVariable Long id,
            @RequestBody DisciplinaDTO disciplinaDTO) {
        try {
            DisciplinaDTO disciplinaAtualizada = disciplinaService.update(id, disciplinaDTO);
            EntityModel<DisciplinaDTO> disciplinaModel = toEntityModel(disciplinaAtualizada);
            return ResponseEntity.ok(disciplinaModel);
        } catch (DisciplinaNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            boolean disciplinaExcluida = disciplinaService.delete(id);
            if (disciplinaExcluida) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (DisciplinaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private EntityModel<DisciplinaDTO> toEntityModel(DisciplinaDTO disciplinaDTO) {
        Link selfLink = WebMvcLinkBuilder.linkTo(DisciplinaController.class)
                .slash(disciplinaDTO.getId())
                .withSelfRel();
        return EntityModel.of(disciplinaDTO, selfLink);
    }
}
