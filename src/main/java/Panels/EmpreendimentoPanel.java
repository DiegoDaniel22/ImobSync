package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import db.MongoDBConnection;
import db.dao.EmpreendimentoDAO;
import models.Empreendimento;
import models.Empresa;
import models.RegistroCompra;

public class EmpreendimentoPanel extends JPanel {
    public EmpreendimentoPanel() {
        setLayout(new BorderLayout());

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Empreendimentos"));

        formPanel.add(new JLabel("Matrícula:"));
        JTextField matriculaField = new JTextField();
        formPanel.add(matriculaField);

        formPanel.add(new JLabel("Tipo de Empreendimento:"));
        JTextField tipoEmpreendimentoField = new JTextField();
        formPanel.add(tipoEmpreendimentoField);

        formPanel.add(new JLabel("Empresa Responsável (Razão Social):"));
        JTextField empresaRazaoSocialField = new JTextField();
        formPanel.add(empresaRazaoSocialField);

        formPanel.add(new JLabel("Valor:"));
        JTextField valorField = new JTextField();
        formPanel.add(valorField);

        formPanel.add(new JLabel("Ponto de Venda:"));
        JTextField pontoVendaField = new JTextField();
        formPanel.add(pontoVendaField);

        formPanel.add(new JLabel("Comissão Máxima:"));
        JTextField comissaoMaximaField = new JTextField();
        formPanel.add(comissaoMaximaField);

        formPanel.add(new JLabel("Margem de Negociação:"));
        JTextField margemNegociacaoField = new JTextField();
        formPanel.add(margemNegociacaoField);

        formPanel.add(new JLabel("Projeto Urbanístico:"));
        JTextField projetoUrbanisticoField = new JTextField();
        formPanel.add(projetoUrbanisticoField);

        JButton salvarButton = new JButton("Salvar");
        JButton limparButton = new JButton("Limpar");

        formPanel.add(salvarButton);
        formPanel.add(limparButton);

        add(formPanel, BorderLayout.NORTH);

        // Modelo da tabela
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{},
                new String[]{"Matrícula", "Tipo", "Empresa", "Valor", "Ponto de Venda", "Comissão Máxima", "Margem"});
        JTable empreendimentoTable = new JTable(tableModel);
        add(new JScrollPane(empreendimentoTable), BorderLayout.CENTER);

        // Conexão com o MongoDB
        EmpreendimentoDAO empreendimentoDAO = new EmpreendimentoDAO(MongoDBConnection.getDatabase());

        // Carregar empreendimentos salvos no banco de dados ao iniciar
        loadEmpreendimentos(empreendimentoDAO, tableModel);

        // Ação ao clicar no botão salvar
        salvarButton.addActionListener(e -> {
            // Criando um novo empreendimento
            Empreendimento empreendimento = new Empreendimento();
            empreendimento.setMatricula(matriculaField.getText());
            empreendimento.setTipoEmpreendimento(tipoEmpreendimentoField.getText());

            Empresa empresa = new Empresa();
            empresa.setRazaoSocial(empresaRazaoSocialField.getText());
            empreendimento.setEmpresaResponsavel(empresa);

            empreendimento.setValor(Float.parseFloat(valorField.getText()));
            empreendimento.setPontoVenda(pontoVendaField.getText());
            empreendimento.setComissaoMaxima(Float.parseFloat(comissaoMaximaField.getText()));
            empreendimento.setMargemNegociacao(Float.parseFloat(margemNegociacaoField.getText()));
            empreendimento.setProjetoUrbanistico(projetoUrbanisticoField.getText());

            // Salvar no MongoDB
            empreendimentoDAO.salvarEmpreendimento(empreendimento);

            // Atualizar a tabela
            tableModel.addRow(new Object[]{
                    empreendimento.getMatricula(),
                    empreendimento.getTipoEmpreendimento(),
                    empresa.getRazaoSocial(),
                    empreendimento.getValor(),
                    empreendimento.getPontoVenda(),
                    empreendimento.getComissaoMaxima(),
                    empreendimento.getMargemNegociacao()
            });

            JOptionPane.showMessageDialog(this, "Empreendimento salvo com sucesso!");
        });

        // Ação ao clicar no botão limpar
        limparButton.addActionListener(e -> {
            matriculaField.setText("");
            tipoEmpreendimentoField.setText("");
            empresaRazaoSocialField.setText("");
            valorField.setText("");
            pontoVendaField.setText("");
            comissaoMaximaField.setText("");
            margemNegociacaoField.setText("");
            projetoUrbanisticoField.setText("");
        });
    }

    private void loadEmpreendimentos(EmpreendimentoDAO empreendimentoDAO, DefaultTableModel tableModel) {
        // Buscar todos os empreendimentos do banco de dados
        for (Empreendimento empreendimento : empreendimentoDAO.buscarTodosEmpreendimentos()) {
            Empresa empresa = empreendimento.getEmpresaResponsavel();
            tableModel.addRow(new Object[]{
                    empreendimento.getMatricula(),
                    empreendimento.getTipoEmpreendimento(),
                    empresa != null ? empresa.getRazaoSocial() : "",
                    empreendimento.getValor(),
                    empreendimento.getPontoVenda(),
                    empreendimento.getComissaoMaxima(),
                    empreendimento.getMargemNegociacao()
            });
        }
    }
}