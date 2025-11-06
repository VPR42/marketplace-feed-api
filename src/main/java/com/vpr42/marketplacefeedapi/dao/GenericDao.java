package com.vpr42.marketplacefeedapi.dao;

import java.util.Optional;

// Для пример / удобства, здесь вообще по-хорошему хранить интерфейсы, а в папке impl рядом реализацию
// как в папке service, но вообще я сюда это по-приколу воткнул, чтобы git увидел папку

public interface GenericDao<T, TKey> {
    void save(T entity);
    boolean update(T entity);
    Optional<T> getById(TKey id);
    boolean delete(T entity);
}
