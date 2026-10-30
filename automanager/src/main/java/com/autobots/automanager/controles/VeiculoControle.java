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

import com.autobots.automanager.dto.VeiculoDTO;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelo.AdicionadorLinkVenda;
import com.autobots.automanager.service.VeiculoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/veiculos")
@Tag(name = "Veículos", description = "Rotas dos veículos e do seu histórico de vendas.")
public class VeiculoControle {

    @Autowired
    private VeiculoService service;

    @Autowired
    private AdicionadorLinkVeiculo adicionadorLink;

    @Autowired
    private AdicionadorLinkVenda adicionadorVenda;

    @Operation(summary = "Cadastra um veículo para um proprietário (proprietarioId)")
    @PostMapping
    public ResponseEntity<Veiculo> criar(@RequestBody @Valid VeiculoDTO dto) {
        Veiculo veiculo = service.criar(dto);
        adicionadorLink.adicionarLink(veiculo);
        return ResponseEntity.created(veiculo.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(veiculo);
    }

    @Operation(summary = "Lista os veículos")
    @GetMapping
    public ResponseEntity<CollectionModel<Veiculo>> listar() {
        List<Veiculo> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(VeiculoControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca um veículo pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscar(@PathVariable Long id) {
        Veiculo veiculo = service.buscar(id);
        adicionadorLink.adicionarLink(veiculo);
        return ResponseEntity.ok(veiculo);
    }

    @Operation(summary = "Atualiza um veículo")
    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long id, @RequestBody @Valid VeiculoDTO dto) {
        Veiculo veiculo = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(veiculo);
        return ResponseEntity.ok(veiculo);
    }

    @Operation(summary = "Exclui um veículo (409 se tiver vendas)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista as vendas do veículo")
    @GetMapping("/{id}/vendas")
    public ResponseEntity<CollectionModel<Venda>> vendas(@PathVariable Long id) {
        List<Venda> lista = service.vendas(id);
        adicionadorVenda.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(VeiculoControle.class).vendas(id)).withSelfRel(),
                linkTo(methodOn(VeiculoControle.class).buscar(id)).withRel("veiculo")));
    }
}
