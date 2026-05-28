/**
 * app.js - Lógica AJAX da aplicação de produtos
 * ================================================
 * Este arquivo usa a Fetch API (JavaScript nativo) para se comunicar
 * com o backend Spring Boot via requisições HTTP assíncronas.
 *
 * SEM jQuery, SEM React, SEM Angular - apenas JavaScript puro!
 *
 * Fluxo:
 *   1. Ao carregar a página -> carregarProdutos() busca a lista via GET
 *   2. Ao submeter o formulário -> salvarProduto() envia via POST
 *   3. Ao clicar em Excluir -> excluirProduto(id) envia via DELETE
 *   Após cada operação, a tabela é recarregada automaticamente.
 */

// ================================================================
// CARREGAR PRODUTOS - GET /api/produtos
// ================================================================

/**
 * Busca todos os produtos do servidor e atualiza a tabela.
 *
 * fetch() retorna uma Promise. Usamos .then() para encadear:
 *   1. response.json() -> converte o corpo da resposta de JSON para objeto JS
 *   2. renderizarTabela(produtos) -> popula o HTML
 */
function carregarProdutos() {
    fetch('/api/produtos')
        .then(function(response) {
            if (!response.ok) {
                throw new Error('Erro ao buscar produtos: ' + response.status);
            }
            return response.json();
        })
        .then(function(produtos) {
            renderizarTabela(produtos);
        })
        .catch(function(erro) {
            console.error('Falha ao carregar produtos:', erro);
        });
}

// ================================================================
// RENDERIZAR TABELA
// ================================================================

/**
 * Recebe um array de objetos Produto (vindo do JSON do servidor)
 * e insere as linhas na tabela HTML.
 *
 * @param {Array} produtos - array de objetos { id, nome, categoria, preco, quantidade }
 */
function renderizarTabela(produtos) {
    var corpoTabela   = document.getElementById('corpo-tabela');
    var semProdutos   = document.getElementById('sem-produtos');
    var contadorEl    = document.getElementById('contador-produtos');

    // Atualiza o contador no cabeçalho do card
    var texto = produtos.length === 1 ? '1 produto' : produtos.length + ' produtos';
    contadorEl.textContent = texto;

    // Mostra mensagem vazia ou esconde
    if (produtos.length === 0) {
        corpoTabela.innerHTML = '';
        semProdutos.style.display = 'flex';
        return;
    }

    semProdutos.style.display = 'none';

    // Constrói as linhas HTML dinamicamente
    var linhas = produtos.map(function(p) {
        // Formata preço como moeda brasileira: R$ 1.500,00
        var precoFormatado = p.preco.toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        });

        /*
         * Template literal (``) permite escrever HTML multi-linha.
         * Cada linha da tabela tem um botão "Excluir" que chama
         * excluirProduto() passando o id do produto.
         *
         * SEGURANÇA: usamos textContent em vez de innerHTML para
         * exibir dados do usuário, evitando XSS. Aqui usamos
         * innerText/textContent via JS após criar os elementos.
         *
         * Para simplificar didaticamente, usamos template string
         * mas escapamos os valores com a função escHtml().
         */
        return `<tr>
            <td>#${p.id}</td>
            <td>${escHtml(p.nome)}</td>
            <td>${escHtml(p.categoria)}</td>
            <td class="preco-cell">${precoFormatado}</td>
            <td class="qtd-cell">${p.quantidade}</td>
            <td>
                <button
                    class="btn btn-danger"
                    onclick="excluirProduto(${p.id}, this)">
                    Excluir
                </button>
            </td>
        </tr>`;
    });

    corpoTabela.innerHTML = linhas.join('');
}

// ================================================================
// SALVAR PRODUTO - POST /api/produtos
// ================================================================

/**
 * Intercepta o submit do formulário e envia os dados via AJAX.
 *
 * event.preventDefault() impede o recarregamento padrão da página.
 * Os dados são coletados do formulário e serializados como JSON.
 */
