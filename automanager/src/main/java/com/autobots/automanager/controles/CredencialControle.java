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

import com.autobots.automanager.dto.CredencialDTO;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.modelo.AdicionadorLinkCredencial;
import com.autobots.automanager.service.CredencialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/credenciais")
@Tag(name = "Credenciais", description = "Rotas das credenciais (usuarioSenha ou codigoBarra) dos usuários.")
public class CredencialControle {

    @Autowired
    private CredencialService service;

    @Autowired
    private AdicionadorLinkCredencial adicionadorLink;

    @Operation(summary = "Cadastra uma credencial para um usuário (usuarioId); o campo tipo define a subclasse")
    @PostMapping
    public ResponseEntity<Credencial> criar(@RequestBody @Valid CredencialDTO dto) {
        Credencial credencial = service.criar(dto);
        adicionadorLink.adicionarLink(credencial);
        return ResponseEntity.created(credencial.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(credencial);
    }

    @Operation(summary = "Lista as credenciais")
    @GetMapping
    public ResponseEntity<CollectionModel<Credencial>> listar() {
        List<Credencial> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(CredencialControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca uma credencial pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Credencial> buscar(@PathVariable Long id) {
        Credencial credencial = service.buscar(id);
        adicionadorLink.adicionarLink(credencial);
        return ResponseEntity.ok(credencial);
    }

    @Operation(summary = "Atualiza ou inativa (inativo=true) uma credencial")
    @PutMapping("/{id}")
    public ResponseEntity<Credencial> atualizar(@PathVariable Long id, @RequestBody @Valid CredencialDTO dto) {
        Credencial credencial = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(credencial);
        return ResponseEntity.ok(credencial);
    }

    @Operation(summary = "Exclui uma credencial")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
