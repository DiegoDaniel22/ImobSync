package Panels;

import db.MongoDBConnection;
import db.dao.AtendimentoDAO;
import models.Atendimento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RelatorioPanel extends JPanel {
    public RelatorioPanel() {
        setLayout(new BorderLayout());

        // Painel do filtro de status
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        JLabel statusLabel = new JLabel("Filtrar por Status:");
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Todos", "Agendado", "Concluído", "Cancelado"});
        JButton buscarButton = new JButton("Buscar");

        filterPanel.add(statusLabel);
        filterPanel.add(statusComboBox);
        filterPanel.add(buscarButton);

        add(filterPanel, BorderLayout.NORTH);

        // Modelo da tabela para o relatório
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new String[]{
                "Cliente", "Empresa", "Funcionário", "Data", "Horário", "Local", "Situação", "Observações"
        });
        JTable atendimentoTable = new JTable(tableModel);
        add(new JScrollPane(atendimentoTable), BorderLayout.CENTER);

        // Conexão com o MongoDB
        AtendimentoDAO atendimentoDAO = new AtendimentoDAO(MongoDBConnection.getDatabase());

        // Ação ao clicar no botão buscar
        buscarButton.addActionListener(e -> {
            String statusSelecionado = (String) statusComboBox.getSelectedItem();
            List<Atendimento> atendimentos;

            // Buscar atendimentos com base no status selecionado
            if ("Todos".equals(statusSelecionado)) {
                atendimentos = atendimentoDAO.buscarTodosAtendimentos();
            } else {
                atendimentos = atendimentoDAO.buscarAtendimentosPorStatus(statusSelecionado);
            }

            // Limpar a tabela antes de adicionar os resultados
            tableModel.setRowCount(0);

            // Preencher a tabela com os dados filtrados
            for (Atendimento atendimento : atendimentos) {
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
        });
    }
}