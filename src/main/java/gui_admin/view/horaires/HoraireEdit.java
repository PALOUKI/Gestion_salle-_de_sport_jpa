package gui_admin.view.horaires;

import entite.Horaire;
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

public class HoraireEdit extends GenericEdit<Horaire> {

    private JTextField idField;
    private JDateChooser debutChooser;
    private JDateChooser finChooser;
    private JTextField searchField;
    private JButton searchButton;
    private final JButton modifyButton = new ActionButton("Modifier", ActionButton.ButtonType.MODIFY);
    private final JButton deleteButton = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE);

    public HoraireEdit(List<List<Object>> tableData, List<String> columnNames) {
        super(tableData, columnNames);

        idField = new JTextField();
        idField.setEditable(false);
        debutChooser = new JDateChooser();
        debutChooser.setDateFormatString("dd/MM/yyyy HH:mm");
        finChooser = new JDateChooser();
        finChooser.setDateFormatString("dd/MM/yyyy HH:mm");
        searchField = new JTextField(20);
        searchButton = new ActionButton("Rechercher", ActionButton.ButtonType.SEARCH);

        initLayout();
        clearForm();
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 242, 245));

        JLabel globalTitleLabel = new JLabel("Gestion des Horaires", SwingConstants.LEFT);
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
                "Détails de l'Horaire", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Goldman", Font.BOLD, 16), new Color(32, 64, 128)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormField(form, gbc, "ID :", idField, 0);
        addFormField(form, gbc, "Début :", debutChooser, 1);
        addFormField(form, gbc, "Fin :", finChooser, 2);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private void addFormField(JPanel parent, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.2; gbc.anchor = GridBagConstraints.WEST;
        parent.add(label, gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        component.setFont(new Font("SansSerif", Font.PLAIN, 14));
        component.setPreferredSize(new Dimension(280, 35));
        parent.add(component, gbc);
    }

    private JPanel createListSectionPanel() {
        JPanel listSectionPanel = new JPanel(new BorderLayout(0, 10));
        listSectionPanel.setBackground(new Color(240, 242, 245));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel listTitleLabel = new JLabel("Liste des Horaires", SwingConstants.LEFT);
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
        searchPanel.add(new JLabel("Rechercher par date (jj/mm/aaaa) :"));
        searchField.putClientProperty("JTextField.placeholderText", "jj/mm/aaaa...");
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
            setEntity(new Horaire());
        }
        Horaire entite = getEntity();
        Date debut = debutChooser.getDate();
        Date fin = finChooser.getDate();

        if (debut != null) {
            entite.setDebut(debut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } else {
            entite.setDebut(null);
        }

        if (fin != null) {
            entite.setFin(fin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } else {
            entite.setFin(null);
        }

        if (entite.getDebut() != null && entite.getFin() != null && entite.getDebut().isAfter(entite.getFin())) {
            JOptionPane.showMessageDialog(this, "La date de début doit être antérieure à la date de fin.", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Date de début après date de fin");
        }
    }

    @Override
    public void initFormWithEntity(Horaire horaire) {
        setEntity(horaire);
        if (horaire != null) {
            idField.setText(String.valueOf(horaire.getId()));
            if (horaire.getDebut() != null) {
                debutChooser.setDate(Date.from(horaire.getDebut().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                debutChooser.setDate(null);
            }
            if (horaire.getFin() != null) {
                finChooser.setDate(Date.from(horaire.getFin().atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                finChooser.setDate(null);
            }
        } else {
            clearForm();
        }
    }

    @Override
    public void clearForm() {
        setEntity(null);
        idField.setText("");
        debutChooser.setDate(null);
        finChooser.setDate(null);
        searchField.setText("");
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    @Override
    public JButton getModifyButton() {
        return this.modifyButton;
    }

    @Override
    public JButton getDeleteButton() {
        return this.deleteButton;
    }
}
