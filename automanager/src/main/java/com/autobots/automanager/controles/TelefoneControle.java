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

import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.AdicionadorLinkTelefone;
import com.autobots.automanager.service.TelefoneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/telefones")
@Tag(name = "Telefones", description = "Rotas dos telefones de usuários e empresas.")
public class TelefoneControle {

    @Autowired
    private TelefoneService service;

    @Autowired
    private AdicionadorLinkTelefone adicionadorLink;

    @Operation(summary = "Cadastra um telefone para um usuário (usuarioId) ou empresa (empresaId)")
    @PostMapping
    public ResponseEntity<Telefone> criar(@RequestBody @Valid TelefoneDTO dto) {
        Telefone telefone = service.criar(dto);
        adicionadorLink.adicionarLink(telefone);
        return ResponseEntity.created(telefone.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(telefone);
    }

    @Operation(summary = "Lista os telefones")
    @GetMapping
    public ResponseEntity<CollectionModel<Telefone>> listar() {
        List<Telefone> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(TelefoneControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca um telefone pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Telefone> buscar(@PathVariable Long id) {
        Telefone telefone = service.buscar(id);
        adicionadorLink.adicionarLink(telefone);
        return ResponseEntity.ok(telefone);
    }

    @Operation(summary = "Atualiza um telefone")
    @PutMapping("/{id}")
    public ResponseEntity<Telefone> atualizar(@PathVariable Long id, @RequestBody @Valid TelefoneDTO dto) {
        Telefone telefone = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(telefone);
        return ResponseEntity.ok(telefone);
    }

    @Operation(summary = "Exclui um telefone")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
