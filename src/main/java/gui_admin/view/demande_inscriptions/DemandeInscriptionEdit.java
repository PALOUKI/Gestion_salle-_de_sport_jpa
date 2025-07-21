package gui_admin.view.demande_inscriptions;

import entite.DemandeInscription;
import gui_admin.gui_util.ActionButton;
import gui_admin.gui_util.GenericEdit;
import gui_admin.gui_util.StatusCellRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class DemandeInscriptionEdit extends GenericEdit<DemandeInscription> {

    private JTextField idField, nomField, prenomField, emailField;
    private JDateChooser dateDemandeChooser, dateTraitementChooser;
    private JComboBox<String> statutComboBox;

    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    private JTextField searchField;
    private JButton searchButton;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DemandeInscriptionEdit(List<List<Object>> tableData, List<String> columnNames) {
        super(tableData, columnNames);

        idField = createDisabledTextField();
        nomField = createDisabledTextField();
        prenomField = createDisabledTextField();
        emailField = createDisabledTextField();
        dateDemandeChooser = new JDateChooser();
        dateDemandeChooser.setEnabled(false);
        dateTraitementChooser = new JDateChooser();
        statutComboBox = new JComboBox<>(new String[]{"EN_ATTENTE", "VALIDEE", "REJETEE"});

        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        initLayout();
        styleTable();
        clearForm();
    }

    private JTextField createDisabledTextField() {
        JTextField textField = new JTextField();
        textField.setEnabled(false);
        textField.setDisabledTextColor(new Color(50, 50, 50));
        textField.setBackground(new Color(235, 235, 235));
        return textField;
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 242, 245));

        JLabel globalTitleLabel = new JLabel("Gestion des Demandes d'Inscription", SwingConstants.LEFT);
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
                "Détails de la Demande",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Goldman", Font.BOLD, 16),
                new Color(32, 64, 128)
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

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        nomField.setPreferredSize(new Dimension(250, 30));
        form.add(nomField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        prenomField.setPreferredSize(new Dimension(250, 30));
        form.add(prenomField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        emailField.setPreferredSize(new Dimension(250, 30));
        form.add(emailField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Date Demande :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dateDemandeChooser.setPreferredSize(new Dimension(250, 30));
        form.add(dateDemandeChooser, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Date Traitement :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dateTraitementChooser.setPreferredSize(new Dimension(250, 30));
        form.add(dateTraitementChooser, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Statut :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        statutComboBox.setPreferredSize(new Dimension(250, 30));
        form.add(statutComboBox, gbc);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListSectionPanel() {
        JPanel listSectionPanel = new JPanel(new BorderLayout(0, 10));
        listSectionPanel.setBackground(new Color(240, 242, 245));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel listTitleLabel = new JLabel("Liste des Demandes", SwingConstants.LEFT);
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
        searchPanel.add(new JLabel("Rechercher par Email :"));
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

    private void styleTable() {
        JTable table = getCustomTablePanel().getTable();
        table.setRowHeight(30);
        table.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());
    }

    @Override
    public void initEntityFromForm() {
        if (currentEntity == null) {
            setEntity(new DemandeInscription());
        }
        DemandeInscription entite = getEntity();

        entite.setStatut((String) statutComboBox.getSelectedItem());
        Date dateTraitement = dateTraitementChooser.getDate();
        if (dateTraitement != null) {
            entite.setDateDeTraitement(dateTraitement.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
    }

    @Override
    public void initFormWithEntity(DemandeInscription entity) {
        setEntity(entity);
        if (entity != null) {
            idField.setText(entity.getId() != null ? String.valueOf(entity.getId()) : "");
            nomField.setText(entity.getNom());
            prenomField.setText(entity.getPrenom());
            emailField.setText(entity.getEmail());
            if (entity.getDateDeDemande() != null) {
                dateDemandeChooser.setDate(Date.from(entity.getDateDeDemande().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                dateDemandeChooser.setDate(null);
            }
            if (entity.getDateDeTraitement() != null) {
                dateTraitementChooser.setDate(Date.from(entity.getDateDeTraitement().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                dateTraitementChooser.setDate(null);
            }
            statutComboBox.setSelectedItem(entity.getStatut());
        } else {
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        idField.setText("");
        nomField.setText("");
        prenomField.setText("");
        emailField.setText("");
        dateDemandeChooser.setDate(null);
        dateTraitementChooser.setDate(null);
        statutComboBox.setSelectedItem("EN_ATTENTE");
        getCustomTablePanel().getTable().clearSelection();
        setEntity(null);
    }

    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }

    public JComboBox<String> getStatutComboBox() {
        return statutComboBox;
    }

    public LocalDateTime getDateTraitement() {
        Date dateTraitement = dateTraitementChooser.getDate();
        if (dateTraitement != null) {
            return dateTraitement.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            return null;
        }
    }

    @Override
    public JButton getModifyButton() {
        return modifyButton;
    }

    @Override
    public JButton getDeleteButton() {
        return deleteButton;
    }
}