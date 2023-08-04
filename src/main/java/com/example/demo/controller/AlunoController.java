package com.example.demo.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Exception.AlunoException.AlunoNotFoundException;
import com.example.demo.dto.AlunoDTO;
import com.example.demo.service.AlunoService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<AlunoDTO>>> listarTodos() {
        List<AlunoDTO> alunos = alunoService.readAll();
        List<EntityModel<AlunoDTO>> alunoModels = alunos.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(AlunoController.class).withSelfRel();
        CollectionModel<EntityModel<AlunoDTO>> collectionModel = CollectionModel.of(alunoModels, selfLink);
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AlunoDTO>> buscarPorId(@PathVariable Long id) {
        try {
            AlunoDTO aluno = alunoService.readById(id);
            EntityModel<AlunoDTO> alunoModel = toEntityModel(aluno);
            return ResponseEntity.ok(alunoModel);
        } catch (AlunoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<AlunoDTO>> salvar(@RequestBody AlunoDTO alunoDTO) {
        try {
            AlunoDTO novoAluno = alunoService.create(alunoDTO);
            EntityModel<AlunoDTO> alunoModel = toEntityModel(novoAluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(alunoModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<AlunoDTO>> atualizar(@PathVariable Long id, @RequestBody AlunoDTO alunoDTO) {
        try {
            AlunoDTO alunoAtualizado = alunoService.update(id, alunoDTO);
            EntityModel<AlunoDTO> alunoModel = toEntityModel(alunoAtualizado);
            return ResponseEntity.ok(alunoModel);
        } catch (AlunoNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            boolean alunoExcluido = alunoService.delete(id);
            if (alunoExcluido) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (AlunoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private EntityModel<AlunoDTO> toEntityModel(AlunoDTO alunoDTO) {
        Link selfLink = WebMvcLinkBuilder.linkTo(AlunoController.class)
                .slash(alunoDTO.getId())
                .withSelfRel();
        return EntityModel.of(alunoDTO, selfLink);
    }
}
