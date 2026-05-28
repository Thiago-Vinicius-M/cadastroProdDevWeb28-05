package com.exemplo.produtos.model;

import jakarta.persistence.*;

/**
 * Entidade JPA que representa um produto no banco de dados.
 *
 * @Entity  -> informa ao Hibernate que esta classe é uma tabela do banco
 * @Table   -> define o nome da tabela (opcional, mas deixa claro)
 *
 * O Hibernate lê as anotações desta classe e cria/atualiza a tabela
 * automaticamente porque configuramos ddl-auto=update no application.properties.
 */
@Entity
@Table(name = "produtos")
public class Produto {

    /**
     * @Id           -> chave primária da tabela
     * @GeneratedValue -> o banco gera o ID automaticamente (auto-increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do produto.
     * @Column(nullable = false) -> campo obrigatório no banco
     */
    @Column(nullable = false)
    private String nome;

    /**
     * Categoria do produto (ex: Eletrônicos, Alimentos, Roupas).
     */
    @Column(nullable = false)
    private String categoria;

    /**
     * Preço do produto. Usamos Double para simplicidade.
     */
    @Column(nullable = false)
    private Double preco;

    /**
     * Quantidade em estoque.
     */
    @Column(nullable = false)
    private Integer quantidade;

    // ==================== CONSTRUTORES ====================

    /** Construtor vazio obrigatório para o JPA/Hibernate */
    public Produto() {}

    public Produto(String nome, String categoria, Double preco, Integer quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    // ==================== GETTERS E SETTERS ====================
    // Necessários para o Jackson serializar/desserializar JSON
    // e para o JPA acessar os campos.

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
