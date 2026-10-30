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

import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.modelo.AdicionadorLinkEmail;
import com.autobots.automanager.service.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/emails")
@Tag(name = "E-mails", description = "Rotas dos e-mails dos usuários.")
public class EmailControle {

    @Autowired
    private EmailService service;

    @Autowired
    private AdicionadorLinkEmail adicionadorLink;

    @Operation(summary = "Cadastra um e-mail para um usuário (usuarioId)")
    @PostMapping
    public ResponseEntity<Email> criar(@RequestBody @Valid EmailDTO dto) {
        Email email = service.criar(dto);
        adicionadorLink.adicionarLink(email);
        return ResponseEntity.created(email.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(email);
    }

    @Operation(summary = "Lista os e-mails")
    @GetMapping
    public ResponseEntity<CollectionModel<Email>> listar() {
        List<Email> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(EmailControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca um e-mail pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Email> buscar(@PathVariable Long id) {
        Email email = service.buscar(id);
        adicionadorLink.adicionarLink(email);
        return ResponseEntity.ok(email);
    }

    @Operation(summary = "Atualiza um e-mail")
    @PutMapping("/{id}")
    public ResponseEntity<Email> atualizar(@PathVariable Long id, @RequestBody @Valid EmailDTO dto) {
        Email email = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(email);
        return ResponseEntity.ok(email);
    }

    @Operation(summary = "Exclui um e-mail")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
