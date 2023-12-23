package com.services.DAOs.session;

import com.models.Session;
import com.services.DAOs.CrudRepertoire;

public interface SessionRepertoire extends CrudRepertoire<Session, Long> {
    // methods to add later on*
    Session trouver_par_compte_id(Long id);
}
