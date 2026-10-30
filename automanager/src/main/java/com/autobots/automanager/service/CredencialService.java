package com.autobots.automanager.service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.CredencialCodigoBarraDTO;
import com.autobots.automanager.dto.CredencialDTO;
import com.autobots.automanager.dto.CredencialUsuarioSenhaDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.Vinculo;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.exception.RegraNegocioException;
import com.autobots.automanager.modelo.CredencialAtualizador;
import com.autobots.automanager.repositorios.CredencialRepositorio;

@Service
@Validated
public class CredencialService {

    @Autowired
    private CredencialRepositorio repositorio;

    @Autowired
    private LocalizadorService localizador;

    private final CredencialAtualizador atualizador = new CredencialAtualizador();

    @Transactional
    @Validated({ Criacao.class, Vinculo.class })
    public Credencial criar(@Valid CredencialDTO dto) {
        Usuario dono = localizador.usuario(dto.getUsuarioId());
        Credencial credencial = repositorio.saveAndFlush(montar(dto));
        dono.getCredenciais().add(credencial);
        credencial.setUsuarioId(dono.getId());
        return credencial;
    }

    public List<Credencial> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Credencial buscar(Long id) {
        return localizador.credencial(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Credencial atualizar(Long id, @Valid CredencialDTO dto) {
        Credencial credencial = buscar(id);
        boolean mesmoTipo = (credencial instanceof CredencialUsuarioSenha && dto instanceof CredencialUsuarioSenhaDTO)
                || (credencial instanceof CredencialCodigoBarra && dto instanceof CredencialCodigoBarraDTO);
        if (!mesmoTipo) {
            throw new RegraNegocioException("O tipo informado não corresponde ao tipo da credencial com o id " + id);
        }
        atualizador.atualizar(credencial, dto);
        return repositorio.saveAndFlush(credencial);
    }

    @Transactional
    public void deletar(Long id) {
        repositorio.delete(buscar(id));
    }

    public Credencial montar(CredencialDTO dto) {
        Credencial credencial = dto.paraEntidade();
        credencial.setCriacao(new Date());
        credencial.setInativo(Boolean.TRUE.equals(dto.getInativo()));
        return credencial;
    }
}
