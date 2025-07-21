package gui_admin.view.moyen_de_paiements;

import entite.MoyenDePaiement;
import gui_admin.gui_util.ActionButton;
import gui_admin.gui_util.GenericEdit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class MoyenDePaiementEdit extends GenericEdit<MoyenDePaiement> {

    private JTextField codeField;
    private JTextField libelleField;

    private JTextField searchField;
    private JButton searchButton;

    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    public MoyenDePaiementEdit(List<List<Object>> tableData, List<String> columnNames) {
        super(tableData, columnNames);

        codeField = new JTextField();
        libelleField = new JTextField();

        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 242, 245));

        JLabel globalTitleLabel = new JLabel("Gestion des Moyens de Paiement", SwingConstants.LEFT);
        globalTitleLabel.setFont(new Font("Goldman", Font.BOLD, 25));
        globalTitleLabel.setForeground(new Color(32, 64, 128));
        globalTitleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(globalTitleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(240, 242, 245));
        contentPanel.add(createDetailsPanel(), BorderLayout.NORTH);
        contentPanel.add(createListSectionPanel(), BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
        add(createGlobalActionButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        ));

        form.setLayout(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(32, 64, 128), 2),
                "Détails du Moyen de Paiement", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Goldman", Font.BOLD, 16), new Color(32, 64, 128)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Code :"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        codeField.setPreferredSize(new Dimension(250, 30));
        form.add(codeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Libellé :"), gbc);
        gbc.gridx = 1;
        libelleField.setPreferredSize(new Dimension(250, 30));
        form.add(libelleField, gbc);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListSectionPanel() {
        JPanel listSectionPanel = new JPanel(new BorderLayout(0, 10));
        listSectionPanel.setBackground(new Color(240, 242, 245));

        JPanel listTitlePanel = new JPanel(new BorderLayout());
        listTitlePanel.setOpaque(false);
        JLabel listTitle = new JLabel("Liste des Moyens de Paiement");
        listTitle.setFont(new Font("Goldman", Font.BOLD, 20));
        listTitle.setForeground(new Color(32, 64, 128));
        listTitlePanel.add(listTitle, BorderLayout.WEST);
        listTitlePanel.add(getRefreshButton(), BorderLayout.EAST);

        listSectionPanel.add(listTitlePanel, BorderLayout.NORTH);
        listSectionPanel.add(createSearchAndTablePanel(), BorderLayout.CENTER);

        return listSectionPanel;
    }

    private JPanel createSearchAndTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Rechercher (Code/Libellé) :"));
        searchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(getCustomTablePanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGlobalActionButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panel.add(getModifyButton());
        panel.add(getDeleteButton());
        panel.add(getSaveButton());
        panel.add(getCancelButton());

        return panel;
    }

    @Override
    public void initEntityFromForm() {
        if (currentEntity == null) {
            setEntity(new MoyenDePaiement());
        }
        MoyenDePaiement entite = getEntity();
        entite.setCode(codeField.getText());
        entite.setLibelle(libelleField.getText());
    }

    @Override
    public void initFormWithEntity(MoyenDePaiement mdp) {
        setEntity(mdp);
        if (mdp != null) {
            codeField.setText(mdp.getCode());
            libelleField.setText(mdp.getLibelle());
            codeField.setEnabled(false); // Le code ne doit pas être modifié
        } else {
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        codeField.setText("");
        libelleField.setText("");
        codeField.setEnabled(true); // Réactiver le champ code pour une nouvelle entrée
        setEntity(null);
        searchField.setText("");
    }

    @Override
    public JButton getModifyButton() { return this.modifyButton; }

    @Override
    public JButton getDeleteButton() { return this.deleteButton; }

    public JTextField getSearchField() { return searchField; }

    public JButton getSearchButton() { return searchButton; }
}