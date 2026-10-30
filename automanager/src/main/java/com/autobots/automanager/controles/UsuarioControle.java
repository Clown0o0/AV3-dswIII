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

import com.autobots.automanager.dto.UsuarioDTO;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.AdicionadorLinkCredencial;
import com.autobots.automanager.modelo.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelo.AdicionadorLinkVenda;
import com.autobots.automanager.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Rotas dos usuários (clientes, funcionários e fornecedores).")
public class UsuarioControle {

    @Autowired
    private UsuarioService service;

    @Autowired
    private AdicionadorLinkUsuario adicionadorLink;

    @Autowired
    private AdicionadorLinkVeiculo adicionadorVeiculo;

    @Autowired
    private AdicionadorLinkVenda adicionadorVenda;

    @Autowired
    private AdicionadorLinkCredencial adicionadorCredencial;

    @Operation(summary = "Cadastra um usuário com seus perfis, documentos, contatos e credenciais")
    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody @Valid UsuarioDTO dto) {
        Usuario usuario = service.criar(dto);
        adicionadorLink.adicionarLink(usuario);
        return ResponseEntity.created(usuario.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(usuario);
    }

    @Operation(summary = "Lista os usuários")
    @GetMapping
    public ResponseEntity<CollectionModel<Usuario>> listar() {
        List<Usuario> lista = service.listar();
        adicionadorLink.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(UsuarioControle.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Busca um usuário pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
        Usuario usuario = service.buscar(id);
        adicionadorLink.adicionarLink(usuario);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Atualiza nome, nome social, perfis e o endereço existente do usuário")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioDTO dto) {
        Usuario usuario = service.atualizar(id, dto);
        adicionadorLink.adicionarLink(usuario);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Exclui um usuário (409 se tiver vendas, veículos ou mercadorias fornecidas)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista os veículos do usuário")
    @GetMapping("/{id}/veiculos")
    public ResponseEntity<CollectionModel<Veiculo>> veiculos(@PathVariable Long id) {
        List<Veiculo> lista = service.veiculos(id);
        adicionadorVeiculo.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(UsuarioControle.class).veiculos(id)).withSelfRel(),
                linkTo(methodOn(UsuarioControle.class).buscar(id)).withRel("usuario")));
    }

    @Operation(summary = "Lista as vendas do usuário (como cliente ou funcionário)")
    @GetMapping("/{id}/vendas")
    public ResponseEntity<CollectionModel<Venda>> vendas(@PathVariable Long id) {
        List<Venda> lista = service.vendas(id);
        adicionadorVenda.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(UsuarioControle.class).vendas(id)).withSelfRel(),
                linkTo(methodOn(UsuarioControle.class).buscar(id)).withRel("usuario")));
    }

    @Operation(summary = "Lista as credenciais do usuário")
    @GetMapping("/{id}/credenciais")
    public ResponseEntity<CollectionModel<Credencial>> credenciais(@PathVariable Long id) {
        List<Credencial> lista = service.credenciais(id);
        adicionadorCredencial.adicionarLink(lista);
        return ResponseEntity.ok(CollectionModel.of(lista, linkTo(methodOn(UsuarioControle.class).credenciais(id)).withSelfRel(),
                linkTo(methodOn(UsuarioControle.class).buscar(id)).withRel("usuario")));
    }
}