document.getElementById('form-produto').addEventListener('submit', function(event) {
    event.preventDefault(); // <-- impede reload da página

    // Coleta os valores dos campos do formulário
    var produto = {
        nome:       document.getElementById('nome').value.trim(),
        categoria:  document.getElementById('categoria').value.trim(),
        preco:      parseFloat(document.getElementById('preco').value),
        quantidade: parseInt(document.getElementById('quantidade').value, 10)
    };

    // Validação básica no frontend
    if (!produto.nome || !produto.categoria || isNaN(produto.preco) || isNaN(produto.quantidade)) {
        mostrarMensagem('Preencha todos os campos corretamente.', 'erro');
        return;
    }

    /*
     * fetch() com método POST:
     *   - method: 'POST' -> envia os dados ao servidor
     *   - headers: Content-Type: application/json -> informa que o corpo é JSON
     *   - body: JSON.stringify(produto) -> converte o objeto JS para string JSON
     *
     * O servidor (@RequestBody) deserializa o JSON de volta para Produto.java
     */
    fetch('/api/produtos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(produto)
    })
    .then(function(response) {
        if (!response.ok) {
            throw new Error('Erro ao salvar: ' + response.status);
        }
        return response.json();
    })
    .then(function(produtoSalvo) {
        // Sucesso: limpa o formulário e recarrega a tabela
        document.getElementById('form-produto').reset();
        mostrarMensagem('Produto "' + escHtml(produtoSalvo.nome) + '" cadastrado com sucesso!', 'sucesso');
        carregarProdutos(); // Atualiza a tabela SEM recarregar a página
    })
    .catch(function(erro) {
        mostrarMensagem('Erro ao cadastrar produto. Tente novamente.', 'erro');
        console.error(erro);
    });
});

// ================================================================
// EXCLUIR PRODUTO - DELETE /api/produtos/{id}
// ================================================================

/**
 * Remove um produto pelo ID via requisição DELETE.
 *
 * @param {number} id - ID do produto a ser removido
 * @param {HTMLElement} botao - referência ao botão clicado (para feedback visual)
 */
function excluirProduto(id, botao) {
    if (!confirm('Deseja remover este produto?')) {
        return; // usuário cancelou
    }

    // Feedback visual: desabilita o botão durante a requisição
    botao.disabled = true;
    botao.textContent = '...';

    /*
     * fetch() com método DELETE:
     * A URL inclui o ID: /api/produtos/1
     * O servidor captura via @PathVariable Long id
     */
    fetch('/api/produtos/' + id, {
        method: 'DELETE'
    })
    .then(function(response) {
        if (!response.ok) {
            throw new Error('Erro ao excluir: ' + response.status);
        }
        return response.text(); // resposta é texto, não JSON
    })
    .then(function() {
        carregarProdutos(); // Atualiza a tabela SEM recarregar a página
        mostrarMensagem('Produto removido com sucesso.', 'sucesso');
    })
    .catch(function(erro) {
        botao.disabled = false;
        botao.textContent = 'Excluir';
        mostrarMensagem('Erro ao remover produto.', 'erro');
        console.error(erro);
    });
}

// ================================================================
// FUNÇÕES AUXILIARES
// ================================================================

/**
 * Exibe uma mensagem de feedback ao usuário.
 * A mensagem some automaticamente após 4 segundos.
 *
 * @param {string} texto - mensagem a exibir
 * @param {string} tipo  - 'sucesso' ou 'erro'
 */
function mostrarMensagem(texto, tipo) {
    var el = document.getElementById('mensagem');
    el.textContent = texto;
    el.className = 'mensagem ' + tipo;
    el.style.display = 'block';

    // Oculta automaticamente após 4 segundos
    setTimeout(function() {
        el.style.display = 'none';
    }, 4000);
}

/**
 * Escapa caracteres HTML especiais para evitar XSS.
 * Converte <, >, &, ", ' em entidades HTML seguras.
 *
 * @param {string} str - string a escapar
 * @returns {string} string escapada
 */
function escHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

// ================================================================
// INICIALIZAÇÃO
// ================================================================

// Carrega a lista de produtos assim que a página termina de carregar.
// window.onload garante que o DOM está pronto antes de manipulá-lo.
window.onload = function() {
    carregarProdutos();
};
