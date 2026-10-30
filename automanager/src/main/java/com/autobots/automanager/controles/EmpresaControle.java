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

import com.autobots.automanager.dto.EmpresaDTO;
import com.autobots.automanager.dto.UsuarioDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelo.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelo.AdicionadorLinkServico;
import com.autobots.automanager.modelo.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.AdicionadorLinkVenda;
import com.autobots.automanager.service.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas", description = "Rotas das empresas e dos seus usuários, mercadorias, serviços e vendas.")
public class EmpresaControle {

    @Autowired
    private EmpresaService service;

    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;

    @Autowired
    private AdicionadorLinkUsuario adicionadorUsuario;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorMercadoria;

    @Autowired
    private AdicionadorLinkServico adicionadorServico;

    @Autowired
    private AdicionadorLinkVenda adicionadorVenda;

    @Operation(summary = "Cadastra uma empresa")
    @PostMapping
    public ResponseEntity<Empresa> criar(@RequestBody @Valid EmpresaDTO dto) {
        Empresa empresa = service.criar(dto);
        adicionadorLink.adicionarLink(empresa);
        return ResponseEntity.created(empresa.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(empresa);
    }

    @Operation(summary = "Lista as empresas")
    @GetMapping
    public ResponseEntity<CollectionModel<Empresa>> listar() {
        List<Empresa> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(EmpresaControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca uma empresa pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscar(@PathVariable Long id) {
        Empresa empresa = service.buscar(id);
        adicionadorLink.adicionarLink(empresa);
        return ResponseEntity.ok(empresa);
    }

    @Operation(summary = "Atualiza uma empresa")
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @RequestBody @Valid EmpresaDTO dto) {
        Empresa empresa = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(empresa);
        return ResponseEntity.ok(empresa);
    }

    @Operation(summary = "Exclui uma empresa (409 se tiver usuários ou vendas)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cadastra um usuário já vinculado à empresa")
    @PostMapping("/{id}/usuarios")
    public ResponseEntity<Usuario> criarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioDTO dto) {
        Usuario usuario = service.criarUsuario(id, dto);
        adicionadorUsuario.adicionarLink(usuario);
        return ResponseEntity.created(usuario.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(usuario);
    }

    @Operation(summary = "Lista os usuários da empresa")
    @GetMapping("/{id}/usuarios")
    public ResponseEntity<CollectionModel<Usuario>> usuarios(@PathVariable Long id) {
        List<Usuario> lista = service.usuarios(id);
        adicionadorUsuario.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(EmpresaControle.class).usuarios(id)).withSelfRel(),
                linkTo(methodOn(EmpresaControle.class).buscar(id)).withRel("empresa")));
    }

    @Operation(summary = "Vincula um usuário existente à empresa")
    @PutMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Usuario> vincularUsuario(@PathVariable Long id, @PathVariable Long usuarioId) {
        Usuario usuario = service.vincularUsuario(id, usuarioId);
        adicionadorUsuario.adicionarLink(usuario);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Desvincula um usuário da empresa")
    @DeleteMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Void> desvincularUsuario(@PathVariable Long id, @PathVariable Long usuarioId) {
        service.desvincularUsuario(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista as mercadorias da empresa")
    @GetMapping("/{id}/mercadorias")
    public ResponseEntity<CollectionModel<Mercadoria>> mercadorias(@PathVariable Long id) {
        List<Mercadoria> lista = service.mercadorias(id);
        adicionadorMercadoria.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(EmpresaControle.class).mercadorias(id)).withSelfRel(),
                linkTo(methodOn(EmpresaControle.class).buscar(id)).withRel("empresa")));
    }

    @Operation(summary = "Lista os serviços da empresa")
    @GetMapping("/{id}/servicos")
    public ResponseEntity<CollectionModel<Servico>> servicos(@PathVariable Long id) {
        List<Servico> lista = service.servicos(id);
        adicionadorServico.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(EmpresaControle.class).servicos(id)).withSelfRel(),
                linkTo(methodOn(EmpresaControle.class).buscar(id)).withRel("empresa")));
    }

    @Operation(summary = "Lista as vendas da empresa")
    @GetMapping("/{id}/vendas")
    public ResponseEntity<CollectionModel<Venda>> vendas(@PathVariable Long id) {
        List<Venda> lista = service.vendas(id);
        adicionadorVenda.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(EmpresaControle.class).vendas(id)).withSelfRel(),
                linkTo(methodOn(EmpresaControle.class).buscar(id)).withRel("empresa")));
    }
}
