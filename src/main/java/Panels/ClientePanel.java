package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import db.MongoDBConnection;
import db.dao.ClienteDAO;
import models.Cliente;
import models.Contato;

public class ClientePanel extends JPanel {
    public ClientePanel() {
        setLayout(new BorderLayout());

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Clientes"));

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

        formPanel.add(new JLabel("Status:"));
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"LEAD", "Em Negociação", "Cliente"});
        formPanel.add(statusComboBox);

        JButton salvarButton = new JButton("Salvar");
        JButton limparButton = new JButton("Limpar");

        formPanel.add(salvarButton);
        formPanel.add(limparButton);

        add(formPanel, BorderLayout.NORTH);

        // Modelo da tabela
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Nome", "Telefone", "Email", "Endereço", "Status"});
        JTable clienteTable = new JTable(tableModel);
        add(new JScrollPane(clienteTable), BorderLayout.CENTER);

        // Conexão com o MongoDB
        ClienteDAO clienteDAO = new ClienteDAO(MongoDBConnection.getDatabase());

        // Carregar clientes salvos no banco de dados ao iniciar
        loadClientes(clienteDAO, tableModel);

        // Ação ao clicar no botão salvar
        salvarButton.addActionListener(e -> {
            // Criação de um novo cliente
            Contato contato = new Contato();
            contato.setNome(nomeField.getText());
            contato.setTelefone(telefoneField.getText());
            contato.setEmail(emailField.getText());
            contato.setEndereco(enderecoField.getText());

            Cliente cliente = new Cliente();
            cliente.setContato(contato);
            cliente.setTipoCliente((String) statusComboBox.getSelectedItem());
            cliente.setObservações(""); // Pode adicionar um campo para observações se necessário

            // Salvar no MongoDB
            clienteDAO.salvarCliente(cliente);

            // Atualizar a tabela
            tableModel.addRow(new Object[]{
                    contato.getNome(),
                    contato.getTelefone(),
                    contato.getEmail(),
                    contato.getEndereco(),
                    cliente.getTipoCliente()
            });

            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
        });

        // Ação ao clicar no botão limpar
        limparButton.addActionListener(e -> {
            nomeField.setText("");
            telefoneField.setText("");
            emailField.setText("");
            enderecoField.setText("");
            statusComboBox.setSelectedIndex(0);
        });
    }

    // Método para carregar os clientes já salvos no banco de dados
    private void loadClientes(ClienteDAO clienteDAO, DefaultTableModel tableModel) {
        // Buscar todos os clientes do banco de dados
        for (Cliente cliente : clienteDAO.buscarTodosClientes()) {
            Contato contato = cliente.getContato();
            tableModel.addRow(new Object[]{
                    contato.getNome(),
                    contato.getTelefone(),
                    contato.getEmail(),
                    contato.getEndereco(),
                    cliente.getTipoCliente()
            });
        }
    }
}