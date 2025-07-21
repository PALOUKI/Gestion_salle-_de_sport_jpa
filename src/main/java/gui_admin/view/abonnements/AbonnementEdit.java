package gui_admin.view.abonnements;

import entite.Abonnement;
import entite.Membre;
import entite.TypeAbonnement;
import gui_admin.gui_util.ActionButton;
import gui_admin.gui_util.GenericEdit;
import gui_admin.gui_util.PlaceholderComboBoxRenderer;

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

public class AbonnementEdit extends GenericEdit<Abonnement> {

    private JTextField idField;
    private JComboBox<Membre> membreComboBox;
    private JComboBox<TypeAbonnement> typeAbonnementComboBox;
    private JDateChooser dateDebutChooser;
    private JDateChooser dateFinChooser;

    private JTextField searchField;
    private JButton searchButton;

    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public AbonnementEdit(List<List<Object>> tableData, List<String> columnNames) {
        super(tableData, columnNames);

        idField = new JTextField();
        idField.setEditable(false);

        membreComboBox = new JComboBox<>();
        membreComboBox.setRenderer(new PlaceholderComboBoxRenderer<>("Sélectionnez un membre..."));

        typeAbonnementComboBox = new JComboBox<>();
        typeAbonnementComboBox.setRenderer(new PlaceholderComboBoxRenderer<>("Sélectionnez un type..."));

        dateDebutChooser = new JDateChooser();
        dateDebutChooser.setDateFormatString("dd/MM/yyyy HH:mm");
        dateFinChooser = new JDateChooser();
        dateFinChooser.setDateFormatString("dd/MM/yyyy HH:mm");
        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 242, 245));

        JLabel globalTitleLabel = new JLabel("Gestion des Abonnements", SwingConstants.LEFT);
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
                "Détails de l'Abonnement", TitledBorder.LEFT, TitledBorder.TOP,
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
        form.add(new JLabel("Membre :"), gbc);
        gbc.gridx = 1;
        membreComboBox.setPreferredSize(new Dimension(250, 30));
        form.add(membreComboBox, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Type d'abonnement :"), gbc);
        gbc.gridx = 1;
        typeAbonnementComboBox.setPreferredSize(new Dimension(250, 30));
        form.add(typeAbonnementComboBox, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Date de début :"), gbc);
        gbc.gridx = 1;
        dateDebutChooser.setPreferredSize(new Dimension(250, 30));
        form.add(dateDebutChooser, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Date de fin :"), gbc);
        gbc.gridx = 1;
        dateFinChooser.setPreferredSize(new Dimension(250, 30));
        form.add(dateFinChooser, gbc);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListSectionPanel() {
        JPanel listSectionPanel = new JPanel(new BorderLayout(0, 10));
        listSectionPanel.setBackground(new Color(240, 242, 245));

        JPanel listTitlePanel = new JPanel(new BorderLayout());
        listTitlePanel.setOpaque(false);
        JLabel listTitle = new JLabel("Liste des Abonnements");
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
        searchPanel.add(new JLabel("Rechercher par Membre :"));
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
            setEntity(new Abonnement());
        }
        Abonnement entite = getEntity();

        if (membreComboBox.getSelectedItem() == null) throw new IllegalArgumentException("Veuillez sélectionner un membre.");
        if (typeAbonnementComboBox.getSelectedItem() == null) throw new IllegalArgumentException("Veuillez sélectionner un type d'abonnement.");

        entite.setMembre((Membre) membreComboBox.getSelectedItem());
        entite.setTypeAbonnement((TypeAbonnement) typeAbonnementComboBox.getSelectedItem());

        Date dateDebut = dateDebutChooser.getDate();
        if (dateDebut == null) {
            throw new IllegalArgumentException("La date de début ne peut pas être vide.");
        }
        entite.setDateDebut(dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        Date dateFin = dateFinChooser.getDate();
        if (dateFin != null) {
            if (dateFin.before(dateDebut)) {
                throw new IllegalArgumentException("La date de fin doit être après la date de début.");
            }
            entite.setDateFin(dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } else {
            entite.setDateFin(null); // Permet une date de fin vide
        }
    }

    @Override
    public void initFormWithEntity(Abonnement abonnement) {
        setEntity(abonnement);
        if (abonnement != null) {
            idField.setText(abonnement.getId() != null ? String.valueOf(abonnement.getId()) : "");
            membreComboBox.setSelectedItem(abonnement.getMembre());
            typeAbonnementComboBox.setSelectedItem(abonnement.getTypeAbonnement());
            if (abonnement.getDateDebut() != null) {
                dateDebutChooser.setDate(Date.from(abonnement.getDateDebut().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                dateDebutChooser.setDate(null);
            }
            if (abonnement.getDateFin() != null) {
                dateFinChooser.setDate(Date.from(abonnement.getDateFin().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                dateFinChooser.setDate(null);
            }
        } else {
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        idField.setText("");
        membreComboBox.setSelectedItem(null);
        typeAbonnementComboBox.setSelectedItem(null);
        dateDebutChooser.setDate(null);
        dateFinChooser.setDate(null);
        setEntity(null);
        searchField.setText("");
    }

    public void setMembresInComboBox(List<Membre> membres) {
        membreComboBox.removeAllItems();
        for (Membre membre : membres) {
            membreComboBox.addItem(membre);
        }
        membreComboBox.setSelectedItem(null);
    }

    public void setTypeAbonnementsInComboBox(List<TypeAbonnement> types) {
        typeAbonnementComboBox.removeAllItems();
        for (TypeAbonnement type : types) {
            typeAbonnementComboBox.addItem(type);
        }
        typeAbonnementComboBox.setSelectedItem(null);
    }

    @Override
    public JButton getModifyButton() { return this.modifyButton; }

    @Override
    public JButton getDeleteButton() { return this.deleteButton; }

    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }

    public JComboBox<Membre> getMembreComboBox() {
        return membreComboBox;
    }

    public JComboBox<TypeAbonnement> getTypeAbonnementComboBox() {
        return typeAbonnementComboBox;
    }
}