package gui;

import candidat.Depute;
import delimitation.Distrika;
import delimitation.Faritany;
import delimitation.Faritra;
import elections.BureauVote;
import button.InsertButton;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Fenetre extends JFrame {

    JComboBox<Faritany> listFaritany;
    JComboBox<Faritra> listFaritra;
    JComboBox<Distrika> listDistrika;
    JComboBox<Depute> listDepute;
    JComboBox<BureauVote> listBV;
    JTextField votes;
    InsertButton submit;

    JButton btnConfirmerFaritany;
    JButton btnConfirmerFaritra;
    JButton btnConfirmerDistrika;

    List<Faritany> faritanyList;

    public Fenetre() {
        setTitle("Election deputes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(10, 2, 5, 5));

        initializeComponents();

        add(new JLabel("Faritany :"));
        add(listFaritany);
        add(btnConfirmerFaritany);
        add(new JLabel("")); 

        add(new JLabel("Faritra :"));
        add(listFaritra);
        add(btnConfirmerFaritra);
        add(new JLabel(""));

        add(new JLabel("Distrika :"));
        add(listDistrika);
        add(btnConfirmerDistrika);
        add(new JLabel(""));

        add(new JLabel("Depute :"));
        add(listDepute);

        add(new JLabel("Bureau de vote :"));
        add(listBV);

        add(new JLabel("Vote :"));
        add(votes);

        add(submit);
    }

    public void initializeComponents() {
        listFaritany = new JComboBox<>();
        listFaritra = new JComboBox<>();
        listDistrika = new JComboBox<>();
        listDepute = new JComboBox<>();
        listBV = new JComboBox<>();
        votes = new JTextField();
        submit = new InsertButton("Submit", listFaritany, listFaritra, listDistrika, listDepute, listBV, votes);

        btnConfirmerFaritany = new JButton("Confirmer Faritany");
        btnConfirmerFaritra = new JButton("Confirmer Faritra");
        btnConfirmerDistrika = new JButton("Confirmer Distrika");

        // Chargement initial Faritany
        faritanyList = getFaritanyFromFile("data/data.txt");
        for (Faritany f : faritanyList) {
            listFaritany.addItem(f);
        }

        // On désactive les combos suivants au départ
        listFaritra.setEnabled(false);
        listDistrika.setEnabled(false);
        listDepute.setEnabled(false);
        listBV.setEnabled(false);

        btnConfirmerFaritra.setEnabled(false);
        btnConfirmerDistrika.setEnabled(false);

        // Bouton Confirmer Faritany : charge Faritra correspondants
        btnConfirmerFaritany.addActionListener(e -> {
            Faritany selectedFaritany = (Faritany) listFaritany.getSelectedItem();

            listFaritra.removeAllItems();
            listDistrika.removeAllItems();
            listDepute.removeAllItems();
            listBV.removeAllItems();

            listDepute.setEnabled(false);
            listBV.setEnabled(false);

            if (selectedFaritany != null && selectedFaritany.getListFaritra() != null) {
                for (Faritra f : selectedFaritany.getListFaritra()) {
                    listFaritra.addItem(f);
                }
                listFaritra.setEnabled(true);
                btnConfirmerFaritra.setEnabled(true);
            } else {
                listFaritra.setEnabled(false);
                btnConfirmerFaritra.setEnabled(false);
            }

            listDistrika.setEnabled(false);
            btnConfirmerDistrika.setEnabled(false);
        });

        // Bouton Confirmer Faritra : charge Distrika correspondants
        btnConfirmerFaritra.addActionListener(e -> {
            Faritra selectedFaritra = (Faritra) listFaritra.getSelectedItem();

            listDistrika.removeAllItems();
            listDepute.removeAllItems();
            listBV.removeAllItems();

            listDepute.setEnabled(false);
            listBV.setEnabled(false);

            if (selectedFaritra != null && selectedFaritra.getListDistrika() != null) {
                for (Distrika d : selectedFaritra.getListDistrika()) {
                    listDistrika.addItem(d);
                }
                listDistrika.setEnabled(true);
                btnConfirmerDistrika.setEnabled(true);
            } else {
                listDistrika.setEnabled(false);
                btnConfirmerDistrika.setEnabled(false);
            }
        });

        // Bouton Confirmer Distrika : charge Depute et BV correspondants
        btnConfirmerDistrika.addActionListener(e -> {
            Distrika selectedDistrika = (Distrika) listDistrika.getSelectedItem();

            listDepute.removeAllItems();
            listBV.removeAllItems();

            if (selectedDistrika != null) {
                // Charge députés
                if (selectedDistrika.getDeputeDistrika() != null) {
                    for (Depute d : selectedDistrika.getDeputeDistrika()) {
                        listDepute.addItem(d);
                    }
                    listDepute.setEnabled(true);
                } else {
                    listDepute.setEnabled(false);
                }

                // Charge bureaux de vote
                List<BureauVote> bureaux = getBureauxFromDistrika(selectedDistrika);
                if (!bureaux.isEmpty()) {
                    for (BureauVote b : bureaux) {
                        listBV.addItem(b);
                    }
                    listBV.setEnabled(true);
                } else {
                    listBV.setEnabled(false);
                }
            } else {
                listDepute.setEnabled(false);
                listBV.setEnabled(false);
            }
        });
    }

    public List<BureauVote> getBureauxFromDistrika(Distrika d) {
        return List.of(
            new BureauVote("BV 1 - " + d.getNomDistrika(), d.getDeputeDistrika(), d),
            new BureauVote("BV 2 - " + d.getNomDistrika(), d.getDeputeDistrika(), d)
        );
    }

    public List<Faritany> getFaritanyFromFile(String filename) {
        List<Faritany> faritanyList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length < 5) {
                    continue;
                }

                String nomFaritany = parts[0].trim();
                String nomFaritra = parts[1].trim();

                String[] distrikaInfo = parts[2].trim().split(",");
                if (distrikaInfo.length != 2) {
                    continue;
                }

                String nomDistrika = distrikaInfo[0].trim();
                int nbDeputes = Integer.parseInt(distrikaInfo[1].trim());

                String[] nomDeputesBruts = parts[3].trim().split(",");
                List<Depute> deputes = new ArrayList<>();

                for (String nomBrut : nomDeputesBruts) {
                    String[] couple = nomBrut.trim().split(";;");
                    String nomTitulaire = couple[0].trim();
                    Depute second = null;

                    if (couple.length > 1) {
                        String nomSecond = couple[1].trim();
                        second = new Depute(nomSecond, null, null);
                    }

                    Depute titulaire = new Depute(nomTitulaire, second, null);
                    deputes.add(titulaire);
                }

                String[] nomBureaux = parts[4].trim().split(",");
                List<BureauVote> bureaux = new ArrayList<>();
                for (String nomBV : nomBureaux) {
                    BureauVote bv = new BureauVote(nomBV.trim(), deputes, null);
                    bureaux.add(bv);
                }

                // Chercher Faritany déjà existant sinon créer
                Faritany faritany = null;
                for (Faritany f : faritanyList) {
                    if (f.getNomFaritany().equalsIgnoreCase(nomFaritany)) {
                        faritany = f;
                        break;
                    }
                }
                if (faritany == null) {
                    faritany = new Faritany(nomFaritany, new ArrayList<>());
                    faritanyList.add(faritany);
                }

                // Chercher Faritra dans ce Faritany sinon créer
                Faritra faritra = null;
                for (Faritra f : faritany.getListFaritra()) {
                    if (f.getNomFaritra().equalsIgnoreCase(nomFaritra)) {
                        faritra = f;
                        break;
                    }
                }
                if (faritra == null) {
                    faritra = new Faritra(nomFaritra, faritany, new ArrayList<>());
                    faritany.getListFaritra().add(faritra);
                }

                // Ajouter le distrika
                Distrika distrika = new Distrika(nomDistrika, faritra, deputes, nbDeputes);
                faritra.getListDistrika().add(distrika);

                // Lier députés au distrika
                for (Depute d : deputes) {
                    d.setDistrikaCandidat(distrika);
                    if (d.getSecondMembre() != null) {
                        d.getSecondMembre().setDistrikaCandidat(distrika);
                    }
                }

                // Lier bureaux au distrika
                for (BureauVote bv : bureaux) {
                    bv.setDistrika(distrika);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return faritanyList;
    }
}
