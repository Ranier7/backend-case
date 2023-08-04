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
import com.example.demo.Exception.SemestreException.SemestreNotFoundException;
import com.example.demo.dto.SemestreDTO;
import com.example.demo.service.SemestreService;

@RestController
@RequestMapping("/api/semestres")
public class SemestreController {

    private final SemestreService semestreService;

    public SemestreController(SemestreService semestreService) {
        this.semestreService = semestreService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<SemestreDTO>>> listarTodos() {
        List<SemestreDTO> semestres = semestreService.readAll();
        List<EntityModel<SemestreDTO>> semestreModels = semestres.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(SemestreController.class).withSelfRel();
        CollectionModel<EntityModel<SemestreDTO>> collectionModel = CollectionModel.of(semestreModels, selfLink);
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SemestreDTO>> buscarPorId(@PathVariable Long id) {
        try {
            SemestreDTO semestre = semestreService.readById(id);
            EntityModel<SemestreDTO> semestreModel = toEntityModel(semestre);
            return ResponseEntity.ok(semestreModel);
        } catch (SemestreNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<SemestreDTO>> salvar(@RequestBody SemestreDTO semestreDTO) {
        try {
            SemestreDTO novoSemestre = semestreService.create(semestreDTO);
            EntityModel<SemestreDTO> semestreModel = toEntityModel(novoSemestre);
            return ResponseEntity.status(HttpStatus.CREATED).body(semestreModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SemestreDTO>> atualizar(@PathVariable Long id,
            @RequestBody SemestreDTO semestreDTO) {
        try {
            SemestreDTO semestreAtualizado = semestreService.update(id, semestreDTO);
            EntityModel<SemestreDTO> semestreModel = toEntityModel(semestreAtualizado);
            return ResponseEntity.ok(semestreModel);
        } catch (SemestreNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            boolean semestreExcluido = semestreService.delete(id);
            if (semestreExcluido) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (SemestreNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private EntityModel<SemestreDTO> toEntityModel(SemestreDTO semestre) {
        Link selfLink = WebMvcLinkBuilder.linkTo(SemestreController.class)
                .slash(semestre.getId())
                .withSelfRel();
        return EntityModel.of(semestre, selfLink);
    }
}
