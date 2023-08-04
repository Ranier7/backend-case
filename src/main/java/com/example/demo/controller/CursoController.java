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
import com.example.demo.Exception.CursoException.CursoNotFoundException;
import com.example.demo.dto.CursoDTO;
import com.example.demo.service.CursoService;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CursoDTO>>> listarTodos() {
        List<CursoDTO> cursos = cursoService.readAll();
        List<EntityModel<CursoDTO>> cursoModels = cursos.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(CursoController.class).withSelfRel();
        CollectionModel<EntityModel<CursoDTO>> collectionModel = CollectionModel.of(cursoModels, selfLink);
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CursoDTO>> buscarPorId(@PathVariable Long id) {
        try {
            CursoDTO curso = cursoService.readById(id);
            EntityModel<CursoDTO> cursoModel = toEntityModel(curso);
            return ResponseEntity.ok(cursoModel);
        } catch (CursoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<CursoDTO>> salvar(@RequestBody CursoDTO cursoDTO) {
        try {
            CursoDTO novoCurso = cursoService.create(cursoDTO);
            EntityModel<CursoDTO> cursoModel = toEntityModel(novoCurso);
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CursoDTO>> atualizar(@PathVariable Long id, @RequestBody CursoDTO cursoDTO) {
        try {
            CursoDTO cursoAtualizado = cursoService.update(id, cursoDTO);
            EntityModel<CursoDTO> cursoModel = toEntityModel(cursoAtualizado);
            return ResponseEntity.ok(cursoModel);
        } catch (CursoNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            boolean cursoExcluido = cursoService.delete(id);
            if (cursoExcluido) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (CursoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private EntityModel<CursoDTO> toEntityModel(CursoDTO curso) {
        Link selfLink = WebMvcLinkBuilder.linkTo(CursoController.class)
                .slash(curso.getId())
                .withSelfRel();
        return EntityModel.of(curso, selfLink);
    }
}
