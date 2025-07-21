package gui_admin.view.tickets;

import entite.Client;
import entite.Ticket;
import gui_admin.gui_util.ActionButton;
import gui_admin.gui_util.GenericEdit;
import gui_admin.gui_util.PlaceholderComboBoxRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class TicketEdit extends GenericEdit<Ticket> {

    private JTextField idField;
    private JComboBox<Client> clientComboBox;
    private JTextField nbSeancesField;
    private JTextField montantField;

    private JTextField searchField;
    private JButton searchButton;

    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    public TicketEdit(List<List<Object>> tableData, List<String> columnNames, List<Client> allClients) {
        super(tableData, columnNames);

        idField = new JTextField();
        idField.setEditable(false);
        clientComboBox = new JComboBox<>(allClients.toArray(new Client[0]));
        clientComboBox.setRenderer(new PlaceholderComboBoxRenderer<>("Sélectionnez un client..."));
        nbSeancesField = new JTextField();
        montantField = new JTextField();
        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 242, 245));

        JLabel globalTitleLabel = new JLabel("Gestion des Tickets", SwingConstants.LEFT);
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
                "Détails du Ticket", TitledBorder.LEFT, TitledBorder.TOP,
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
        form.add(new JLabel("Client :"), gbc);
        gbc.gridx = 1;
        clientComboBox.setPreferredSize(new Dimension(250, 30));
        form.add(clientComboBox, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Nombre de séances :"), gbc);
        gbc.gridx = 1;
        nbSeancesField.setPreferredSize(new Dimension(250, 30));
        form.add(nbSeancesField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Montant (€) :"), gbc);
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
        JLabel listTitle = new JLabel("Liste des Tickets");
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
        searchPanel.add(new JLabel("Rechercher par Client :"));
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
            setEntity(new Ticket());
        }
        Ticket entite = getEntity();

        Object selectedClient = clientComboBox.getSelectedItem();
        if (selectedClient == null) {
            throw new IllegalArgumentException("Veuillez sélectionner un client.");
        }
        entite.setClient((Client) selectedClient);

        try {
            entite.setNombreDeSeance(Integer.parseInt(nbSeancesField.getText()));
            double montantDouble = Double.parseDouble(montantField.getText().replace(',', '.'));
            entite.setMontant((int) (montantDouble * 100)); // Conversion en centimes
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Le nombre de séances et le montant doivent être des nombres valides.");
        }
    }

    @Override
    public void initFormWithEntity(Ticket ticket) {
        setEntity(ticket);
        if (ticket != null) {
            idField.setText(ticket.getId() != null ? String.valueOf(ticket.getId()) : "");
            clientComboBox.setSelectedItem(ticket.getClient());
            nbSeancesField.setText(String.valueOf(ticket.getNombreDeSeance()));
            montantField.setText(String.valueOf(ticket.getMontant() / 100.0).replace('.', ','));
        } else {
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        idField.setText("");
        clientComboBox.setSelectedItem(null);
        nbSeancesField.setText("");
        montantField.setText("");
        setEntity(null);
        searchField.setText("");
    }

    @Override public JButton getModifyButton() { return this.modifyButton; }
    @Override public JButton getDeleteButton() { return this.deleteButton; }

    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }

    public JComboBox<Client> getClientComboBox() {
        return clientComboBox;
    }
}