package com.services.DAOs;

import java.util.List;
import java.util.Map;

public interface CrudRepertoire<T, ID> {

    T enregistrer(T entity);

    T trouver_par_id(ID id);

    List<T> trouver_tout();

    void supprimer_par_id(ID id);

    List<T> trouver_par_page(int page, int items_count);

    T modifier(ID id, Map<String, Object> updates);

    int count();

    // Additional CRUD methods can be added as needed
}
