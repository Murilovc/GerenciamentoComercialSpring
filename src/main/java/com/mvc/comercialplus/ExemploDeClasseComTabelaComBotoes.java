package com.mvc.comercialplus;
/*
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class Cardapio extends JFrame {

    private static final long serialVersionUID = 4434690587649948410L;
    private JPanel contentPane;
    private JTable lanches;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Cardapio frame = new Cardapio();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Cardapio() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        JTable lanches = getLanches();
        contentPane.add(lanches, BorderLayout.CENTER);

        adicioneOsLanchesDisponiveisNessaTabela(lanches);
        adicioneBotaoDeVerPrecoNaUltimaColunaDessaTabela(lanches);
    }

    private void adicioneOsLanchesDisponiveisNessaTabela(JTable lanches) {
        lanches.setModel(new LanchesDisponiveis());
    }

    private static void adicioneBotaoDeVerPrecoNaUltimaColunaDessaTabela(
            JTable tabela) {
        tabela.setDefaultRenderer(Lanche.class, VerPrecoRenderer.getInstanciaUnica());
        VerPrecoListener tabListener = new VerPrecoListener(tabela);
        tabela.addKeyListener(tabListener);
        tabela.addMouseListener(tabListener);
    }

    private JTable getLanches() {
        if (lanches == null) {
            lanches = new JTable();
            lanches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        return lanches;
    }

    private static class VerPrecoRenderer extends JButton implements TableCellRenderer {

        private static final long serialVersionUID = -3686680462801073538L;
        
        private VerPrecoRenderer() {
        }

        public static TableCellRenderer getInstanciaUnica() {
            return new VerPrecoRenderer();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            return new JButton("Ver preço");
        }
        
    }
    
    private static class VerPrecoListener extends MouseAdapter implements
            KeyListener {

        private JTable lanches;

        public VerPrecoListener(JTable lanches) {
            this.lanches = lanches;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                exibaPrecoDoLancheSelecionado();
            }
        }

        private void exibaPrecoDoLancheSelecionado() {
            int linhaSelecionada = lanches.getSelectedRow();
            if (linhaSelecionada == -1) {
                return;
            }

            int colunaSelecionada = lanches.getSelectedColumn();
            if (colunaSelecionada == -1) {
                return;
            }

            Lanche lanche = (Lanche) lanches.getValueAt(linhaSelecionada,
                    colunaSelecionada);
            JOptionPane.showMessageDialog(null, "Lanche: " + lanche.descricao
                    + "\r\nPreço: " + lanche.preco);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            exibaPrecoDoLancheSelecionado();
        }

        @Override
        public void keyTyped(KeyEvent e) { }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }

    private static class Lanche {

        public String descricao;
        public BigDecimal preco;

        public Lanche(String descricao, double preco) {
            this.descricao = descricao;
            this.preco = BigDecimal.valueOf(preco);
        }

    }

    private static class LanchesDisponiveis extends AbstractTableModel {

        private static final long serialVersionUID = -8828829829157781777L;

        private static final Lanche[] LANCHES = new Lanche[] {
                new Lanche("X salada", 8.50), new Lanche("Hamburguer", 6.99),
                new Lanche("Mini pizza", 4.25) };

        @Override
        public int getRowCount() {
            return LANCHES.length;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Lanche lanche = LANCHES[rowIndex];
            return columnIndex == 0 ? lanche.descricao : lanche;
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

    }

}
*/