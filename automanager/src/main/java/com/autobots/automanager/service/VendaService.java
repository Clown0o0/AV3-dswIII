package com.autobots.automanager.service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.VendaDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.exception.ConflitoException;
import com.autobots.automanager.exception.RegraNegocioException;
import com.autobots.automanager.repositorios.VendaRepositorio;

@Service
@Validated
public class VendaService {

    @Autowired
    private VendaRepositorio repositorio;

    @Autowired
    private LocalizadorService localizador;

    @Transactional
    @Validated(Criacao.class)
    public Venda criar(@Valid VendaDTO dto) {
        if (repositorio.existsByIdentificacao(dto.getIdentificacao())) {
            throw new ConflitoException("Já existe uma venda com a identificação " + dto.getIdentificacao());
        }
        Empresa empresa = localizador.empresa(dto.getEmpresaId());
        Venda venda = new Venda();
        venda.setIdentificacao(dto.getIdentificacao());
        venda.setCadastro(new Date());
        aplicar(venda, dto);
        validar(venda, empresa.getId());
        repositorio.save(venda);
        empresa.getVendas().add(venda);
        venda.setEmpresaId(empresa.getId());
        venda.getCliente().getVendas().add(venda);
        venda.getFuncionario().getVendas().add(venda);
        return venda;
    }

    public List<Venda> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Venda buscar(Long id) {
        return localizador.venda(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Venda atualizar(Long id, @Valid VendaDTO dto) {
        Venda venda = buscar(id);
        if (dto.getIdentificacao() != null && !dto.getIdentificacao().isBlank()) {
            if (repositorio.existsByIdentificacaoAndIdNot(dto.getIdentificacao(), id)) {
                throw new ConflitoException("Já existe uma venda com a identificação " + dto.getIdentificacao());
            }
            venda.setIdentificacao(dto.getIdentificacao());
        }
        Usuario clienteAnterior = venda.getCliente();
        Usuario funcionarioAnterior = venda.getFuncionario();
        aplicar(venda, dto);
        validar(venda, venda.getEmpresaId());
        clienteAnterior.getVendas().remove(venda);
        funcionarioAnterior.getVendas().remove(venda);
        venda.getCliente().getVendas().add(venda);
        venda.getFuncionario().getVendas().add(venda);
        return repositorio.save(venda);
    }

    @Transactional
    public void deletar(Long id) {
        Venda venda = buscar(id);
        venda.getCliente().getVendas().remove(venda);
        venda.getFuncionario().getVendas().remove(venda);
        repositorio.delete(venda);
    }

    private void aplicar(Venda venda, VendaDTO dto) {
        if (dto.getClienteId() != null) {
            venda.setCliente(localizador.usuario(dto.getClienteId()));
        }
        if (dto.getFuncionarioId() != null) {
            venda.setFuncionario(localizador.usuario(dto.getFuncionarioId()));
        }
        if (dto.getVeiculoId() != null) {
            venda.setVeiculo(localizador.veiculo(dto.getVeiculoId()));
        }
        if (dto.getMercadorias() != null) {
            venda.getMercadorias().clear();
            dto.getMercadorias().forEach(id -> venda.getMercadorias().add(localizador.mercadoria(id)));
        }
        if (dto.getServicos() != null) {
            venda.getServicos().clear();
            dto.getServicos().forEach(id -> venda.getServicos().add(localizador.servico(id)));
        }
    }

    private void validar(Venda venda, Long empresaId) {
        Usuario cliente = venda.getCliente();
        Usuario funcionario = venda.getFuncionario();
        if (!cliente.getPerfis().contains(PerfilUsuario.CLIENTE)) {
            throw new RegraNegocioException("O usuário com o id " + cliente.getId() + " não possui o perfil CLIENTE");
        }
        if (!funcionario.getPerfis().contains(PerfilUsuario.FUNCIONARIO)) {
            throw new RegraNegocioException("O usuário com o id " + funcionario.getId() + " não possui o perfil FUNCIONARIO");
        }
        if (venda.getVeiculo() != null && !venda.getVeiculo().getProprietario().getId().equals(cliente.getId())) {
            throw new RegraNegocioException("O veículo com o id " + venda.getVeiculo().getId()
                    + " não pertence ao cliente com o id " + cliente.getId());
        }
        if (venda.getMercadorias().isEmpty() && venda.getServicos().isEmpty()) {
            throw new RegraNegocioException("A venda deve ter ao menos uma mercadoria ou um serviço");
        }
        for (Mercadoria mercadoria : venda.getMercadorias()) {
            if (!empresaId.equals(mercadoria.getEmpresaId())) {
                throw new RegraNegocioException("A mercadoria com o id " + mercadoria.getId() + " não pertence à empresa da venda");
            }
        }
        for (Servico servico : venda.getServicos()) {
            if (!empresaId.equals(servico.getEmpresaId())) {
                throw new RegraNegocioException("O serviço com o id " + servico.getId() + " não pertence à empresa da venda");
            }
        }
    }
}
