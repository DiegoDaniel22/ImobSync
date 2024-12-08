package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import db.MongoDBConnection;
import db.dao.FuncionarioDAO;
import models.Funcionario;
import models.Contato;

public class FuncionarioPanel extends JPanel {
    public FuncionarioPanel() {
        setLayout(new BorderLayout());

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Funcionários"));

        formPanel.add(new JLabel("Nome:"));
        JTextField nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Telefone:"));
        JTextField telefoneField = new JTextField();
        formPanel.add(telefoneField);

        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Endereço:"));
        JTextField enderecoField = new JTextField();
        formPanel.add(enderecoField);

        formPanel.add(new JLabel("CPF:"));
        JTextField cpfField = new JTextField();
        formPanel.add(cpfField);

        formPanel.add(new JLabel("Função:"));
        JTextField funcaoField = new JTextField();
        formPanel.add(funcaoField);

        formPanel.add(new JLabel("Comissão (%):"));
        JTextField comissaoField = new JTextField();
        formPanel.add(comissaoField);

        formPanel.add(new JLabel("Data de Admissão (dd/MM/yyyy):"));
        JTextField dataAdmissaoField = new JTextField();
        formPanel.add(dataAdmissaoField);

        formPanel.add(new JLabel("Data de Demissão (dd/MM/yyyy):"));
        JTextField dataDemissaoField = new JTextField();
        formPanel.add(dataDemissaoField);

        JButton salvarButton = new JButton("Salvar");
        JButton limparButton = new JButton("Limpar");

        formPanel.add(salvarButton);
        formPanel.add(limparButton);

        add(formPanel, BorderLayout.NORTH);

        // Modelo da tabela
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new String[]{
                "Nome", "Telefone", "Email", "Endereço", "CPF", "Função", "Comissão", "Admissão", "Demissão"
        });
        JTable funcionarioTable = new JTable(tableModel);
        add(new JScrollPane(funcionarioTable), BorderLayout.CENTER);

        // Conexão com o MongoDB
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO(MongoDBConnection.getDatabase());

        // Carregar funcionários salvos no banco de dados ao iniciar
        loadFuncionarios(funcionarioDAO, tableModel);

        // Ação ao clicar no botão salvar
        salvarButton.addActionListener(e -> {
            try {
                // Criação de um novo funcionário
                Contato contato = new Contato();
                contato.setNome(nomeField.getText());
                contato.setTelefone(telefoneField.getText());
                contato.setEmail(emailField.getText());
                contato.setEndereco(enderecoField.getText());

                Funcionario funcionario = new Funcionario();
                funcionario.setContato(contato);
                funcionario.setCpf(cpfField.getText());
                funcionario.setFuncao(funcaoField.getText());
                funcionario.setComissao(Float.parseFloat(comissaoField.getText()));

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                funcionario.setDataAdmissao(sdf.parse(dataAdmissaoField.getText()));
                funcionario.setDataDemissao(dataDemissaoField.getText().isEmpty() ? null : sdf.parse(dataDemissaoField.getText()));

                // Salvar no MongoDB
                funcionarioDAO.salvarFuncionario(funcionario);

                // Atualizar a tabela
                tableModel.addRow(new Object[]{
                        contato.getNome(),
                        contato.getTelefone(),
                        contato.getEmail(),
                        contato.getEndereco(),
                        funcionario.getCpf(),
                        funcionario.getFuncao(),
                        funcionario.getComissao(),
                        sdf.format(funcionario.getDataAdmissao()),
                        funcionario.getDataDemissao() == null ? "" : sdf.format(funcionario.getDataDemissao())
                });

                JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar funcionário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação ao clicar no botão limpar
        limparButton.addActionListener(e -> {
            nomeField.setText("");
            telefoneField.setText("");
            emailField.setText("");
            enderecoField.setText("");
            cpfField.setText("");
            funcaoField.setText("");
            comissaoField.setText("");
            dataAdmissaoField.setText("");
            dataDemissaoField.setText("");
        });
    }

    private void loadFuncionarios(FuncionarioDAO funcionarioDAO, DefaultTableModel tableModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Funcionario funcionario : funcionarioDAO.buscarTodosFuncionarios()) {
            Contato contato = funcionario.getContato();
            tableModel.addRow(new Object[]{
                    contato.getNome(),
                    contato.getTelefone(),
                    contato.getEmail(),
                    contato.getEndereco(),
                    funcionario.getCpf(),
                    funcionario.getFuncao(),
                    funcionario.getComissao(),
                    sdf.format(funcionario.getDataAdmissao()),
                    funcionario.getDataDemissao() == null ? "" : sdf.format(funcionario.getDataDemissao())
            });
        }
    }
}