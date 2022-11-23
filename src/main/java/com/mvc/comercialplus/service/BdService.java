package com.mvc.comercialplus.service;

import java.util.List;

public interface BdService<T> {

    public List<T> getAll();

    public T getById(Long id);

    public T save(T objeto);

    public void delete(Long id);
    
}
