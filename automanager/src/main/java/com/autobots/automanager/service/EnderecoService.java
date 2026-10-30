package com.autobots.automanager.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.Vinculo;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.exception.ConflitoException;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@Service
@Validated
public class EnderecoService {

    @Autowired
    private EnderecoRepositorio repositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Autowired
    private LocalizadorService localizador;

    private final EnderecoAtualizador atualizador = new EnderecoAtualizador();

    @Transactional
    @Validated({ Criacao.class, Vinculo.class })
    public Endereco criar(@Valid EnderecoDTO dto) {
        Endereco endereco = dto.paraEntidade();
        if (dto.getUsuarioId() != null) {
            Usuario dono = localizador.usuario(dto.getUsuarioId());
            if (dono.getEndereco() != null) {
                throw new ConflitoException("O usuário com o id " + dono.getId() + " já possui endereço");
            }
            dono.setEndereco(repositorio.save(endereco));
        } else {
            Empresa dono = localizador.empresa(dto.getEmpresaId());
            if (dono.getEndereco() != null) {
                throw new ConflitoException("A empresa com o id " + dono.getId() + " já possui endereço");
            }
            dono.setEndereco(repositorio.save(endereco));
        }
        return endereco;
    }

    public List<Endereco> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Endereco buscar(Long id) {
        return localizador.endereco(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Endereco atualizar(Long id, @Valid EnderecoDTO dto) {
        Endereco endereco = buscar(id);
        atualizador.atualizar(endereco, dto);
        return repositorio.save(endereco);
    }

    @Transactional
    public void deletar(Long id) {
        Endereco endereco = buscar(id);
        Optional<Usuario> usuario = usuarioRepositorio.findByEnderecoId(id);
        Optional<Empresa> empresa = empresaRepositorio.findByEnderecoId(id);
        if (usuario.isPresent()) {
            usuario.get().setEndereco(null);
        } else if (empresa.isPresent()) {
            empresa.get().setEndereco(null);
        } else {
            repositorio.delete(endereco);
        }
    }
}
