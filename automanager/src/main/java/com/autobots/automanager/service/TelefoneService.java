package com.autobots.automanager.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.Vinculo;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@Service
@Validated
public class TelefoneService {

    @Autowired
    private TelefoneRepositorio repositorio;

    @Autowired
    private LocalizadorService localizador;

    private final TelefoneAtualizador atualizador = new TelefoneAtualizador();

    @Transactional
    @Validated({ Criacao.class, Vinculo.class })
    public Telefone criar(@Valid TelefoneDTO dto) {
        Telefone telefone = dto.paraEntidade();
        if (dto.getUsuarioId() != null) {
            Usuario dono = localizador.usuario(dto.getUsuarioId());
            repositorio.save(telefone);
            dono.getTelefones().add(telefone);
        } else {
            Empresa dono = localizador.empresa(dto.getEmpresaId());
            repositorio.save(telefone);
            dono.getTelefones().add(telefone);
        }
        return telefone;
    }

    public List<Telefone> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Telefone buscar(Long id) {
        return localizador.telefone(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Telefone atualizar(Long id, @Valid TelefoneDTO dto) {
        Telefone telefone = buscar(id);
        atualizador.atualizar(telefone, dto);
        return repositorio.save(telefone);
    }

    @Transactional
    public void deletar(Long id) {
        repositorio.delete(buscar(id));
    }
}
