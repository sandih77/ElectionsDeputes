package gui;

import elections.Result;
import elections.Vote;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FenetreResultat extends JFrame {

    JComboBox<String> comboFaritany;
    JComboBox<String> comboFaritra;
    JComboBox<String> comboDistrika;
    JButton btnConfirmer;
    JButton boutonAfficher;
    JTextArea textAreaResultat;

    List<Vote> allVotes;
    Result resultUtil;

    int etapeSelection = 0; // 0=Faritany, 1=Faritra, 2=Distrika

    public FenetreResultat() {
        setTitle("Resultat");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        resultUtil = new Result();
        allVotes = resultUtil.lireVotes("data/votes.txt");

        comboFaritany = new JComboBox<>();
        comboFaritra = new JComboBox<>();
        comboDistrika = new JComboBox<>();
        btnConfirmer = new JButton("Confirmer Faritany");
        boutonAfficher = new JButton("Afficher élu");
        textAreaResultat = new JTextArea();
        textAreaResultat.setEditable(false);
        JScrollPane scroll = new JScrollPane(textAreaResultat);

        JPanel panelHaut = new JPanel(new GridLayout(5, 2, 10, 5));

        remplirFaritany();

        comboFaritra.setEnabled(false);
        comboDistrika.setEnabled(false);

        btnConfirmer.addActionListener(e -> {
            switch (etapeSelection) {
                case 0 -> {
                    String faritany = (String) comboFaritany.getSelectedItem();
                    if (faritany != null && !faritany.isBlank()) {
                        remplirFaritra(faritany);
                        comboFaritra.setEnabled(true);
                        comboDistrika.removeAllItems();
                        comboDistrika.setEnabled(false);
                        etapeSelection = 1;
                        btnConfirmer.setText("Confirmer Faritra");
                        textAreaResultat.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un Faritany.");
                    }
                }
                case 1 -> {
                    String faritany = (String) comboFaritany.getSelectedItem();
                    String faritra = (String) comboFaritra.getSelectedItem();
                    if (faritra != null && !faritra.isBlank()) {
                        remplirDistrika(faritany, faritra);
                        comboDistrika.setEnabled(true);
                        etapeSelection = 2;
                        btnConfirmer.setText("Reinitialiser");
                        textAreaResultat.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un Faritra.");
                    }
                }
                case 2 -> {
                    comboFaritany.setEnabled(true);
                    comboFaritra.removeAllItems();
                    comboFaritra.setEnabled(false);
                    comboDistrika.removeAllItems();
                    comboDistrika.setEnabled(false);
                    etapeSelection = 0;
                    btnConfirmer.setText("Confirmer Faritany");
                    textAreaResultat.setText("");
                }
            }
        });

        boutonAfficher.addActionListener(e -> {
            String resultat = "";
            String faritany = (String) comboFaritany.getSelectedItem();
            String faritra = (String) comboFaritra.getSelectedItem();
            String distrika = (String) comboDistrika.getSelectedItem();

            if (distrika != null && !distrika.isBlank()) {
                List<Vote> votes = resultUtil.filtrerVotesParDistrika(allVotes, distrika);
                String elu = resultUtil.trouverEluDansDistrika(votes);
                resultat += "Distrika : " + distrika + " → élu : " + elu + "\n";

            } else if (faritra != null && !faritra.isBlank()) {
                List<String> distrikas = resultUtil.getNomsDistrikaDansFaritra(allVotes, faritany, faritra);
                for (String d : distrikas) {
                    List<Vote> votes = resultUtil.filtrerVotesParDistrika(allVotes, d);
                    String elu = resultUtil.trouverEluDansDistrika(votes);
                    resultat += "Distrika : " + d + " → élu : " + elu + "\n";
                }

            } else if (faritany != null && !faritany.isBlank()) {
                List<String> faritras = resultUtil.getNomsFaritraDansFaritany(allVotes, faritany);
                for (String r : faritras) {
                    List<String> distrikas = resultUtil.getNomsDistrikaDansFaritra(allVotes, faritany, r);
                    for (String d : distrikas) {
                        List<Vote> votes = resultUtil.filtrerVotesParDistrika(allVotes, d);
                        String elu = resultUtil.trouverEluDansDistrika(votes);
                        resultat += "Distrika : " + d + " → élu : " + elu + "\n";
                    }
                }

            } else {
                resultat += "Veuillez choisir au moins un Faritany.\n";
            }

            textAreaResultat.setText(resultat);
        });

        panelHaut.add(new JLabel("Faritany :"));
        panelHaut.add(comboFaritany);
        panelHaut.add(new JLabel("Faritra :"));
        panelHaut.add(comboFaritra);
        panelHaut.add(new JLabel("Distrika :"));
        panelHaut.add(comboDistrika);
        panelHaut.add(btnConfirmer);
        panelHaut.add(boutonAfficher);

        add(panelHaut, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public void remplirFaritany() {
        comboFaritany.removeAllItems();
        for (String f : resultUtil.getNomsFaritany(allVotes)) {
            comboFaritany.addItem(f);
        }
    }

    public void remplirFaritra(String faritany) {
        comboFaritra.removeAllItems();
        for (String r : resultUtil.getNomsFaritraDansFaritany(allVotes, faritany)) {
            comboFaritra.addItem(r);
        }
    }

    public void remplirDistrika(String faritany, String faritra) {
        comboDistrika.removeAllItems();
        for (String d : resultUtil.getNomsDistrikaDansFaritra(allVotes, faritany, faritra)) {
            comboDistrika.addItem(d);
        }
    }
}
