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

import com.autobots.automanager.dto.VendaDTO;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.AdicionadorLinkVenda;
import com.autobots.automanager.service.VendaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/vendas")
@Tag(name = "Vendas", description = "Rotas das vendas: cliente, funcionário, veículo, mercadorias e serviços informados por id.")
public class VendaControle {

    @Autowired
    private VendaService service;

    @Autowired
    private AdicionadorLinkVenda adicionadorLink;

    @Operation(summary = "Registra uma venda validando perfis, dono do veículo e identificação única")
    @PostMapping
    public ResponseEntity<Venda> criar(@RequestBody @Valid VendaDTO dto) {
        Venda venda = service.criar(dto);
        adicionadorLink.adicionarLink(venda);
        return ResponseEntity.created(venda.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(venda);
    }

    @Operation(summary = "Lista as vendas")
    @GetMapping
    public ResponseEntity<CollectionModel<Venda>> listar() {
        List<Venda> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(VendaControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca uma venda pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscar(@PathVariable Long id) {
        Venda venda = service.buscar(id);
        adicionadorLink.adicionarLink(venda);
        return ResponseEntity.ok(venda);
    }

    @Operation(summary = "Atualiza uma venda")
    @PutMapping("/{id}")
    public ResponseEntity<Venda> atualizar(@PathVariable Long id, @RequestBody @Valid VendaDTO dto) {
        Venda venda = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(venda);
        return ResponseEntity.ok(venda);
    }

    @Operation(summary = "Exclui uma venda")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
