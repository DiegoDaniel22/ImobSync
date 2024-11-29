import javax.swing.*;
import java.awt.*;

public class ClientePanel extends JPanel {
    public ClientePanel() {
        setLayout(new BorderLayout());

        // Formulário de cadastro
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

        // Tabela de clientes
        JTable clienteTable = new JTable(new Object[][]{
                {"1", "João Silva", "99999-9999", "joao@email.com", "Rua A", "LEAD"},
                {"2", "Maria Oliveira", "88888-8888", "maria@email.com", "Rua B", "Cliente"}
        }, new String[]{"ID", "Nome", "Telefone", "Email", "Endereço", "Status"});

        add(new JScrollPane(clienteTable), BorderLayout.CENTER);
    }
}