package com.exemplo.produtos.controller;

import com.exemplo.produtos.model.Produto;
import com.exemplo.produtos.service.ProdutoService;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller responsável por gerar o relatório PDF com JasperReports.
 *
 * Fluxo de geração do PDF:
 *
 *   1. Carrega o template .jrxml do classpath
 *   2. Compila o .jrxml em um JasperReport (objeto em memória)
 *   3. Busca os dados do banco via ProdutoService
 *   4. Cria um JRBeanCollectionDataSource com a lista de produtos
 *   5. Preenche o relatório com os dados
 *   6. Exporta para bytes PDF
 *   7. Escreve os bytes na resposta HTTP
 *
 * O browser recebe o PDF e o exibe ou faz download automaticamente.
 */
@Controller
public class RelatorioController {

    @Autowired
    private ProdutoService service;

    /**
     * Gera e retorna um PDF com todos os produtos cadastrados.
     *
     * GET /relatorio -> resposta HTTP com Content-Type: application/pdf
     */
    @GetMapping("/relatorio")
    public void gerarRelatorio(HttpServletResponse response) throws Exception {

        // 1. Carrega o arquivo .jrxml do classpath (pasta resources/reports/)
        InputStream jrxmlStream = getClass()
            .getResourceAsStream("/reports/produtos.jrxml");

        if (jrxmlStream == null) {
            response.sendError(500, "Arquivo de template não encontrado: /reports/produtos.jrxml");
            return;
        }

        // 2. Compila o template JRXML em um JasperReport
        //    (processo que valida a estrutura XML e prepara para preenchimento)
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        // 3. Busca todos os produtos do banco de dados
        List<Produto> produtos = service.listar();

        // 4. Cria um DataSource a partir da lista de objetos Java
        //    O JasperReports acessa os campos via getters (getNome(), getPreco(), etc.)
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(produtos);

        // 5. Parâmetros adicionais para o relatório (poderia passar título, data, etc.)
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO_RELATORIO", "Relatório de Produtos");

        // 6. Preenche o relatório com os dados
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            jasperReport, parametros, dataSource
        );

        // 7. Configura a resposta HTTP para PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=relatorio-produtos.pdf");

        // 8. Exporta o relatório preenchido para bytes PDF
        //    e escreve diretamente no OutputStream da resposta HTTP
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
}
