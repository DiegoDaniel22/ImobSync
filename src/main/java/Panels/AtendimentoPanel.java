package Panels;

import db.MongoDBConnection;
import db.dao.AtendimentoDAO;
import models.Atendimento;
import models.Cliente;
import models.Empresa;
import models.Funcionario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Time;
import java.util.Date;

public class AtendimentoPanel extends JPanel {
    public AtendimentoPanel() {
        setLayout(new BorderLayout());

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Agendamento de Visitas"));

        formPanel.add(new JLabel("Cliente:"));
        JTextField clienteField = new JTextField();
        formPanel.add(clienteField);

        formPanel.add(new JLabel("Empresa Responsável:"));
        JTextField empresaField = new JTextField();
        formPanel.add(empresaField);

        formPanel.add(new JLabel("Funcionário:"));
        JTextField funcionarioField = new JTextField();
        formPanel.add(funcionarioField);

// Substituindo o JDateChooser por JSpinner
        formPanel.add(new JLabel("Data da Visita:"));
        JSpinner dataVisitaSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dataVisitaSpinner, "dd/MM/yyyy");
        dataVisitaSpinner.setEditor(editor);
        formPanel.add(dataVisitaSpinner);

        formPanel.add(new JLabel("Horário da Visita:"));
        JTextField horarioVisitaField = new JTextField();
        formPanel.add(horarioVisitaField);

        formPanel.add(new JLabel("Local de Encontro:"));
        JTextField localEncontroField = new JTextField();
        formPanel.add(localEncontroField);

        formPanel.add(new JLabel("Situação:"));
        JComboBox<String> situacaoComboBox = new JComboBox<>(new String[]{"Agendado", "Concluído", "Cancelado"});
        formPanel.add(situacaoComboBox);

        formPanel.add(new JLabel("Observações:"));
        JTextField observacoesField = new JTextField();
        formPanel.add(observacoesField);

        JButton salvarButton = new JButton("Salvar");
        JButton limparButton = new JButton("Limpar");

        formPanel.add(salvarButton);
        formPanel.add(limparButton);

        add(formPanel, BorderLayout.NORTH);

// Modelo da tabela
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new String[]{
                "Cliente", "Empresa", "Funcionário", "Data", "Horário", "Local", "Situação", "Observações"
        });
        JTable atendimentoTable = new JTable(tableModel);
        add(new JScrollPane(atendimentoTable), BorderLayout.CENTER);

// Conexão com o MongoDB
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(MongoDBConnection.getDatabase());

// Carregar atendimentos salvos no banco de dados ao iniciar
        loadAtendimentos(atendimentoDAO, tableModel);

// Ação ao clicar no botão salvar
        salvarButton.addActionListener(e -> {
            // Criação de um novo atendimento
            Atendimento atendimento = new Atendimento();
            Cliente cliente = new Cliente();
            cliente.setIdCliente(clienteField.getText());  // Buscar o cliente de forma adequada, ex: por ID
            atendimento.setCliente(cliente);

            Empresa empresa = new Empresa();
            empresa.setRazaoSocial(empresaField.getText());  // Buscar empresa
            atendimento.setAgenteEmpresa(empresa);

            Funcionario funcionario = new Funcionario();
            funcionario.setNome(funcionarioField.getText());  // Buscar funcionário
            atendimento.setAgenteFuncionario(funcionario);

            // Verificar se a data foi selecionada
            Date dataVisita = (Date) dataVisitaSpinner.getValue();
            if (dataVisita != null) {
                atendimento.setDataVisita(dataVisita);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione a data da visita.");
                return; // Retorna para não continuar com a operação
            }

            atendimento.setHorarioVisita(Time.valueOf(horarioVisitaField.getText() + ":00"));
            atendimento.setLocarEncontro(localEncontroField.getText());
            atendimento.setSituacao((String) situacaoComboBox.getSelectedItem());
            atendimento.setObservacoesAtendimento(observacoesField.getText());

            // Salvar no MongoDB
            atendimentoDAO.salvarAtendimento(atendimento);

            // Atualizar a tabela
            tableModel.addRow(new Object[]{
                    cliente.getIdCliente(),
                    empresa.getRazaoSocial(),
                    funcionario.getContato().getNome(),
                    atendimento.getDataVisita(),
                    atendimento.getHorarioVisita(),
                    atendimento.getLocarEncontro(),
                    atendimento.getSituacao(),
                    atendimento.getObservacoesAtendimento()
            });

            JOptionPane.showMessageDialog(this, "Atendimento agendado com sucesso!");
        });

// Ação ao clicar no botão limpar
        limparButton.addActionListener(e -> {
            clienteField.setText("");
            empresaField.setText("");
            funcionarioField.setText("");
            dataVisitaSpinner.setValue(new Date());
            horarioVisitaField.setText("");
            localEncontroField.setText("");
            situacaoComboBox.setSelectedIndex(0);
            observacoesField.setText("");
        });
    }

    private void loadAtendimentos(AtendimentoDAO atendimentoDAO, DefaultTableModel tableModel) {
        // Buscar todos os atendimentos do banco de dados
        for (Atendimento atendimento : atendimentoDAO.buscarTodosAtendimentos()) {
            tableModel.addRow(new Object[]{
                    atendimento.getCliente().getIdCliente(),
                    atendimento.getAgenteEmpresa().getRazaoSocial(),
                    atendimento.getAgenteFuncionario().getContato().getNome(),
                    atendimento.getDataVisita(),
                    atendimento.getHorarioVisita(),
                    atendimento.getLocarEncontro(),
                    atendimento.getSituacao(),
                    atendimento.getObservacoesAtendimento()
            });
        }
    }
}