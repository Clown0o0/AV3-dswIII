package com.autobots.automanager.entidades;

import org.springframework.hateoas.RepresentationModel;

public abstract class Entidade<T extends Entidade<T>> extends RepresentationModel<T> {

    @Override
    public final boolean equals(Object outro) {
        return this == outro;
    }

    @Override
    public final int hashCode() {
        return System.identityHashCode(this);
    }
}
