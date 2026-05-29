package com.exemplo.produtos.controller;

import com.exemplo.produtos.model.Produto;
import com.exemplo.produtos.service.ProdutoService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class RelatorioController {

    @Autowired
    private ProdutoService service;

    @GetMapping("/relatorio")
    public void gerarRelatorio(HttpServletResponse response) throws Exception {
        List<Produto> produtos = service.listar();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=relatorio-produtos.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Título
        Font fontesTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLACK);
        Paragraph titulo = new Paragraph("Relatório de Produtos", fontesTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        // Data de geração
        Font fonteData = FontFactory.getFont(FontFactory.HELVETICA, 9, new Color(107, 114, 128));
        String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        Paragraph data = new Paragraph("Gerado em: " + dataHora, fonteData);
        data.setAlignment(Element.ALIGN_CENTER);
        document.add(data);

        document.add(Chunk.NEWLINE);

        // Tabela: ID | Nome | Categoria | Preço | Quantidade
        PdfPTable tabela = new PdfPTable(5);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{0.5f, 2.5f, 2f, 1.5f, 1f});

        Color corCabecalho = new Color(37, 99, 235);
        Font fonteCabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

        String[] colunas = {"ID", "Nome", "Categoria", "Preço", "Qtd."};
        int[] alinhamentos = {
            Element.ALIGN_CENTER,
            Element.ALIGN_LEFT,
            Element.ALIGN_LEFT,
            Element.ALIGN_RIGHT,
            Element.ALIGN_RIGHT
        };

        for (int i = 0; i < colunas.length; i++) {
            PdfPCell celula = new PdfPCell(new Phrase(colunas[i], fonteCabecalho));
            celula.setBackgroundColor(corCabecalho);
            celula.setHorizontalAlignment(alinhamentos[i]);
            celula.setPadding(6);
            celula.setBorderColor(corCabecalho);
            tabela.addCell(celula);
        }

        Font fonteDados = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
        boolean zebraAlternado = false;
        for (Produto p : produtos) {
            Color corLinha = zebraAlternado ? new Color(243, 244, 246) : Color.WHITE;
            zebraAlternado = !zebraAlternado;

            adicionarCelula(tabela, String.valueOf(p.getId()), fonteDados, corLinha, Element.ALIGN_CENTER);
            adicionarCelula(tabela, p.getNome(), fonteDados, corLinha, Element.ALIGN_LEFT);
            adicionarCelula(tabela, p.getCategoria(), fonteDados, corLinha, Element.ALIGN_LEFT);
            adicionarCelula(tabela, String.format("R$ %.2f", p.getPreco()).replace(".", ","), fonteDados, corLinha, Element.ALIGN_RIGHT);
            adicionarCelula(tabela, String.valueOf(p.getQuantidade()), fonteDados, corLinha, Element.ALIGN_RIGHT);
        }

        document.add(tabela);

        // Totalizador
        document.add(Chunk.NEWLINE);
        Font fonteTotalizador = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
        document.add(new Paragraph("Total de produtos cadastrados: " + produtos.size(), fonteTotalizador));

        document.close();
    }

    private void adicionarCelula(PdfPTable tabela, String texto, Font fonte, Color bg, int alinhamento) {
        PdfPCell celula = new PdfPCell(new Phrase(texto != null ? texto : "", fonte));
        celula.setBackgroundColor(bg);
        celula.setHorizontalAlignment(alinhamento);
        celula.setPadding(5);
        tabela.addCell(celula);
    }
}
