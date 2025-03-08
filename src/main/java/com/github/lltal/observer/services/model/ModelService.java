package com.github.lltal.observer.services.model;

import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public interface ModelService<T, V> {

    void create(T dto);

    T find(V key);

    Collection<T> findAll();

    void delete(V key);

    @Autowired
    default void regService(ModelServices modelServices) {
        modelServices.putService(getActionObjectType(), this);
    }

    AdminActionObjectType getActionObjectType();
}
