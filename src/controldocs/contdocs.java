package controldocs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import java.awt.HeadlessException;
import java.awt.Font;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

//import org.apache.pdfbox.*;
//import org.apache.pdfbox.commons.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableWidthType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import com.itextpdf.text.Paragraph;

import com.lowagie.text.pdf.parser.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class contdocs {

    private String nomeArquivo;
    private String formatoArquivo;
    private String localSalvar;

    public contdocs() {
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    private String obterNomeArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar arquivo");

        fileChooser.setFileFilter(new FileNameExtensionFilter("Documentos do Word (.docx)", "docx"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos PDF (.pdf)", "pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Planilhas do Excel (.xlxs)", "xlxs"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos XML (.xml)", "xml"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de Texto (.txt)", "txt"));

        int resultado = fileChooser.showSaveDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();
            return arquivoSelecionado.getAbsolutePath();
        } else {
            return null;
        }
    }

    public String getFormatoArquivo() {
        return formatoArquivo;
    }

    public void setFormatoArquivo(String formatoArquivo) {
        this.formatoArquivo = formatoArquivo;
    }

    public String getLocalSalvar() {
        return localSalvar;
    }

    public void setLocalSalvar(String localSalvar) {
        this.localSalvar = localSalvar;
    }

    public String adicionarExtensao(String nomeArquivo, String formato) {
        if (!nomeArquivo.toLowerCase().endsWith("." + formato.toLowerCase())) {
            nomeArquivo += "." + formato.toLowerCase();
        }
        return nomeArquivo;
    }

    private String formatarHorario(LocalDateTime horario) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return horario.format(formatter);
    }

    private String formatarHorarioPesquisa(LocalDateTime horarioPesquisa) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return horarioPesquisa.format(formatter);
    }

    private String formatarHorarioExportacao(LocalDateTime horarioExportacao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return horarioExportacao.format(formatter);
    }

    public void exportarDados(ArrayList<Object[]> dados, LocalDateTime horarioPesquisa, LocalDateTime horarioExportacao, String formatoSelecionado) {
        if (nomeArquivo == null || nomeArquivo.isEmpty()) {
            nomeArquivo = obterNomeArquivo();
            if (nomeArquivo == null) {
                JOptionPane.showMessageDialog(null, "Nenhum nome de arquivo fornecido!");
                return;
            }
        }

        String nomeArquivoCompleto = adicionarExtensao(nomeArquivo, formatoSelecionado);

        if (formatoSelecionado.equals("txt")) {
            salvarEmTxt(dados, horarioPesquisa, horarioExportacao, nomeArquivoCompleto);
        } else if (formatoSelecionado.equals("pdf")) {
            salvarEmPdf(dados, horarioPesquisa, horarioExportacao, nomeArquivoCompleto);
        } else if (formatoSelecionado.equals("xml")) {
            salvarEmXml(dados, horarioPesquisa, horarioExportacao, nomeArquivoCompleto);
        } else if (formatoSelecionado.equals("docx")) {
            salvarEmDocx(dados, horarioPesquisa, horarioExportacao, nomeArquivoCompleto);
        } else if (formatoSelecionado.equals("xlsx")) {
            salvarEmXlsx(dados, horarioPesquisa, horarioExportacao, nomeArquivoCompleto);
        } else {
            JOptionPane.showMessageDialog(null, "Formato de exportação inválido.");
        }

        LocalDateTime ultimoHorarioExportacao = horarioExportacao;
        String ultimoFormatoExportacao = formatoSelecionado;
    }

    public String obterFormatoSelecionado() {
        return formatoArquivo;
    }

    // Salva os usuários em um arquivo TXT
    public void salvarEmTxt(ArrayList<Object[]> dados, LocalDateTime horarioPesquisa, LocalDateTime horarioExportacao, String nomeArquivoCompleto) {
        try (FileOutputStream fos = new FileOutputStream(nomeArquivoCompleto)) {
            StringBuilder conteudo = new StringBuilder();
            conteudo.append("Dados do Usuario\n\n");
            conteudo.append("ID\t\tNome\t\tEmail\t\tSenha\t\tPermissao\n");
            for (Object[] linha : dados) {
                conteudo.append(linha[0]).append("\t\t").append(linha[1]).append("\t\t")
                        .append(linha[2]).append("\t\t").append(linha[3]).append("\t\t")
                        .append(linha[4]).append("\n");
            }
            conteudo.append("\nHorário da Pesquisa: ").append(formatarHorarioPesquisa(horarioPesquisa));
            conteudo.append("\nHorário da Exportação: ").append(formatarHorario(horarioExportacao));
            fos.write(conteudo.toString().getBytes());
            JOptionPane.showMessageDialog(null, "Exportação para TXT concluída com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exportar para TXT: " + e.getMessage());
        }
    }

    // Salva os usuários em um arquivo DOCX
    public void salvarEmDocx(ArrayList<Object[]> dados, LocalDateTime horarioPesquisa, LocalDateTime horarioExportacao, String nomeArquivoCompleto) {
        try (FileOutputStream fos = new FileOutputStream(nomeArquivoCompleto)) {
            XWPFDocument document = new XWPFDocument();

            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun run = paragraph.createRun();
            run.setText("Dados do Usuario");
            run.setBold(true);
            run.setFontSize(18);

            XWPFTable table = document.createTable(dados.size() + 1, 5);
            table.setWidthType(TableWidthType.PCT);
            table.setWidth("100%");

            table.getRow(0).getCell(0).setText("ID");
            table.getRow(0).getCell(1).setText("Nome");
            table.getRow(0).getCell(2).setText("Email");
            table.getRow(0).getCell(3).setText("Senha");
            table.getRow(0).getCell(4).setText("Permissao");

            int rowIndex = 1;
            for (Object[] linha : dados) {
                table.getRow(rowIndex).getCell(0).setText(linha[0].toString());
                table.getRow(rowIndex).getCell(1).setText(linha[1].toString());
                table.getRow(rowIndex).getCell(2).setText(linha[2].toString());
                table.getRow(rowIndex).getCell(3).setText(linha[3].toString());
                table.getRow(rowIndex).getCell(4).setText(linha[4].toString());
                rowIndex++;
            }

            XWPFParagraph horario = document.createParagraph();
            horario.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun runHorario = horario.createRun();
            runHorario.setText("Horário da Pesquisa: " + formatarHorarioPesquisa(horarioPesquisa)
                    + " - Horário de Exportação: " + formatarHorarioPesquisa(LocalDateTime.now()));

            document.write(fos);
            JOptionPane.showMessageDialog(null, "Exportação para DOCX concluída com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exportar para DOCX: " + e.getMessage());
        }
    }

    // Salva os usuários em um arquivo PDF
    public void salvarEmPdf(ArrayList<Object[]> dados, LocalDateTime horarioPesquisa, LocalDateTime horarioExportacao, String nomeArquivoCompleto) {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(nomeArquivoCompleto));
            document.open();

            Paragraph titulo = new Paragraph("Dados do Usuario", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(titulo);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell cell;

            cell = new PdfPCell(new Phrase("ID"));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nome"));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Email"));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Senha"));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Permissao"));
            table.addCell(cell);
            for (Object[] linha : dados) {
                table.addCell(linha[0].toString());
                table.addCell(linha[1].toString());
                table.addCell(linha[2].toString());
                table.addCell(linha[3].toString());
                table.addCell(linha[4].toString());
            }

            document.add(table);

            Paragraph horarioPesquisaPara = new Paragraph("Horário da Pesquisa: " + formatarHorarioPesquisa(horarioPesquisa));
            horarioPesquisaPara.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            document.add(horarioPesquisaPara);

            Paragraph horarioExportPara = new Paragraph("Horário da Exportação: " + formatarHorario(horarioExportacao));
            horarioExportPara.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            document.add(horarioExportPara);

            document.close();

            JOptionPane.showMessageDialog(null, "Exportação para PDF concluída com sucesso!");
        } catch (DocumentException | HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exportar para PDF: " + e.getMessage());
        }
    }

    // Salva os usuários em um arquivo Xml
    public void salvarEmXml(ArrayList<Object[]> dados, LocalDateTime horarioPesquisa, LocalDateTime horarioExportacao, String nomeArquivoCompleto) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc;

        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("DadosUsuarios");
            doc.appendChild(rootElement);

            for (Object[] linha : dados) {
                Element user = doc.createElement("Usuario");
                rootElement.appendChild(user);

                Element id = doc.createElement("ID");
                id.appendChild(doc.createTextNode(linha[0].toString()));
                user.appendChild(id);

                Element nome = doc.createElement("Nome");
                nome.appendChild(doc.createTextNode(linha[1].toString()));
                user.appendChild(nome);

                Element email = doc.createElement("Email");
                email.appendChild(doc.createTextNode(linha[2].toString()));
                user.appendChild(email);

                Element senha = doc.createElement("Senha");
                senha.appendChild(doc.createTextNode(linha[3].toString()));
                user.appendChild(senha);

                Element permissao = doc.createElement("Permissao");
                permissao.appendChild(doc.createTextNode(linha[4].toString()));
                user.appendChild(permissao);
            }

            Element horarioPesquisaElement = doc.createElement("HorarioPesquisa");
            horarioPesquisaElement.appendChild(doc.createTextNode(horarioPesquisa.toString())); // Formatar se necessário
            rootElement.appendChild(horarioPesquisaElement);

            Element horarioExportElement = doc.createElement("HorarioExportacao");
            horarioExportElement.appendChild(doc.createTextNode(horarioExportacao.toString())); // Formatar se necessário
            rootElement.appendChild(horarioExportElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(nomeArquivoCompleto));
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(null, "Exportação para XML concluída com sucesso!");
        } catch (ParserConfigurationException | TransformerException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exportar para XML: " + e.getMessage());
        }
    }

    // Salva os usuários em um arquivo Xls
    public void salvarEmXlsx(ArrayList<Object[]> dados, LocalDateTime horarioPesquisa, LocalDateTime horarioExportacao, String nomeArquivoCompleto) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Dados do Usuario");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nome");
            headerRow.createCell(2).setCellValue("Email");
            headerRow.createCell(3).setCellValue("Senha");
            headerRow.createCell(4).setCellValue("Permissao");

            int rowIndex = 1;
            for (Object[] linha : dados) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(linha[0].toString());
                dataRow.createCell(1).setCellValue(linha[1].toString());
                dataRow.createCell(2).setCellValue(linha[2].toString());
                dataRow.createCell(3).setCellValue(linha[3].toString());
                dataRow.createCell(4).setCellValue(linha[3].toString());
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            try (FileOutputStream fos = new FileOutputStream(nomeArquivoCompleto)) {
                workbook.write(fos);

                // Adicionar o horário da pesquisa no final da planilha
                Row horarioRow = sheet.createRow(rowIndex);
                horarioRow.createCell(0).setCellValue("Horário da Pesquisa:");
                horarioRow.createCell(1).setCellValue(formatarHorarioPesquisa(horarioPesquisa));

                JOptionPane.showMessageDialog(null, "Exportação para XLSX concluída com sucesso!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exportar para XLSX: " + e.getMessage());
        }
    }

    private boolean verificarSeFormatoNulo() {
        return formatoArquivo == null || formatoArquivo.isEmpty();
    }

    private boolean verificarFormatoSelecionado() {
        return formatoArquivo.equals("txt") 
                || formatoArquivo.equals("pdf")
                || formatoArquivo.equals("xml") 
                || formatoArquivo.equals("docx")
                || formatoArquivo.equals("xlsx");
    }

    public ArrayList<Object[]> importarDadosDoDocumento() {
        String caminhoArquivo = obterCaminhoArquivo();

        if (caminhoArquivo == null) {
            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado!");
            return new ArrayList<>();
        }

        if (verificarFormatoSelecionado()) {
            ArrayList<Object[]> dados = new ArrayList<>();
            switch (formatoArquivo.toLowerCase()) {
                case "txt":
                    dados = importarDeTxt();
                    break;
                case "pdf":
                    dados = importarDePdf();
                    break;
                case "xml":
                    dados = importarDeXml();
                    break;
                case "docx":
                    dados = importarDeDocx();
                    break;
                case "xlsx":
                    dados = importarDeXlsx();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Formato de importação inválido.");
                    break;
            }

            return dados;
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum formato de arquivo selecionado!");
            return new ArrayList<>();
        }
    }


    public String obterCaminhoArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar arquivo");
        
        fileChooser.setFileFilter(new FileNameExtensionFilter("Documentos do Word (.docx)", "docx"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos PDF (.pdf)", "pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Planilhas do Excel (.xlxs)", "xlxs"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos XML (.xml)", "xml"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de Texto (.txt)", "txt"));

        int resultado = fileChooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();

            // Obtém a extensão do arquivo selecionado
            String nomeArquivo = arquivoSelecionado.getName();
            int pontoIndex = nomeArquivo.lastIndexOf(".");
            if (pontoIndex > 0 && pontoIndex < nomeArquivo.length() - 1) {
                String formato = nomeArquivo.substring(pontoIndex + 1).toLowerCase();
                // Atribui o formato do arquivo selecionado ao atributo formatoArquivo
                formatoArquivo = formato;
            }

            return arquivoSelecionado.getAbsolutePath();
        } else {
            return null;
        }
    }

    public ArrayList<Object[]> importarDeTxt() {
        ArrayList<Object[]> dados = new ArrayList<>();
        String caminhoArquivo = obterCaminhoArquivo();

        if (caminhoArquivo != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    Object[] colunas = linha.split("\t");
                    dados.add(colunas);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao importar do arquivo TXT: " + e.getMessage());
            }
        }

        return dados;
    }

    public ArrayList<Object[]> importarDePdf() {
        ArrayList<Object[]> dados = new ArrayList<>();
        String caminhoArquivo = obterCaminhoArquivo();

        if (caminhoArquivo != null) {
            try {
                PdfReader pdfReader = new PdfReader(caminhoArquivo);
                int numPaginas = pdfReader.getNumberOfPages();

                for (int pagina = 1; pagina <= numPaginas; pagina++) {
                    String textoPagina = com.itextpdf.text.pdf.parser.PdfTextExtractor.getTextFromPage(pdfReader, pagina);
                    String[] linhas = textoPagina.split("\n");
                    for (String linha : linhas) {
                        String[] colunas = linha.split("\t");
                        dados.add(colunas);
                    }
                }

                pdfReader.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao importar do arquivo PDF: " + e.getMessage());
            }
        }

        return dados;
    }

    public ArrayList<Object[]> importarDeXml() {
        ArrayList<Object[]> dados = new ArrayList<>();
        String caminhoArquivo = obterCaminhoArquivo();

        if (caminhoArquivo != null) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new File(caminhoArquivo));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Usuario");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String id = element.getElementsByTagName("ID").item(0).getTextContent();
                        String nome = element.getElementsByTagName("Nome").item(0).getTextContent();
                        String email = element.getElementsByTagName("Email").item(0).getTextContent();
                        String senha = element.getElementsByTagName("Senha").item(0).getTextContent();
                        String permissao = element.getElementsByTagName("Permissao ").item(0).getTextContent();
                        dados.add(new Object[]{id, nome, email, senha, permissao });
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao importar do arquivo XML: " + e.getMessage());
            }
        }

        return dados;
    }

    public ArrayList<Object[]> importarDeDocx() {
        ArrayList<Object[]> dados = new ArrayList<>();
        String caminhoArquivo = obterCaminhoArquivo();

        if (caminhoArquivo != null) {
            try (FileInputStream fis = new FileInputStream(caminhoArquivo); XWPFDocument document = new XWPFDocument(fis)) {

                Iterator<XWPFParagraph> paraIterator = document.getParagraphsIterator();
                while (paraIterator.hasNext()) {
                    XWPFParagraph paragraph = paraIterator.next();
                    String[] colunas = paragraph.getText().split("\t");
                    dados.add(colunas);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao importar do arquivo DOCX: " + e.getMessage());
            }
        }

        return dados;
    }

    public ArrayList<Object[]> importarDeXlsx() {
        ArrayList<Object[]> dados = new ArrayList<>();
        String caminhoArquivo = obterCaminhoArquivo();

        if (caminhoArquivo != null) {
            try (FileInputStream fis = new FileInputStream(caminhoArquivo); Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    ArrayList<Object> colunas = new ArrayList<>();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        colunas.add(cell.getStringCellValue());
                    }

                    dados.add(colunas.toArray());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao importar do arquivo XLSX: " + e.getMessage());
            }
        }

        return dados;
    }
}

