package com.autobots.automanager.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.autobots.automanager.dto.VeiculoDTO;
import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.exception.ConflitoException;
import com.autobots.automanager.modelo.VeiculoAtualizador;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@Service
@Validated
public class VeiculoService {

    @Autowired
    private VeiculoRepositorio repositorio;

    @Autowired
    private LocalizadorService localizador;

    private final VeiculoAtualizador atualizador = new VeiculoAtualizador();

    @Transactional
    @Validated(Criacao.class)
    public Veiculo criar(@Valid VeiculoDTO dto) {
        Usuario proprietario = localizador.usuario(dto.getProprietarioId());
        Veiculo veiculo = new Veiculo();
        atualizador.atualizar(veiculo, dto);
        if (repositorio.existsByPlaca(veiculo.getPlaca())) {
            throw new ConflitoException("Já existe um veículo com a placa " + veiculo.getPlaca());
        }
        veiculo.setProprietario(proprietario);
        repositorio.save(veiculo);
        proprietario.getVeiculos().add(veiculo);
        return veiculo;
    }

    public List<Veiculo> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    public Veiculo buscar(Long id) {
        return localizador.veiculo(id);
    }

    @Transactional
    @Validated(Atualizacao.class)
    public Veiculo atualizar(Long id, @Valid VeiculoDTO dto) {
        Veiculo veiculo = buscar(id);
        atualizador.atualizar(veiculo, dto);
        if (repositorio.existsByPlacaAndIdNot(veiculo.getPlaca(), id)) {
            throw new ConflitoException("Já existe um veículo com a placa " + veiculo.getPlaca());
        }
        if (dto.getProprietarioId() != null && !dto.getProprietarioId().equals(veiculo.getProprietario().getId())) {
            Usuario novo = localizador.usuario(dto.getProprietarioId());
            veiculo.getProprietario().getVeiculos().remove(veiculo);
            veiculo.setProprietario(novo);
            novo.getVeiculos().add(veiculo);
        }
        return repositorio.save(veiculo);
    }

    @Transactional
    public void deletar(Long id) {
        Veiculo veiculo = buscar(id);
        if (!veiculo.getVendas().isEmpty()) {
            throw new ConflitoException("O veículo com o id " + id + " possui " + veiculo.getVendas().size()
                    + " venda(s) registrada(s) e não pode ser excluído");
        }
        veiculo.getProprietario().getVeiculos().remove(veiculo);
        repositorio.delete(veiculo);
    }

    public List<Venda> vendas(Long id) {
        return new ArrayList<>(buscar(id).getVendas());
    }
}
