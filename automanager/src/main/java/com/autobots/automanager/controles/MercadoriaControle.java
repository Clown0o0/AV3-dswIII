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

import com.autobots.automanager.dto.MercadoriaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelo.AdicionadorLinkMercadoria;
import com.autobots.automanager.service.MercadoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/mercadorias")
@Tag(name = "Mercadorias", description = "Rotas das mercadorias (autopeças) das empresas.")
public class MercadoriaControle {

    @Autowired
    private MercadoriaService service;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorLink;

    @Operation(summary = "Cadastra uma mercadoria na empresa (empresaId), com fornecedor opcional")
    @PostMapping
    public ResponseEntity<Mercadoria> criar(@RequestBody @Valid MercadoriaDTO dto) {
        Mercadoria mercadoria = service.criar(dto);
        adicionadorLink.adicionarLink(mercadoria);
        return ResponseEntity.created(mercadoria.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(mercadoria);
    }

    @Operation(summary = "Lista as mercadorias")
    @GetMapping
    public ResponseEntity<CollectionModel<Mercadoria>> listar() {
        List<Mercadoria> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(MercadoriaControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca uma mercadoria pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Mercadoria> buscar(@PathVariable Long id) {
        Mercadoria mercadoria = service.buscar(id);
        adicionadorLink.adicionarLink(mercadoria);
        return ResponseEntity.ok(mercadoria);
    }

    @Operation(summary = "Atualiza uma mercadoria")
    @PutMapping("/{id}")
    public ResponseEntity<Mercadoria> atualizar(@PathVariable Long id, @RequestBody @Valid MercadoriaDTO dto) {
        Mercadoria mercadoria = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(mercadoria);
        return ResponseEntity.ok(mercadoria);
    }

    @Operation(summary = "Exclui uma mercadoria (409 se estiver em venda)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
