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
    JButton boutonAfficher;
    JButton boutonCharger;
    JTextArea textAreaResultat;

    List<Vote> allVotes;
    Result resultUtil;

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
        boutonAfficher = new JButton("Afficher elu");
        boutonCharger = new JButton("Charger sélection");
        textAreaResultat = new JTextArea();
        textAreaResultat.setEditable(false);
        JScrollPane scroll = new JScrollPane(textAreaResultat);

        JPanel panelHaut = new JPanel(new GridLayout(5, 2, 10, 5));

        remplirFaritany();

        boutonCharger.addActionListener(e -> {
            String faritany = (String) comboFaritany.getSelectedItem();
            String faritra = (String) comboFaritra.getSelectedItem();

            if (faritany != null && !faritany.isBlank() && (faritra == null || faritra.isBlank())) {
                remplirFaritra(faritany);
                comboDistrika.removeAllItems();
                textAreaResultat.setText("");
            }

            else if (faritany != null && !faritany.isBlank() && faritra != null && !faritra.isBlank()) {
                remplirDistrika(faritany, faritra);
                textAreaResultat.setText("");
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
                resultat += "Distrika : " + distrika + " → elu : " + elu + "\n";

            } else if (faritra != null && !faritra.isBlank()) {
                List<String> distrikas = resultUtil.getNomsDistrikaDansFaritra(allVotes, faritany, faritra);
                for (String d : distrikas) {
                    List<Vote> votes = resultUtil.filtrerVotesParDistrika(allVotes, d);
                    String elu = resultUtil.trouverEluDansDistrika(votes);
                    resultat += "Distrika : " + d + " → elu : " + elu + "\n";
                }

            } else if (faritany != null && !faritany.isBlank()) {
                List<String> faritras = resultUtil.getNomsFaritraDansFaritany(allVotes, faritany);
                for (String r : faritras) {
                    List<String> distrikas = resultUtil.getNomsDistrikaDansFaritra(allVotes, faritany, r);
                    for (String d : distrikas) {
                        List<Vote> votes = resultUtil.filtrerVotesParDistrika(allVotes, d);
                        String elu = resultUtil.trouverEluDansDistrika(votes);
                        resultat += "Distrika : " + d + " → elu : " + elu + "\n";
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
        panelHaut.add(boutonCharger);
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
