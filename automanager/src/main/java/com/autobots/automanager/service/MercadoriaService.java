package com.autobots.automanager.service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.MercadoriaDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.exception.ConflitoException;
import com.autobots.automanager.exception.RegraNegocioException;
import com.autobots.automanager.modelo.MercadoriaAtualizador;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@Service
@Validated
public class MercadoriaService {

    @Autowired
    private MercadoriaRepositorio repositorio;

    @Autowired
    private VendaRepositorio vendaRepositorio;

    @Autowired
    private LocalizadorService localizador;

    private final MercadoriaAtualizador atualizador = new MercadoriaAtualizador();

    @Transactional
    @Validated(Criacao.class)
    public Mercadoria criar(@Valid MercadoriaDTO dto) {
        Empresa empresa = localizador.empresa(dto.getEmpresaId());
        Usuario fornecedor = dto.getFornecedorId() == null ? null : fornecedor(dto.getFornecedorId());
        Mercadoria mercadoria = new Mercadoria();
        atualizador.atualizar(mercadoria, dto);
        mercadoria.setCadastro(new Date());
        validarDatas(mercadoria);
        repositorio.save(mercadoria);
        empresa.getMercadorias().add(mercadoria);
        mercadoria.setEmpresaId(empresa.getId());
        if (fornecedor != null) {
            fornecedor.getMercadorias().add(mercadoria);
            mercadoria.setFornecedorId(fornecedor.getId());
        }
        return mercadoria;
    }

    public List<Mercadoria> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Mercadoria buscar(Long id) {
        return localizador.mercadoria(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Mercadoria atualizar(Long id, @Valid MercadoriaDTO dto) {
        Mercadoria mercadoria = buscar(id);
        atualizador.atualizar(mercadoria, dto);
        validarDatas(mercadoria);
        if (dto.getFornecedorId() != null && !dto.getFornecedorId().equals(mercadoria.getFornecedorId())) {
            Usuario novo = fornecedor(dto.getFornecedorId());
            if (mercadoria.getFornecedorId() != null) {
                localizador.usuario(mercadoria.getFornecedorId()).getMercadorias().remove(mercadoria);
            }
            novo.getMercadorias().add(mercadoria);
            mercadoria.setFornecedorId(novo.getId());
        }
        return repositorio.save(mercadoria);
    }

    @Transactional
    public void deletar(Long id) {
        Mercadoria mercadoria = buscar(id);
        if (vendaRepositorio.existsByMercadoriasId(id)) {
            throw new ConflitoException("A mercadoria com o id " + id + " faz parte de uma venda e não pode ser excluída");
        }
        repositorio.delete(mercadoria);
    }

    private Usuario fornecedor(Long id) {
        Usuario fornecedor = localizador.usuario(id);
        if (!fornecedor.getPerfis().contains(PerfilUsuario.FORNECEDOR)) {
            throw new RegraNegocioException("O usuário com o id " + id + " não possui o perfil FORNECEDOR");
        }
        return fornecedor;
    }

    private void validarDatas(Mercadoria mercadoria) {
        if (mercadoria.getValidade().before(mercadoria.getFabricao())) {
            throw new RegraNegocioException("A validade não pode ser anterior à data de fabricação");
        }
    }
}
