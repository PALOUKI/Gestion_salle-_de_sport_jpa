package gui_admin.view.equipements;

import entite.Equipement;
import entite.Salle;
import gui_admin.gui_util.ActionButton;
import gui_admin.gui_util.GenericEdit;
import gui_admin.gui_util.PlaceholderComboBoxRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class EquipementEdit extends GenericEdit<Equipement> {

    private JTextField idField;
    private JTextField libelleField;
    private JTextArea descriptionArea;
    private JComboBox<Salle> salleComboBox;

    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    private JTextField searchField;
    private JButton searchButton;

    public EquipementEdit(List<List<Object>> tableData, List<String> columnNames, List<Salle> allSalles) {
        super(tableData, columnNames);

        idField = new JTextField();
        idField.setEditable(false);
        libelleField = new JTextField();
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        salleComboBox = new JComboBox<>(allSalles.toArray(new Salle[0]));
        salleComboBox.setRenderer(new PlaceholderComboBoxRenderer<>("Sélectionnez une salle..."));
        salleComboBox.setSelectedItem(null);

        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 242, 245));

        JLabel globalTitleLabel = new JLabel("Gestion des Équipements", SwingConstants.LEFT);
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
                "Détails de l'Équipement", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Goldman", Font.BOLD, 16), new Color(32, 64, 128)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("ID :"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        idField.setPreferredSize(new Dimension(250, 30));
        form.add(idField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Libellé :"), gbc);
        gbc.gridx = 1;
        libelleField.setPreferredSize(new Dimension(250, 30));
        form.add(libelleField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Salle :"), gbc);
        gbc.gridx = 1;
        salleComboBox.setPreferredSize(new Dimension(250, 30));
        form.add(salleComboBox, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHEAST;
        form.add(new JLabel("Description :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        form.add(new JScrollPane(descriptionArea), gbc);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListSectionPanel() {
        JPanel listSectionPanel = new JPanel(new BorderLayout(0, 10));
        listSectionPanel.setBackground(new Color(240, 242, 245));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel listTitleLabel = new JLabel("Liste des Équipements", SwingConstants.LEFT);
        listTitleLabel.setFont(new Font("Goldman", Font.BOLD, 20));
        listTitleLabel.setForeground(new Color(32, 64, 128));
        titlePanel.add(listTitleLabel, BorderLayout.WEST);
        titlePanel.add(getRefreshButton(), BorderLayout.EAST);

        listSectionPanel.add(titlePanel, BorderLayout.NORTH);
        listSectionPanel.add(createSearchAndTablePanel(), BorderLayout.CENTER);

        return listSectionPanel;
    }

    private JPanel createSearchAndTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Rechercher (Libellé/Description) :"));
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
            setEntity(new Equipement());
        }
        Equipement entite = getEntity();

        Object selectedSalle = salleComboBox.getSelectedItem();
        if (selectedSalle == null || !(selectedSalle instanceof Salle)) {
            throw new IllegalArgumentException("Veuillez sélectionner une salle valide.");
        }
        entite.setSalle((Salle) selectedSalle);
        entite.setLibelle(libelleField.getText());
        entite.setDescription(descriptionArea.getText());
    }

    @Override
    public void initFormWithEntity(Equipement equipement) {
        setEntity(equipement);
        if (equipement != null) {
            idField.setText(equipement.getId() != null ? String.valueOf(equipement.getId()) : "");
            libelleField.setText(equipement.getLibelle());
            descriptionArea.setText(equipement.getDescription());
            salleComboBox.setSelectedItem(equipement.getSalle());
        } else {
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        idField.setText("");
        libelleField.setText("");
        descriptionArea.setText("");
        salleComboBox.setSelectedItem(null);
        setEntity(null);
        searchField.setText("");
    }

    @Override public JButton getModifyButton() { return this.modifyButton; }
    @Override public JButton getDeleteButton() { return this.deleteButton; }
    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }
}