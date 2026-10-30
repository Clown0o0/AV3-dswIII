package com.autobots.automanager.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.EmpresaDTO;
import com.autobots.automanager.dto.UsuarioDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.exception.ConflitoException;
import com.autobots.automanager.exception.RecursoNaoEncontradoException;
import com.autobots.automanager.modelo.EmpresaAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;

@Service
@Validated
public class EmpresaService {

    @Autowired
    private EmpresaRepositorio repositorio;

    @Autowired
    private LocalizadorService localizador;

    @Autowired
    private UsuarioService usuarioService;

    private final EmpresaAtualizador atualizador = new EmpresaAtualizador();

    @Transactional
    @Validated(Criacao.class)
    public Empresa criar(@Valid EmpresaDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCadastro(new Date());
        if (dto.getEndereco() != null) {
            empresa.setEndereco(dto.getEndereco().paraEntidade());
        }
        if (dto.getTelefones() != null) {
            dto.getTelefones().forEach(item -> empresa.getTelefones().add(item.paraEntidade()));
        }
        return repositorio.save(empresa);
    }

    public List<Empresa> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Empresa buscar(Long id) {
        return localizador.empresa(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Empresa atualizar(Long id, @Valid EmpresaDTO dto) {
        Empresa empresa = buscar(id);
        atualizador.atualizar(empresa, dto);
        return repositorio.save(empresa);
    }

    @Transactional
    public void deletar(Long id) {
        Empresa empresa = buscar(id);
        if (!empresa.getUsuarios().isEmpty()) {
            throw new ConflitoException("A empresa com o id " + id + " possui " + empresa.getUsuarios().size()
                    + " usuário(s) vinculado(s) e não pode ser excluída");
        }
        if (!empresa.getVendas().isEmpty()) {
            throw new ConflitoException("A empresa com o id " + id + " possui " + empresa.getVendas().size()
                    + " venda(s) registrada(s) e não pode ser excluída");
        }
        repositorio.delete(empresa);
    }

    @Transactional
    public Usuario criarUsuario(Long id, UsuarioDTO dto) {
        buscar(id);
        dto.setEmpresaId(id);
        return usuarioService.criar(dto);
    }

    @Transactional
    public Usuario vincularUsuario(Long id, Long usuarioId) {
        Empresa empresa = buscar(id);
        Usuario usuario = localizador.usuario(usuarioId);
        if (usuario.getEmpresaId() != null && !usuario.getEmpresaId().equals(id)) {
            throw new ConflitoException("O usuário com o id " + usuarioId + " já está vinculado à empresa com o id "
                    + usuario.getEmpresaId());
        }
        empresa.getUsuarios().add(usuario);
        usuario.setEmpresaId(id);
        return usuario;
    }

    @Transactional
    public void desvincularUsuario(Long id, Long usuarioId) {
        Empresa empresa = buscar(id);
        Usuario usuario = localizador.usuario(usuarioId);
        if (!empresa.getUsuarios().remove(usuario)) {
            throw new RecursoNaoEncontradoException("O usuário com o id " + usuarioId + " não está vinculado à empresa com o id " + id);
        }
        usuario.setEmpresaId(null);
    }

    public List<Usuario> usuarios(Long id) {
        return new ArrayList<>(buscar(id).getUsuarios());
    }

    public List<Mercadoria> mercadorias(Long id) {
        return new ArrayList<>(buscar(id).getMercadorias());
    }

    public List<Servico> servicos(Long id) {
        return new ArrayList<>(buscar(id).getServicos());
    }

    public List<Venda> vendas(Long id) {
        return new ArrayList<>(buscar(id).getVendas());
    }
}
