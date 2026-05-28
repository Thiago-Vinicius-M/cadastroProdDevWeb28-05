package com.exemplo.produtos.controller;

import com.exemplo.produtos.model.Produto;
import com.exemplo.produtos.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller que contém DOIS tipos de endpoints:
 *
 * 1. Endpoint de PÁGINA (GET /) -> retorna o template HTML "produtos"
 *    via Thymeleaf (anotação @Controller)
 *
 * 2. API REST (GET/POST/DELETE /api/produtos) -> retorna JSON para
 *    o JavaScript consumir via AJAX (anotação @ResponseBody implícita
 *    nos métodos que precisam retornar JSON)
 *
 * Separamos a URL da API em /api/produtos para deixar claro
 * o que é página HTML e o que é endpoint de dados.
 */
@Controller
public class ProdutoController {

    /**
     * Spring injeta automaticamente o ProdutoService aqui.
     */
    @Autowired
    private ProdutoService service;

    // ================================================================
    // ENDPOINT DE PÁGINA
    // ================================================================

    /**
     * Rota raiz: retorna a página principal com a lista de produtos.
     *
     * @Controller + retorno de String = Thymeleaf renderiza o template
     * com este nome em src/main/resources/templates/
     *
     * GET / -> renderiza templates/produtos.html
     */
    @GetMapping("/")
    public String paginaPrincipal() {
        return "produtos";  // resolve para src/main/resources/templates/produtos.html
    }

    // ================================================================
    // API REST - retorna JSON para o AJAX do JavaScript
    // ================================================================

    /**
     * Lista todos os produtos.
     *
     * @ResponseBody faz o Spring serializar o retorno como JSON
     * usando o Jackson (já incluso no spring-boot-starter-web).
     *
     * GET /api/produtos -> [ {"id":1, "nome":"...", ...}, ... ]
     */
    @GetMapping("/api/produtos")
    @ResponseBody
    public List<Produto> listar() {
        return service.listar();
    }

    /**
     * Cadastra um novo produto.
     *
     * @RequestBody faz o Spring desserializar o JSON do corpo da
     * requisição HTTP e criar um objeto Produto automaticamente.
     *
     * POST /api/produtos
     * Body: {"nome":"TV","categoria":"Eletrônicos","preco":1500.0,"quantidade":10}
     * Retorna: o produto salvo com o ID gerado
     */
    @PostMapping("/api/produtos")
    @ResponseBody
    public ResponseEntity<Produto> salvar(@RequestBody Produto produto) {
        Produto salvo = service.salvar(produto);
        return ResponseEntity.ok(salvo);
    }

    /**
     * Remove um produto pelo ID.
     *
     * @PathVariable captura o {id} da URL.
     *
     * DELETE /api/produtos/1 -> remove o produto com id=1
     * Retorna: mensagem de confirmação em texto
     */
    @DeleteMapping("/api/produtos/{id}")
    @ResponseBody
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok("Produto removido com sucesso.");
    }
}
