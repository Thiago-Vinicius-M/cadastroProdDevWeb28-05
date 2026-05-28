package com.exemplo.produtos.repository;

import com.exemplo.produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório de acesso ao banco de dados para a entidade Produto.
 *
 * Ao estender JpaRepository<Produto, Long>, o Spring Data JPA
 * gera automaticamente a implementação de todos estes métodos:
 *
 *   - findAll()          -> SELECT * FROM produtos
 *   - findById(id)       -> SELECT * FROM produtos WHERE id = ?
 *   - save(produto)      -> INSERT ou UPDATE automático
 *   - deleteById(id)     -> DELETE FROM produtos WHERE id = ?
 *   - count()            -> SELECT COUNT(*) FROM produtos
 *   - existsById(id)     -> SELECT 1 FROM produtos WHERE id = ?
 *
 * Não precisamos escrever nenhuma query SQL!
 * O Spring Data JPA cuida de tudo.
 *
 * @Repository é opcional aqui (JpaRepository já é detectado),
 * mas torna o código mais legível e explícito.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Nenhum método adicional necessário para este projeto.
    // O Spring Data fornece tudo que precisamos via JpaRepository.
}
