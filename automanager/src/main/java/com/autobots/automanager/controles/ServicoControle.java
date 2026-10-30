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

import com.autobots.automanager.dto.ServicoDTO;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelo.AdicionadorLinkServico;
import com.autobots.automanager.service.ServicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/servicos")
@Tag(name = "Serviços", description = "Rotas dos serviços prestados pelas empresas.")
public class ServicoControle {

    @Autowired
    private ServicoService service;

    @Autowired
    private AdicionadorLinkServico adicionadorLink;

    @Operation(summary = "Cadastra um serviço na empresa (empresaId)")
    @PostMapping
    public ResponseEntity<Servico> criar(@RequestBody @Valid ServicoDTO dto) {
        Servico servico = service.criar(dto);
        adicionadorLink.adicionarLink(servico);
        return ResponseEntity.created(servico.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(servico);
    }

    @Operation(summary = "Lista os serviços")
    @GetMapping
    public ResponseEntity<CollectionModel<Servico>> listar() {
        List<Servico> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(ServicoControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca um serviço pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscar(@PathVariable Long id) {
        Servico servico = service.buscar(id);
        adicionadorLink.adicionarLink(servico);
        return ResponseEntity.ok(servico);
    }

    @Operation(summary = "Atualiza um serviço")
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id, @RequestBody @Valid ServicoDTO dto) {
        Servico servico = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(servico);
        return ResponseEntity.ok(servico);
    }

    @Operation(summary = "Exclui um serviço (409 se estiver em venda)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
