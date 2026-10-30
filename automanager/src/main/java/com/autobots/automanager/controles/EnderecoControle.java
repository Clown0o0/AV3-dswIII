package com.autobots.automanager.controles;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.AdicionadorLinkEndereco;
import com.autobots.automanager.service.EnderecoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/enderecos")
@Tag(name = "Endereços", description = "Rotas do endereço (1:1) de usuários e empresas.")
public class EnderecoControle {

    @Autowired
    private EnderecoService service;

    @Autowired
    private AdicionadorLinkEndereco adicionadorLink;

    @Operation(summary = "Cadastra o endereço de um usuário (usuarioId) ou empresa (empresaId)")
    @PostMapping
    public ResponseEntity<Endereco> criar(@RequestBody @Valid EnderecoDTO dto) {
        Endereco endereco = service.criar(dto);
        adicionadorLink.adicionarLink(endereco);
        return ResponseEntity.created(endereco.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(endereco);
    }

    @Operation(summary = "Lista os endereços")
    @GetMapping
    public ResponseEntity<CollectionModel<Endereco>> listar() {
        List<Endereco> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(EnderecoControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca um endereço pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscar(@PathVariable Long id) {
        Endereco endereco = service.buscar(id);
        adicionadorLink.adicionarLink(endereco);
        return ResponseEntity.ok(endereco);
    }

    @Operation(summary = "Atualiza um endereço")
    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizar(@PathVariable Long id, @RequestBody @Valid EnderecoDTO dto) {
        Endereco endereco = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(endereco);
        return ResponseEntity.ok(endereco);
    }

    @Operation(summary = "Exclui um endereço")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
