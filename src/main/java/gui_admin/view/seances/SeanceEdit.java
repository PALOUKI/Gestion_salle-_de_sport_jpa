package gui_admin.view.seances;

import entite.Salle;
import entite.Seance;
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

public class SeanceEdit extends GenericEdit<Seance> {

    private JTextField idField;
    private JComboBox<Salle> salleComboBox;
    private JDateChooser dateDebutChooser;
    private JDateChooser dateFinChooser;

    private JTextField searchField;
    private JButton searchButton;

    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SeanceEdit(List<List<Object>> tableData, List<String> columnNames) {
        super(tableData, columnNames);

        idField = new JTextField();
        idField.setEditable(false);
        salleComboBox = new JComboBox<>();
        salleComboBox.setRenderer(new PlaceholderComboBoxRenderer<>("Sélectionnez une salle..."));
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

        JLabel globalTitleLabel = new JLabel("Gestion des Séances", SwingConstants.LEFT);
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
                "Détails de la Séance", TitledBorder.LEFT, TitledBorder.TOP,
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
        form.add(new JLabel("Salle :"), gbc);
        gbc.gridx = 1;
        salleComboBox.setPreferredSize(new Dimension(250, 30));
        form.add(salleComboBox, gbc);
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

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel listTitle = new JLabel("Liste des Séances");
        listTitle.setFont(new Font("Goldman", Font.BOLD, 20));
        listTitle.setForeground(new Color(32, 64, 128));
        titlePanel.add(listTitle, BorderLayout.WEST);
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
        searchPanel.add(new JLabel("Rechercher par Salle :"));
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
            setEntity(new Seance());
        }
        Seance entite = getEntity();

        Object selectedSalle = salleComboBox.getSelectedItem();
        if (selectedSalle == null || !(selectedSalle instanceof Salle)) {
            throw new IllegalArgumentException("Veuillez sélectionner une salle valide.");
        }
        entite.setSalle((Salle) selectedSalle);

        Date dateDebut = dateDebutChooser.getDate();
        Date dateFin = dateFinChooser.getDate();

        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates de début et de fin ne peuvent pas être vides.");
        }

        LocalDateTime ldtDebut = dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime ldtFin = dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (ldtFin.isBefore(ldtDebut) || ldtFin.isEqual(ldtDebut)) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }

        entite.setDateDebut(ldtDebut);
        entite.setDateFin(ldtFin);
    }

    @Override
    public void initFormWithEntity(Seance seance) {
        setEntity(seance);
        if (seance != null) {
            idField.setText(seance.getId() != null ? String.valueOf(seance.getId()) : "");
            salleComboBox.setSelectedItem(seance.getSalle());
            if (seance.getDateDebut() != null) {
                dateDebutChooser.setDate(Date.from(seance.getDateDebut().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                dateDebutChooser.setDate(null);
            }
            if (seance.getDateFin() != null) {
                dateFinChooser.setDate(Date.from(seance.getDateFin().atZone(ZoneId.systemDefault()).toInstant()));
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
        salleComboBox.setSelectedItem(null);
        dateDebutChooser.setDate(null);
        dateFinChooser.setDate(null);
        setEntity(null);
        searchField.setText("");
    }

    public void setSallesInComboBox(List<Salle> salles) {
        salleComboBox.removeAllItems();
        for (Salle salle : salles) {
            salleComboBox.addItem(salle);
        }
        salleComboBox.setSelectedItem(null);
    }

    @Override
    public JButton getModifyButton() {
        return this.modifyButton;
    }

    @Override
    public JButton getDeleteButton() {
        return this.deleteButton;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }
}