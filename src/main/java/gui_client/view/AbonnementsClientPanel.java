package gui_client.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AbonnementsClientPanel extends JPanel {
    private JTable abonnementsTable;
    private DefaultTableModel tableModel;
    private JButton retourButton;

    public AbonnementsClientPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Mes Abonnements", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // Tableau
        String[] columnNames = {"Type d'Abonnement", "Date de Début", "Date de Fin", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre les cellules non éditables
            }
        };
        abonnementsTable = new JTable(tableModel);
        abonnementsTable.setFillsViewportHeight(true);
        abonnementsTable.setRowHeight(25);
        abonnementsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(abonnementsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bouton de retour
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        retourButton = new JButton("Retour au Tableau de Bord");
        buttonPanel.add(retourButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Getters
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getRetourButton() {
        return retourButton;
    }
}
