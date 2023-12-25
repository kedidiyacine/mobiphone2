package com.controllers;

import com.services.AuthService;

public interface ControllersWithAuth {
    void setAuth(AuthService auth);
}
