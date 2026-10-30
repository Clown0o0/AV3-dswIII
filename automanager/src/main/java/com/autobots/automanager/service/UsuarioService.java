package com.autobots.automanager.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.dto.UsuarioDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.exception.ConflitoException;
import com.autobots.automanager.modelo.UsuarioAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@Service
@Validated
public class UsuarioService {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private LocalizadorService localizador;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private CredencialService credencialService;

    private final UsuarioAtualizador atualizador = new UsuarioAtualizador();

    @Transactional
    @Validated(Criacao.class)
    public Usuario criar(@Valid UsuarioDTO dto) {
        Empresa empresa = dto.getEmpresaId() == null ? null : localizador.empresa(dto.getEmpresaId());
        Usuario usuario = repositorio.save(montar(dto));
        usuario.getCredenciais().forEach(credencial -> credencial.setUsuarioId(usuario.getId()));
        if (empresa != null) {
            empresa.getUsuarios().add(usuario);
            usuario.setEmpresaId(empresa.getId());
        }
        return usuario;
    }

    public List<Usuario> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Usuario buscar(Long id) {
        return localizador.usuario(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Usuario atualizar(Long id, @Valid UsuarioDTO dto) {
        Usuario usuario = buscar(id);
        atualizador.atualizar(usuario, dto);
        return repositorio.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscar(id);
        if (!usuario.getVendas().isEmpty()) {
            throw new ConflitoException("O usuário com o id " + id + " possui " + usuario.getVendas().size()
                    + " venda(s) registrada(s) e não pode ser excluído");
        }
        if (!usuario.getVeiculos().isEmpty()) {
            throw new ConflitoException("O usuário com o id " + id + " possui " + usuario.getVeiculos().size()
                    + " veículo(s) cadastrado(s) e não pode ser excluído");
        }
        if (!usuario.getMercadorias().isEmpty()) {
            throw new ConflitoException("O usuário com o id " + id + " é fornecedor de " + usuario.getMercadorias().size()
                    + " mercadoria(s) e não pode ser excluído");
        }
        repositorio.delete(usuario);
    }

    public List<Veiculo> veiculos(Long id) {
        return new ArrayList<>(buscar(id).getVeiculos());
    }

    public List<Venda> vendas(Long id) {
        return new ArrayList<>(buscar(id).getVendas());
    }

    public List<Credencial> credenciais(Long id) {
        return new ArrayList<>(buscar(id).getCredenciais());
    }

    private Usuario montar(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setNomeSocial(dto.getNomeSocial());
        usuario.getPerfis().addAll(dto.getPerfis());
        if (dto.getEndereco() != null) {
            usuario.setEndereco(dto.getEndereco().paraEntidade());
        }
        if (dto.getTelefones() != null) {
            dto.getTelefones().forEach(item -> usuario.getTelefones().add(item.paraEntidade()));
        }
        if (dto.getDocumentos() != null) {
            Set<String> numeros = new HashSet<>();
            for (DocumentoDTO item : dto.getDocumentos()) {
                if (!numeros.add(item.getNumero())) {
                    throw new ConflitoException("O número de documento " + item.getNumero() + " foi informado mais de uma vez");
                }
                documentoService.verificarNumeroLivre(item.getNumero());
                usuario.getDocumentos().add(item.paraEntidade());
            }
        }
        if (dto.getEmails() != null) {
            dto.getEmails().forEach(item -> usuario.getEmails().add(item.paraEntidade()));
        }
        if (dto.getCredenciais() != null) {
            dto.getCredenciais().forEach(item -> usuario.getCredenciais().add(credencialService.montar(item)));
        }
        return usuario;
    }
}
