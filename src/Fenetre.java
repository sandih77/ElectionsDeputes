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

    public Fenetre() {
        setTitle("Election deputes");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2));

        initializeComponents();

        add(new JLabel("Faritany :"));
        add(listFaritany);
        add(new JLabel("Faritra :"));
        add(listFaritra);
        add(new JLabel("Distrika :"));
        add(listDistrika);
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

        List<Faritany> faritanyList = getFaritanyFromFile("data/data.txt");

        for (Faritany f : faritanyList) {
            listFaritany.addItem(f);
        }

        listFaritany.addActionListener(e -> {
            Faritany selectedFaritany = (Faritany) listFaritany.getSelectedItem();
            listFaritra.removeAllItems();
            if (selectedFaritany != null && selectedFaritany.getListFaritra() != null) {
                for (Faritra f : selectedFaritany.getListFaritra()) {
                    listFaritra.addItem(f);
                }
            }
            listDistrika.removeAllItems();
            listDepute.removeAllItems();
            listBV.removeAllItems();
        });

        listFaritra.addActionListener(e -> {
            Faritra selectedFaritra = (Faritra) listFaritra.getSelectedItem();
            listDistrika.removeAllItems();
            if (selectedFaritra != null && selectedFaritra.getListDistrika() != null) {
                for (Distrika d : selectedFaritra.getListDistrika()) {
                    listDistrika.addItem(d);
                }
            }
            listDepute.removeAllItems();
            listBV.removeAllItems();
        });

        listDistrika.addActionListener(e -> {
            Distrika selectedDistrika = (Distrika) listDistrika.getSelectedItem();
            listDepute.removeAllItems();
            listBV.removeAllItems();
            if (selectedDistrika != null && selectedDistrika.getDeputeDistrika() != null) {
                for (Depute d : selectedDistrika.getDeputeDistrika()) {
                    listDepute.addItem(d);
                }
                for (BureauVote b : getBureauxFromDistrika(selectedDistrika)) {
                    listBV.addItem(b);
                }
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

                // === Création des Députés avec second membre ===
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

                // === Création des bureaux de vote ===
                String[] nomBureaux = parts[4].trim().split(",");
                List<BureauVote> bureaux = new ArrayList<>();
                for (String nomBV : nomBureaux) {
                    BureauVote bv = new BureauVote(nomBV.trim(), deputes, null); // Distrika assigné plus tard
                    bureaux.add(bv);
                }

                // === Recherche ou création de Faritany ===
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

                // === Recherche ou création de Faritra ===
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

                // === Création de Distrika ===
                Distrika distrika = new Distrika(nomDistrika, faritra, deputes, nbDeputes);
                faritra.getListDistrika().add(distrika);

                // === Association des députés et leurs seconds au Distrika ===
                for (Depute d : deputes) {
                    d.setDistrikaCandidat(distrika);
                    if (d.getSecondMembre() != null) {
                        d.getSecondMembre().setDistrikaCandidat(distrika);
                    }
                }

                // === Mise à jour des bureaux avec le Distrika ===
                for (BureauVote bv : bureaux) {
                    bv.setDistrika(distrika); // suppose que tu as un setter ou constructeur adapté
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return faritanyList;
    }
}
