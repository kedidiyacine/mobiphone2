package com.services.DAOs.client;

import com.models.Client;
import com.services.DAOs.CrudRepertoire;

public interface ClientRepertoire extends CrudRepertoire<Client, Long> {
    Client trouver_par_cin(String cin);
}
