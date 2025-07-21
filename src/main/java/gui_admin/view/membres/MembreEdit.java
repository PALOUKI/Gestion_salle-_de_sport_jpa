package gui_admin.view.membres;

import entite.Client;
import entite.Membre;
import gui_admin.gui_util.ActionButton;
import gui_admin.gui_util.GenericEdit;

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

public class MembreEdit extends GenericEdit<Membre> {

    private JTextField idField;
    private JComboBox<Client> clientComboBox;
    private JDateChooser dateInscriptionChooser;

    private JTextField searchField;
    private JButton searchButton;

    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public MembreEdit(List<List<Object>> tableData, List<String> columnNames) {
        super(tableData, columnNames);

        idField = new JTextField();
        idField.setEditable(false);
        clientComboBox = new JComboBox<>();
        dateInscriptionChooser = new JDateChooser();
        dateInscriptionChooser.setDateFormatString("dd/MM/yyyy HH:mm");

        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 242, 245));

        JLabel globalTitleLabel = new JLabel("Gestion des Membres", SwingConstants.LEFT);
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
                "Détails du Membre",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Goldman", Font.BOLD, 16),
                new Color(32, 64, 128)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        form.add(new JLabel("ID :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0;
        idField.setPreferredSize(new Dimension(250, 30));
        form.add(idField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        form.add(new JLabel("Client :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0;
        clientComboBox.setPreferredSize(new Dimension(250, 30));
        form.add(clientComboBox, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        form.add(new JLabel("Date Inscription :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0;
        dateInscriptionChooser.setPreferredSize(new Dimension(250, 30));
        form.add(dateInscriptionChooser, gbc);
        row++;

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListSectionPanel() {
        JPanel listSectionPanel = new JPanel(new BorderLayout(0, 10));
        listSectionPanel.setBackground(new Color(240, 242, 245));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel listTitleLabel = new JLabel("Liste des membres", SwingConstants.LEFT);
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
        searchPanel.add(new JLabel("Rechercher (Nom/Prénom) :"));
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
            setEntity(new Membre());
        }

        Membre entite = getEntity();
        entite.setClient((Client) clientComboBox.getSelectedItem());
        Date date = dateInscriptionChooser.getDate();
        if (date != null) {
            entite.setDateInscription(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } else {
            entite.setDateInscription(null);
        }
    }

    @Override
    public void initFormWithEntity(Membre membre) {
        setEntity(membre);
        if (membre != null) {
            idField.setText(membre.getId() != null ? String.valueOf(membre.getId()) : "");
            clientComboBox.setSelectedItem(membre.getClient());
            if (membre.getDateInscription() != null) {
                dateInscriptionChooser.setDate(Date.from(membre.getDateInscription().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                dateInscriptionChooser.setDate(null);
            }
        } else {
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        setEntity(null);
        idField.setText("");
        clientComboBox.setSelectedItem(null);
        dateInscriptionChooser.setDate(null);
    }

    public void setClientsInComboBox(List<Client> clients) {
        clientComboBox.removeAllItems();
        for (Client client : clients) {
            clientComboBox.addItem(client);
        }
    }

    public JComboBox<Client> getClientComboBox() {
        return clientComboBox;
    }

    @Override
    public JButton getModifyButton() {
        return modifyButton;
    }

    @Override
    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }
}