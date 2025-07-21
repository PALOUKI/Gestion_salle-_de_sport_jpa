package gui_admin.view.clients;

import entite.Client;
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

public class ClientEdit extends GenericEdit<Client> {

    private JTextField idField;
    private JTextField nomField;
    private JTextField prenomField;
    private JDateChooser dateNaissanceChooser;
    private JTextField emailField;

    private JTextField searchField;
    private JButton searchButton;

    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ClientEdit(List<List<Object>> tableData, List<String> columnNames) {
        super(tableData, columnNames);

        idField = new JTextField();
        idField.setEditable(false);
        nomField = new JTextField();
        prenomField = new JTextField();
        dateNaissanceChooser = new JDateChooser();
        dateNaissanceChooser.setDateFormatString("dd/MM/yyyy");
        emailField = new JTextField();

        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 242, 245));

        JLabel globalTitleLabel = new JLabel("Gestion des Clients", SwingConstants.LEFT);
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
                "Détails du Client",
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
        form.add(new JLabel("Date Naissance :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dateNaissanceChooser.setPreferredSize(new Dimension(250, 30));
        form.add(dateNaissanceChooser, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        emailField.setPreferredSize(new Dimension(250, 30));
        form.add(emailField, gbc);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListSectionPanel() {
        JPanel listSectionPanel = new JPanel(new BorderLayout(0, 10));
        listSectionPanel.setBackground(new Color(240, 242, 245));

        JPanel listTitlePanel = new JPanel(new BorderLayout());
        listTitlePanel.setOpaque(false);
        JLabel listTitle = new JLabel("Liste des Clients");
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
        searchPanel.add(new JLabel("Rechercher (Nom/Prénom/Email) :"));
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
            setEntity(new Client());
        }

        Client entite = getEntity();
        entite.setNom(nomField.getText());
        entite.setPrenom(prenomField.getText());
        entite.setEmail(emailField.getText());
        Date dateNaissance = dateNaissanceChooser.getDate();
        if (dateNaissance != null) {
            entite.setDateNaissance(dateNaissance.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } else {
            entite.setDateNaissance(null);
        }
    }

    @Override
    public void initFormWithEntity(Client client) {
        setEntity(client);
        if (client != null) {
            idField.setText(client.getId() != null ? String.valueOf(client.getId()) : "");
            nomField.setText(client.getNom());
            prenomField.setText(client.getPrenom());
            if (client.getDateNaissance() != null) {
                dateNaissanceChooser.setDate(Date.from(client.getDateNaissance().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                dateNaissanceChooser.setDate(null);
            }
            emailField.setText(client.getEmail());
        } else {
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        idField.setText("");
        nomField.setText("");
        prenomField.setText("");
        dateNaissanceChooser.setDate(null);
        emailField.setText("");
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