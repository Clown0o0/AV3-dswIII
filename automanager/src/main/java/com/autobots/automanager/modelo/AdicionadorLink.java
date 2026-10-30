package com.autobots.automanager.modelo;

import java.util.Collection;

public interface AdicionadorLink<T> {

    default void adicionarLink(Collection<? extends T> lista) {
        lista.forEach(this::adicionarLink);
    }

    void adicionarLink(T objeto);
}
