package com.services;

import java.util.List;
import java.util.Map;

public interface DataService<T> {
    List<T> getAllByPage(int page, int count);

    T enregistrer(T entity);

    T modifier(Long id, Map<String, Object> updates);

    void supprimer_par_id(Long id);
}
