package com.exemplo.produtos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsável por servir a página de login.
 *
 * Spring Security redireciona usuários não autenticados para /login,
 * mas ele mesmo não renderiza a página — quem faz isso é este controller.
 * Sem este mapeamento, o Spring fica em loop infinito de redirecionamento.
 */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // resolve para templates/login.html
    }
}
