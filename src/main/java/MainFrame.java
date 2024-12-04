import Panels.ClientePanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("CRM - Vendas de Empreendimentos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu principal
        JMenuBar menuBar = new JMenuBar();

        JMenu cadastroMenu = new JMenu("Base Cadastral");
        JMenuItem clientesItem = new JMenuItem("Clientes");
        JMenuItem funcionariosItem = new JMenuItem("Funcionários");
        JMenuItem empresasParceirasItem = new JMenuItem("Empresas Parceiras");
        JMenuItem empreendimentosItem = new JMenuItem("Empreendimentos");
        cadastroMenu.add(clientesItem);
        cadastroMenu.add(funcionariosItem);
        cadastroMenu.add(empresasParceirasItem);
        cadastroMenu.add(empreendimentosItem);

        JMenu agendamentoMenu = new JMenu("Agendamento");
        JMenuItem visitasItem = new JMenuItem("Agendar Visitas");
        agendamentoMenu.add(visitasItem);

        JMenu acompanhamentoMenu = new JMenu("Acompanhamento");
        JMenuItem relatoriosItem = new JMenuItem("Relatórios");
        acompanhamentoMenu.add(relatoriosItem);

        menuBar.add(cadastroMenu);
        menuBar.add(agendamentoMenu);
        menuBar.add(acompanhamentoMenu);

        setJMenuBar(menuBar);

        // Card Layout para as telas
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Telas do sistema
        mainPanel.add(new JLabel("Bem-vindo ao CRM! Selecione uma opção no menu."), "home");
        mainPanel.add(new ClientePanel(), "clientes");
        mainPanel.add(new JLabel("Tela de Funcionários (em construção)"), "funcionarios");
        mainPanel.add(new JLabel("Tela de Empresas Parceiras (em construção)"), "empresasParceiras");
        mainPanel.add(new JLabel("Tela de Empreendimentos (em construção)"), "empreendimentos");
        mainPanel.add(new JLabel("Tela de Agendamento (em construção)"), "agendamento");
        mainPanel.add(new JLabel("Tela de Relatórios (em construção)"), "relatorios");

        // Troca de telas ao clicar nos menus
        clientesItem.addActionListener(e -> cardLayout.show(mainPanel, "clientes"));
        funcionariosItem.addActionListener(e -> cardLayout.show(mainPanel, "funcionarios"));
        empresasParceirasItem.addActionListener(e -> cardLayout.show(mainPanel, "empresasParceiras"));
        empreendimentosItem.addActionListener(e -> cardLayout.show(mainPanel, "empreendimentos"));
        visitasItem.addActionListener(e -> cardLayout.show(mainPanel, "agendamento"));
        relatoriosItem.addActionListener(e -> cardLayout.show(mainPanel, "relatorios"));

        add(mainPanel);
    }
}