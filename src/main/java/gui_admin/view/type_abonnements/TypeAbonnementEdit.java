package gui_admin.view.type_abonnements;

import entite.TypeAbonnement;
import gui_admin.gui_util.ActionButton;
import gui_admin.gui_util.GenericEdit;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class TypeAbonnementEdit extends GenericEdit<TypeAbonnement> {

    private JTextField codeField;
    private JTextField libelleField;
    private JTextField montantField;

    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton = new ActionButton("Rafraîchir", ActionButton.ButtonType.REFRESH);

    private JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);


    public TypeAbonnementEdit(List<List<Object>> tableData, List<String> columnNames) {
        super(tableData, columnNames);

        // Renderer pour la colonne Montant
        int montantColumnIndex = 2; // "Montant" est à l'index 2
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

        codeField = new JTextField();
        libelleField = new JTextField();
        montantField = new JTextField();

        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        setupMainLayout();
    }

    public TypeAbonnementEdit(TypeAbonnement entite, List<List<Object>> tableData, List<String> columnNames) {
        this(tableData, columnNames);
        setEntity(entite);
        initFormWithEntity(entite);
    }

    private void setupMainLayout() {
        this.setLayout(new BorderLayout(10, 10)); // Marge et espacement entre les composants principaux
        this.setBorder(new EmptyBorder(10, 10, 10, 10)); // Marge extérieure du panneau Edit
        this.setBackground(new Color(240, 242, 245)); // Couleur de fond générale du Edit Panel

        // Titre principal global pour toute l'interface de gestion des abonnements
        JLabel globalTitleLabel = new JLabel("Gestion des Types d'Abonnements", SwingConstants.LEFT);
        globalTitleLabel.setFont(new Font("Goldman", Font.BOLD, 25)); // Plus grand et plus imposant
        globalTitleLabel.setForeground(new Color(32, 64, 128));
        globalTitleLabel.setBorder(new EmptyBorder(10, 0, 20, 0)); // Espacement en bas
        this.add(globalTitleLabel, BorderLayout.NORTH);

        // Créer un panneau central pour les détails et la liste/recherche
        // Ce panneau utilisera un BorderLayout vertical
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(240, 242, 245)); // Même fond que le parent

        // 1. Panneau des Détails (Formulaire uniquement)
        JPanel detailsPanel = createDetailsPanel();
        contentPanel.add(detailsPanel, BorderLayout.NORTH); // Positionné en haut du contentPanel

        // 2. Panneau pour "Liste des types abonnements" et "Recherche et Tableau"
        JPanel listSectionPanel = new JPanel(new BorderLayout(0, 10)); // Espacement vertical
        listSectionPanel.setBackground(new Color(240, 242, 245));

        JPanel listTitlePanel = new JPanel(new BorderLayout());
        listTitlePanel.setOpaque(false);
        JLabel listTitleLabel = new JLabel("Liste des types abonnements", SwingConstants.LEFT); // Aligné à gauche
        listTitleLabel.setFont(new Font("Goldman", Font.BOLD, 20)); // Taille appropriée
        listTitleLabel.setForeground(new Color(32, 64, 128));
        listTitlePanel.add(listTitleLabel, BorderLayout.WEST);
        listTitlePanel.add(getRefreshButton(), BorderLayout.EAST);

        JPanel searchAndTablePanel = createSearchAndTablePanel();
        listSectionPanel.add(listTitlePanel, BorderLayout.NORTH);
        listSectionPanel.add(searchAndTablePanel, BorderLayout.CENTER);

        contentPanel.add(listSectionPanel, BorderLayout.CENTER); // listSectionPanel au centre du contentPanel

        this.add(contentPanel, BorderLayout.CENTER); // contentPanel au centre du Edit panel

        // 3. Panneau des Boutons d'Action Globaux (Modifier, Supprimer, Enregistrer, Annuler)
        JPanel globalActionButtonsPanel = createGlobalActionButtonsPanel();
        this.add(globalActionButtonsPanel, BorderLayout.SOUTH); // Positionné en bas du Edit panel
    }

    private JPanel createDetailsPanel() {
        // Le panneau du détail n'a plus le titre "Gestion des Types d'Abonnements"
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE); // Couleur de fond du cadre du détail
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        ));

        // Panneau des champs du formulaire (form est le JPanel hérité de GenericEdit)
        form.setLayout(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(32, 64, 128), 2),
                "Détails du Type d'Abonnement", // Titre du cadre du détail
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Goldman", Font.BOLD, 16),
                new Color(32, 64, 128)
        ));
        GridBagConstraints gbcFields = new GridBagConstraints();
        gbcFields.insets = new Insets(5, 5, 5, 5);
        gbcFields.fill = GridBagConstraints.HORIZONTAL;

        int fieldRow = 0;
        gbcFields.gridx = 0; gbcFields.gridy = fieldRow; gbcFields.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Code :"), gbcFields);
        gbcFields.gridx = 1; gbcFields.anchor = GridBagConstraints.WEST; gbcFields.weightx = 1.0;
        codeField.setPreferredSize(new Dimension(250, 30));
        form.add(codeField, gbcFields);
        fieldRow++;

        gbcFields.gridx = 0; gbcFields.gridy = fieldRow; gbcFields.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Libellé :"), gbcFields);
        gbcFields.gridx = 1; gbcFields.anchor = GridBagConstraints.WEST; gbcFields.weightx = 1.0;
        libelleField.setPreferredSize(new Dimension(250, 30));
        form.add(libelleField, gbcFields);
        fieldRow++;

        gbcFields.gridx = 0; gbcFields.gridy = fieldRow; gbcFields.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Montant :"), gbcFields);
        gbcFields.gridx = 1; gbcFields.anchor = GridBagConstraints.WEST; gbcFields.weightx = 1.0;
        montantField.setPreferredSize(new Dimension(250, 30));
        form.add(montantField, gbcFields);
        fieldRow++;

        panel.add(form, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchAndTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE); // Couleur de fond du cadre du tableau
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Panneau de recherche
        JPanel searchControlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchControlsPanel.setBackground(Color.WHITE); // Couleur de fond de la barre de recherche
        searchControlsPanel.add(new JLabel("Rechercher (Code/Libellé) :"));
        searchField.setPreferredSize(new Dimension(200, 30));
        searchControlsPanel.add(searchField);
        searchControlsPanel.add(searchButton);
        panel.add(searchControlsPanel, BorderLayout.NORTH);

        // Tableau (customTablePanel est hérité de GenericEdit)
        panel.add(this.customTablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGlobalActionButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Aligne à droite
        // Supprime le fond blanc pour laisser transparaître le fond de la fenêtre principale
        panel.setBackground(new Color(240, 242, 245)); // Ou laissez-le null si l'arrière-plan de la fenêtre est défini par le parent
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Marge verticale pour séparer du bas

        panel.add(this.modifyButton);
        panel.add(this.deleteButton);
        panel.add(this.buttonPanel.getSaveButton());
        panel.add(this.buttonPanel.getCancelButton());

        return panel;
    }

    @Override
    public void initEntityFromForm() {
        if (currentEntity == null) {
            setEntity(new TypeAbonnement());
        }

        TypeAbonnement entite = (TypeAbonnement) getEntity();
        entite.setCode(codeField.getText());
        entite.setLibelle(libelleField.getText());
        try {
            entite.setMontant(Integer.parseInt(montantField.getText()));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Le montant doit être un nombre entier valide.");
        }
    }

    @Override
    public void initFormWithEntity(TypeAbonnement typeAbonnement) {
        if (typeAbonnement != null) {
            codeField.setText(typeAbonnement.getCode());
            libelleField.setText(typeAbonnement.getLibelle());
            montantField.setText(String.valueOf(typeAbonnement.getMontant()));
            codeField.setEnabled(false); // Disable code for modification
        } else {
            System.err.println("initFormWithEntity: L'objet TypeAbonnement passé est null. Nettoyage du formulaire.");
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        codeField.setText("");
        libelleField.setText("");
        montantField.setText("");
        codeField.setEnabled(true); // Re-enable code for new entry
        setEntity(null);
        searchField.setText("");
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

    public JButton getRefreshButton() {
        return this.refreshButton;
    }
}