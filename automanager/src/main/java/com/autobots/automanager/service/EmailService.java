package com.autobots.automanager.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.Vinculo;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.EmailAtualizador;
import com.autobots.automanager.repositorios.EmailRepositorio;

@Service
@Validated
public class EmailService {

    @Autowired
    private EmailRepositorio repositorio;

    @Autowired
    private LocalizadorService localizador;

    private final EmailAtualizador atualizador = new EmailAtualizador();

    @Transactional
    @Validated({ Criacao.class, Vinculo.class })
    public Email criar(@Valid EmailDTO dto) {
        Usuario dono = localizador.usuario(dto.getUsuarioId());
        Email email = repositorio.save(dto.paraEntidade());
        dono.getEmails().add(email);
        return email;
    }

    public List<Email> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Email buscar(Long id) {
        return localizador.email(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Email atualizar(Long id, @Valid EmailDTO dto) {
        Email email = buscar(id);
        atualizador.atualizar(email, dto);
        return repositorio.save(email);
    }

    @Transactional
    public void deletar(Long id) {
        repositorio.delete(buscar(id));
    }
}
