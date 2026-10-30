package com.autobots.automanager.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.ServicoDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.exception.ConflitoException;
import com.autobots.automanager.modelo.ServicoAtualizador;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@Service
@Validated
public class ServicoService {

    @Autowired
    private ServicoRepositorio repositorio;

    @Autowired
    private VendaRepositorio vendaRepositorio;

    @Autowired
    private LocalizadorService localizador;

    private final ServicoAtualizador atualizador = new ServicoAtualizador();

    @Transactional
    @Validated(Criacao.class)
    public Servico criar(@Valid ServicoDTO dto) {
        Empresa empresa = localizador.empresa(dto.getEmpresaId());
        Servico servico = new Servico();
        atualizador.atualizar(servico, dto);
        repositorio.save(servico);
        empresa.getServicos().add(servico);
        servico.setEmpresaId(empresa.getId());
        return servico;
    }

    public List<Servico> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Servico buscar(Long id) {
        return localizador.servico(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Servico atualizar(Long id, @Valid ServicoDTO dto) {
        Servico servico = buscar(id);
        atualizador.atualizar(servico, dto);
        return repositorio.save(servico);
    }

    @Transactional
    public void deletar(Long id) {
        Servico servico = buscar(id);
        if (vendaRepositorio.existsByServicosId(id)) {
            throw new ConflitoException("O serviço com o id " + id + " faz parte de uma venda e não pode ser excluído");
        }
        repositorio.delete(servico);
    }
}
