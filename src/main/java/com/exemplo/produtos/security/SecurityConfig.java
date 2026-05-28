package com.exemplo.produtos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança da aplicação.
 *
 * @Configuration -> marca esta classe como fonte de configuração Spring
 * @EnableWebSecurity -> ativa o Spring Security na aplicação
 *
 * Esta classe substitui o antigo WebSecurityConfigurerAdapter (removido
 * no Spring Boot 3) e usa o modelo de configuração com @Bean.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura as regras de acesso HTTP e o formulário de login.
     *
     * SecurityFilterChain é a cadeia de filtros que o Spring Security
     * aplica em cada requisição HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Regras de autorização de URLs
            .authorizeHttpRequests(auth -> auth
                // Permite acesso público à página de login e recursos estáticos
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                // Permite acesso ao console H2 (apenas para desenvolvimento)
                .requestMatchers("/h2-console/**").permitAll()
                // Todas as outras URLs exigem autenticação
                .anyRequest().authenticated()
            )

            // Configuração do formulário de login
            .formLogin(form -> form
                .loginPage("/login")                  // URL da página de login customizada
                .defaultSuccessUrl("/", true)         // Redireciona para "/" após login com sucesso
                .failureUrl("/login?erro=1")          // Redireciona para login com erro
                .permitAll()                          // Qualquer um pode acessar a página de login
            )

            // Configuração do logout
            .logout(logout -> logout
                .logoutSuccessUrl("/login")           // Após logout, vai para a tela de login
                .permitAll()
            )

            // Desabilita proteção CSRF para simplificar as chamadas AJAX
            // Em produção real, use tokens CSRF. Para fins acadêmicos, desabilitamos.
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/h2-console/**")
            )

            // Permite que o console H2 seja carregado em iframes (usa frames HTML)
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    /**
     * Define os usuários da aplicação em memória.
     *
     * Para fins acadêmicos, usamos um usuário fixo sem banco de dados.
     * Em produção real, os usuários viriam de um banco de dados.
     *
     * Usuário: admin | Senha: 123
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            .password("123")           // {noop} seria necessário, mas o NoOpPasswordEncoder abaixo resolve
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(admin);
    }

    /**
     * Encoder de senha: NoOpPasswordEncoder não aplica nenhuma criptografia.
     *
     * ATENÇÃO: Use apenas para fins didáticos!
     * Em produção, use BCryptPasswordEncoder.
     */
    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
