package com.mycompany.agendaeletronica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Main extends JFrame {

    private JTextField txtCpf, txtNome, txtEmail, txtTelefone;
    private JTable tabela;
    private DefaultTableModel modelo;
    private Agenda agenda;

    // Cores suaves
    private final Color VERDE_BARRA = new Color(46, 125, 50);
    private final Color VERDE_CLARO = new Color(232, 245, 233);
    private final Color TEXTO_CLARO = new Color(240, 248, 240);
    private final Color AZUL_TEXTO = new Color(44, 62, 80);

    public Main() {
        agenda = new Agenda();

        setTitle("Agenda Eletrônica");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);
        Font fonteBotao  = new Font("Segoe UI", Font.BOLD, 15);
        Font fonteTitulo = new Font("Segoe UI Semibold", Font.BOLD, 22);

        /* ===== BARRA SUPERIOR ===== */
        JPanel barraTopo = new JPanel();
        barraTopo.setBackground(VERDE_BARRA);
        barraTopo.setPreferredSize(new Dimension(900, 60));
        barraTopo.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("AGENDA ELETRÔNICA", SwingConstants.CENTER);
        lblTitulo.setFont(fonteTitulo);
        lblTitulo.setForeground(TEXTO_CLARO);
        barraTopo.add(lblTitulo, BorderLayout.CENTER);

        /* ===== CAMPOS ===== */
        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 15, 15));
        painelCampos.setBackground(VERDE_CLARO);
        painelCampos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(VERDE_BARRA, 2),
                "Dados do Contato",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                AZUL_TEXTO
        ));

        txtCpf = criarCampo("CPF (11 dígitos):", painelCampos, fontePadrao);
        txtNome = criarCampo("Nome:", painelCampos, fontePadrao);
        txtEmail = criarCampo("E-mail:", painelCampos, fontePadrao);
        txtTelefone = criarCampo("Telefone:", painelCampos, fontePadrao);

        /* ===== BOTÕES ===== */
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        painelBotoes.setBackground(VERDE_CLARO);

        JButton btnCadastrar = criarBotao("CADASTRAR", fonteBotao);
        JButton btnBuscar    = criarBotao("BUSCAR", fonteBotao);
        JButton btnExibir    = criarBotao("EXIBIR TODOS", fonteBotao);
        JButton btnExcluir   = criarBotao("EXCLUIR", fonteBotao);

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnExibir);
        painelBotoes.add(btnExcluir);

        /* ===== TABELA ===== */
        modelo = new DefaultTableModel(
                new Object[]{"CPF", "Nome", "E-mail", "Telefone"}, 0
        );

        tabela = new JTable(modelo);
        tabela.setRowHeight(28);
        tabela.setFont(fontePadrao);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(VERDE_BARRA);
        tabela.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(VERDE_BARRA, 2),
                "Contatos Cadastrados",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                AZUL_TEXTO
        ));

        /* ===== PRINCIPAL ===== */
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBackground(VERDE_CLARO);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelPrincipal.add(painelCampos);
        painelPrincipal.add(Box.createVerticalStrut(10));
        painelPrincipal.add(painelBotoes);
        painelPrincipal.add(Box.createVerticalStrut(10));
        painelPrincipal.add(scroll);

        setLayout(new BorderLayout());
        add(barraTopo, BorderLayout.NORTH);
        add(painelPrincipal, BorderLayout.CENTER);

        /* ===== AÇÕES ===== */
        btnCadastrar.addActionListener(e -> cadastrarContato());
        btnBuscar.addActionListener(e -> buscarContato());
        btnExibir.addActionListener(e -> exibirTodos());
        btnExcluir.addActionListener(e -> excluirContato());
    }

    /* ===== MÉTODOS ===== */

    private void excluirContato() {
        modelo.setRowCount(0);
        Contato contato = agenda.buscarContato(txtCpf.getText());

        if (contato == null) {
            JOptionPane.showMessageDialog(this, "Contato não cadastrado.");
            return;
        }

        modelo.addRow(new Object[]{
                contato.getCpf(),
                contato.getNome(),
                contato.getEmail(),
                contato.getTelefone()
        });

        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este contato?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            agenda.excluirContato(contato.getCpf());
            modelo.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Contato excluído com sucesso.");
            limparCampos();
        }
    }

    private void cadastrarContato() {
        try {
            if (agenda.buscarContato(txtCpf.getText()) != null) {
                JOptionPane.showMessageDialog(this, "CPF já cadastrado!");
                return;
            }

            Contato c = new Contato(
                    txtCpf.getText(),
                    txtNome.getText(),
                    txtEmail.getText(),
                    txtTelefone.getText()
            );

            agenda.cadastrar(c);
            JOptionPane.showMessageDialog(this, "Contato cadastrado com sucesso!");
            limparCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void buscarContato() {
        modelo.setRowCount(0);
        Contato c = agenda.buscarContato(txtCpf.getText());

        if (c != null) {
            modelo.addRow(new Object[]{
                    c.getCpf(),
                    c.getNome(),
                    c.getEmail(),
                    c.getTelefone()
            });
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Contato não encontrado.");
        }
    }

    private void exibirTodos() {
        modelo.setRowCount(0);
        for (Contato c : agenda.getContatos()) {
            modelo.addRow(new Object[]{
                    c.getCpf(),
                    c.getNome(),
                    c.getEmail(),
                    c.getTelefone()
            });
        }
    }

    private JButton criarBotao(String texto, Font fonte) {
        JButton b = new JButton(texto);
        b.setFont(fonte);
        b.setBackground(Color.WHITE);
        b.setForeground(AZUL_TEXTO);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(VERDE_BARRA, 2));
        return b;
    }

    private JTextField criarCampo(String rotulo, JPanel painel, Font fonte) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(VERDE_CLARO);

        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(fonte);
        lbl.setForeground(AZUL_TEXTO);

        JTextField campo = new JTextField();
        campo.setFont(fonte);

        p.add(lbl, BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        painel.add(p);

        return campo;
    }

    private void limparCampos() {
        txtCpf.setText("");
        txtNome.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
