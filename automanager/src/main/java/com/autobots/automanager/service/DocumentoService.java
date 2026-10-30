package com.autobots.automanager.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.DocumentoValidador;
import com.autobots.automanager.dto.validacao.Vinculo;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.exception.ConflitoException;
import com.autobots.automanager.exception.RegraNegocioException;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@Service
@Validated
public class DocumentoService {

    @Autowired
    private DocumentoRepositorio repositorio;

    @Autowired
    private LocalizadorService localizador;

    private final DocumentoAtualizador atualizador = new DocumentoAtualizador();

    @Transactional
    @Validated({ Criacao.class, Vinculo.class })
    public Documento criar(@Valid DocumentoDTO dto) {
        Usuario dono = localizador.usuario(dto.getUsuarioId());
        verificarNumeroLivre(dto.getNumero());
        Documento documento = repositorio.save(dto.paraEntidade());
        dono.getDocumentos().add(documento);
        return documento;
    }

    public List<Documento> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Documento buscar(Long id) {
        return localizador.documento(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Documento atualizar(Long id, @Valid DocumentoDTO dto) {
        Documento documento = buscar(id);
        atualizador.atualizar(documento, dto);
        if (!DocumentoValidador.valido(documento.getTipo(), documento.getNumero())) {
            throw new RegraNegocioException("Número inválido para o tipo " + documento.getTipo());
        }
        if (repositorio.existsByNumeroAndIdNot(documento.getNumero(), id)) {
            throw new ConflitoException("Já existe um documento com o número " + documento.getNumero());
        }
        return repositorio.save(documento);
    }

    @Transactional
    public void deletar(Long id) {
        repositorio.delete(buscar(id));
    }

    public void verificarNumeroLivre(String numero) {
        if (repositorio.existsByNumero(numero)) {
            throw new ConflitoException("Já existe um documento com o número " + numero);
        }
    }
}
