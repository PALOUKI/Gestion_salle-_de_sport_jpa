package gui_admin.view.seances;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoriqueSeancesPanel extends JPanel {

    private JTextField searchField;
    private JButton searchButton;
    private JTable seancesTable;
    private DefaultTableModel tableModel;
    private JLabel clientInfoLabel;

    public HistoriqueSeancesPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- Panneau de recherche ---
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Rechercher un client par email ou nom"));

        searchField = new JTextField();
        searchButton = new JButton("Rechercher");

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // --- Panneau d'information client ---
        clientInfoLabel = new JLabel("Aucun client sélectionné.");
        clientInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        clientInfoLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // --- Panneau supérieur combiné ---
        JPanel topPanel = new JPanel(new BorderLayout(10,10));
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(clientInfoLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // --- Tableau de l'historique ---
        String[] columnNames = {"Date", "Heure Début", "Heure Fin", "Salle"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        seancesTable = new JTable(tableModel);
        seancesTable.setFillsViewportHeight(true);
        seancesTable.setRowHeight(25);
        seancesTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        seancesTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        add(new JScrollPane(seancesTable), BorderLayout.CENTER);
    }

    // Getters
    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }
    public JTable getSeancesTable() { return seancesTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JLabel getClientInfoLabel() { return clientInfoLabel; }
}
