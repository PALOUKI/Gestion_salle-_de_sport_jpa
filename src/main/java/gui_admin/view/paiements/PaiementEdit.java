package gui_admin.view.paiements;

import entite.Abonnement;
import entite.MoyenDePaiement;
import entite.Paiement;
import gui_admin.gui_util.ActionButton;
import gui_admin.gui_util.GenericEdit;
import gui_admin.gui_util.PlaceholderComboBoxRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class PaiementEdit extends GenericEdit<Paiement> {

    private JTextField idField;
    private JComboBox<Abonnement> abonnementComboBox;
    private JComboBox<MoyenDePaiement> moyenDePaiementComboBox;
    private JDateChooser datePaiementChooser;
    private JTextField montantField;

    private JTextField searchField;
    private JButton searchButton;

    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PaiementEdit(List<List<Object>> tableData, List<String> columnNames, List<Abonnement> allAbonnements, List<MoyenDePaiement> allMoyens) {
        super(tableData, columnNames);

        // Renderer pour la colonne Montant
        int montantColumnIndex = 3; // "Montant" est à l'index 3
        getCustomTablePanel().getTable().getColumnModel().getColumn(montantColumnIndex).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Number) {
                    setText(String.format("%,.0f FCFA", ((Number) value).doubleValue()));
                    setHorizontalAlignment(SwingConstants.RIGHT);
                }
                return c;
            }
        });

        idField = new JTextField();
        idField.setEditable(false);

        abonnementComboBox = new JComboBox<>(allAbonnements.toArray(new Abonnement[0]));
        abonnementComboBox.setRenderer(new PlaceholderComboBoxRenderer<>("Sélectionnez un abonnement..."));

        moyenDePaiementComboBox = new JComboBox<>(allMoyens.toArray(new MoyenDePaiement[0]));
        moyenDePaiementComboBox.setRenderer(new PlaceholderComboBoxRenderer<>("Sélectionnez un moyen de paiement..."));

        datePaiementChooser = new JDateChooser();
        datePaiementChooser.setDateFormatString("dd/MM/yyyy HH:mm");
        montantField = new JTextField();

        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 242, 245));

        JLabel globalTitleLabel = new JLabel("Gestion des Paiements", SwingConstants.LEFT);
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
                "Détails du Paiement", TitledBorder.LEFT, TitledBorder.TOP,
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
        form.add(new JLabel("Abonnement concerné :"), gbc);
        gbc.gridx = 1;
        abonnementComboBox.setPreferredSize(new Dimension(250, 30));
        form.add(abonnementComboBox, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Moyen de Paiement :"), gbc);
        gbc.gridx = 1;
        moyenDePaiementComboBox.setPreferredSize(new Dimension(250, 30));
        form.add(moyenDePaiementComboBox, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Date de paiement :"), gbc);
        gbc.gridx = 1;
        datePaiementChooser.setPreferredSize(new Dimension(250, 30));
        form.add(datePaiementChooser, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Montant (FCFA) :"), gbc);
        gbc.gridx = 1;
        montantField.setPreferredSize(new Dimension(250, 30));
        form.add(montantField, gbc);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListSectionPanel() {
        JPanel listSectionPanel = new JPanel(new BorderLayout(0, 10));
        listSectionPanel.setBackground(new Color(240, 242, 245));

        JPanel listTitlePanel = new JPanel(new BorderLayout());
        listTitlePanel.setOpaque(false);
        JLabel listTitle = new JLabel("Liste des Paiements");
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
            setEntity(new Paiement());
        }
        Paiement entite = getEntity();

        if (abonnementComboBox.getSelectedItem() == null) throw new IllegalArgumentException("Veuillez sélectionner un abonnement.");
        if (moyenDePaiementComboBox.getSelectedItem() == null) throw new IllegalArgumentException("Veuillez sélectionner un moyen de paiement.");

        entite.setAbonnement((Abonnement) abonnementComboBox.getSelectedItem());
        entite.setMoyenDePaiement((MoyenDePaiement) moyenDePaiementComboBox.getSelectedItem());

        try {
            Date datePaiement = datePaiementChooser.getDate();
            if (datePaiement == null) {
                throw new IllegalArgumentException("La date de paiement ne peut pas être vide.");
            }
            entite.setDateDePaiement(datePaiement.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            double montantDouble = Double.parseDouble(montantField.getText().replace(',', '.'));
            entite.setMontant((int) (montantDouble * 100)); // Conversion en centimes
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Le montant doit être un nombre valide.");
        }
    }

    @Override
    public void initFormWithEntity(Paiement paiement) {
        setEntity(paiement);
        if (paiement != null) {
            idField.setText(paiement.getId() != null ? String.valueOf(paiement.getId()) : "");
            abonnementComboBox.setSelectedItem(paiement.getAbonnement());
            moyenDePaiementComboBox.setSelectedItem(paiement.getMoyenDePaiement());
            if (paiement.getDateDePaiement() != null) {
                datePaiementChooser.setDate(Date.from(paiement.getDateDePaiement().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                datePaiementChooser.setDate(null);
            }
            montantField.setText(paiement.getMontant() != null ? String.valueOf(paiement.getMontant() / 100.0).replace('.', ',') : ""); // Conversion de centimes en euros
        } else {
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        idField.setText("");
        abonnementComboBox.setSelectedItem(null);
        moyenDePaiementComboBox.setSelectedItem(null);
        datePaiementChooser.setDate(null);
        montantField.setText("");
        setEntity(null);
    }

    @Override
    public JButton getModifyButton() { return this.modifyButton; }

    @Override
    public JButton getDeleteButton() { return this.deleteButton; }

    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }

    public JComboBox<Abonnement> getAbonnementComboBox() {
        return abonnementComboBox;
    }

    public JComboBox<MoyenDePaiement> getMoyenDePaiementComboBox() {
        return moyenDePaiementComboBox;
    }
}