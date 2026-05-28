package com.exemplo.produtos.service;

import com.exemplo.produtos.model.Produto;
import com.exemplo.produtos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Camada de serviço: contém a lógica de negócio da aplicação.
 *
 * Em projetos simples como este, o Service apenas delega ao Repository.
 * Em projetos maiores, aqui ficaria validação, regras de negócio,
 * cálculos, etc.
 *
 * @Service informa ao Spring que esta classe é um componente de serviço.
 * O Spring cria uma instância única (Singleton) e a injeta onde for
 * necessário via @Autowired.
 */
@Service
public class ProdutoService {

    /**
     * @Autowired: o Spring injeta automaticamente uma instância do
     * ProdutoRepository aqui. Não precisamos usar "new".
     */
    @Autowired
    private ProdutoRepository repository;

    /**
     * Retorna todos os produtos cadastrados no banco.
     * Equivale a: SELECT * FROM produtos ORDER BY id
     */
    public List<Produto> listar() {
        return repository.findAll();
    }

    /**
     * Salva um produto novo ou atualiza um existente.
     * O JPA detecta automaticamente se é INSERT ou UPDATE
     * baseado na presença ou ausência do campo ID.
     */
    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    /**
     * Remove um produto pelo seu ID.
     * Lança EmptyResultDataAccessException se o ID não existir.
     */
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
