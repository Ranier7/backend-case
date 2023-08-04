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
import com.example.demo.Exception.MatrizCurricularException.MatrizCurricularNotFoundException;
import com.example.demo.dto.MatrizCurricularDTO;
import com.example.demo.service.MatrizCurricularService;

@RestController
@RequestMapping("/api/matrizesCurriculares")
public class MatrizCurricularController {

    private final MatrizCurricularService matrizCurricularService;

    public MatrizCurricularController(MatrizCurricularService matrizCurricularService) {
        this.matrizCurricularService = matrizCurricularService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<MatrizCurricularDTO>>> listarTodas() {

        List<MatrizCurricularDTO> matrizesCurriculares = matrizCurricularService.readAll();
        List<EntityModel<MatrizCurricularDTO>> matrizesCurricularesModels = matrizesCurriculares.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(MatrizCurricularController.class).withSelfRel();
        CollectionModel<EntityModel<MatrizCurricularDTO>> collectionModel = CollectionModel
                .of(matrizesCurricularesModels, selfLink);
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MatrizCurricularDTO>> buscarPorId(@PathVariable Long id) {
        try {
            MatrizCurricularDTO matrizCurricular = matrizCurricularService.readById(id);
            EntityModel<MatrizCurricularDTO> matrizCurricularModel = toEntityModel(matrizCurricular);
            return ResponseEntity.ok(matrizCurricularModel);
        } catch (MatrizCurricularNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<MatrizCurricularDTO>> salvar(
            @RequestBody MatrizCurricularDTO matrizCurricularDTO) {
        try {
            MatrizCurricularDTO novaMatrizCurricular = matrizCurricularService.create(matrizCurricularDTO);
            EntityModel<MatrizCurricularDTO> matrizCurricularModel = toEntityModel(novaMatrizCurricular);
            return ResponseEntity.status(HttpStatus.CREATED).body(matrizCurricularModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<MatrizCurricularDTO>> atualizar(@PathVariable Long id,
            @RequestBody MatrizCurricularDTO matrizCurricularDTO) {
        try {
            MatrizCurricularDTO matrizCurricularAtualizada = matrizCurricularService.update(id, matrizCurricularDTO);
            EntityModel<MatrizCurricularDTO> matrizCurricularModel = toEntityModel(matrizCurricularAtualizada);
            return ResponseEntity.ok(matrizCurricularModel);
        } catch (MatrizCurricularNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            boolean matrizCurricularExcluida = matrizCurricularService.delete(id);
            if (matrizCurricularExcluida) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (MatrizCurricularNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private EntityModel<MatrizCurricularDTO> toEntityModel(MatrizCurricularDTO matrizCurricularDTO) {
        Link selfLink = WebMvcLinkBuilder.linkTo(MatrizCurricularController.class)
                .slash(matrizCurricularDTO.getId())
                .withSelfRel();
        return EntityModel.of(matrizCurricularDTO, selfLink);
    }
}
