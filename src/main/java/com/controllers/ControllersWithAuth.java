package com.controllers;

import com.services.authentification.AuthService;

public interface ControllersWithAuth {
    void setAuth(AuthService auth);
}
