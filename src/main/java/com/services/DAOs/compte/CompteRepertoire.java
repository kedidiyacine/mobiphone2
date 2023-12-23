package com.services.DAOs.compte;

import com.models.Compte;
import com.services.DAOs.CrudRepertoire;

public interface CompteRepertoire extends CrudRepertoire<Compte, Long> {
    Compte trouver_par_login(String login);
}
