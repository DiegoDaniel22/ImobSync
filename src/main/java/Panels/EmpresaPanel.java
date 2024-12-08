package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import db.MongoDBConnection;
import db.dao.EmpresaDAO;
import models.Empresa;
import models.Responsavel;

public class EmpresaPanel extends JPanel {
    public EmpresaPanel() {
        setLayout(new BorderLayout());

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Empresas"));

        formPanel.add(new JLabel("Razão Social:"));
        JTextField razaoSocialField = new JTextField();
        formPanel.add(razaoSocialField);

        formPanel.add(new JLabel("CNPJ:"));
        JTextField cnpjField = new JTextField();
        formPanel.add(cnpjField);

        formPanel.add(new JLabel("CRECI:"));
        JTextField creciField = new JTextField();
        formPanel.add(creciField);

        formPanel.add(new JLabel("Comissão:"));
        JTextField comissaoField = new JTextField();
        formPanel.add(comissaoField);

        formPanel.add(new JLabel("Responsável (CPF):"));
        JTextField responsavelCpfField = new JTextField();
        formPanel.add(responsavelCpfField);

        JButton salvarButton = new JButton("Salvar");
        JButton limparButton = new JButton("Limpar");

        formPanel.add(salvarButton);
        formPanel.add(limparButton);

        add(formPanel, BorderLayout.NORTH);

        // Modelo da tabela
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{},
                new String[]{"Razão Social", "CNPJ", "CRECI", "Comissão", "Responsável (CPF)"});
        JTable empresaTable = new JTable(tableModel);
        add(new JScrollPane(empresaTable), BorderLayout.CENTER);

        // Conexão com o MongoDB
        EmpresaDAO empresaDAO = new EmpresaDAO(MongoDBConnection.getDatabase());

        // Carregar empresas salvas no banco de dados ao iniciar
        loadEmpresas(empresaDAO, tableModel);

        // Ação ao clicar no botão salvar
        salvarButton.addActionListener(e -> {
            // Criando uma nova empresa
            Empresa empresa = new Empresa();
            empresa.setRazaoSocial(razaoSocialField.getText());
            empresa.setCnpj(cnpjField.getText());
            empresa.setCreci(creciField.getText());
            empresa.setComissao(Float.parseFloat(comissaoField.getText()));

            Responsavel responsavel = new Responsavel();
            responsavel.setCpf(responsavelCpfField.getText());
            empresa.setResponsavel(responsavel);

            // Salvar no MongoDB
            empresaDAO.salvarEmpresa(empresa);

            // Atualizar a tabela
            tableModel.addRow(new Object[]{
                    empresa.getRazaoSocial(),
                    empresa.getCnpj(),
                    empresa.getCreci(),
                    empresa.getComissao(),
                    responsavel.getCpf()
            });

            JOptionPane.showMessageDialog(this, "Empresa salva com sucesso!");
        });

        // Ação ao clicar no botão limpar
        limparButton.addActionListener(e -> {
            razaoSocialField.setText("");
            cnpjField.setText("");
            creciField.setText("");
            comissaoField.setText("");
            responsavelCpfField.setText("");
        });
    }

    private void loadEmpresas(EmpresaDAO empresaDAO, DefaultTableModel tableModel) {
        // Buscar todas as empresas do banco de dados
        for (Empresa empresa : empresaDAO.buscarTodasEmpresas()) {
            Responsavel responsavel = empresa.getResponsavel();
            tableModel.addRow(new Object[]{
                    empresa.getRazaoSocial(),
                    empresa.getCnpj(),
                    empresa.getCreci(),
                    empresa.getComissao(),
                    responsavel != null ? responsavel.getCpf() : ""
            });
        }
    }
}