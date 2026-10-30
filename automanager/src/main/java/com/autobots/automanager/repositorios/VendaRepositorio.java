package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Venda;

public interface VendaRepositorio extends JpaRepository<Venda, Long> {

    boolean existsByIdentificacao(String identificacao);

    boolean existsByIdentificacaoAndIdNot(String identificacao, Long id);

    boolean existsByMercadoriasId(Long mercadoriaId);

    boolean existsByServicosId(Long servicoId);
}
