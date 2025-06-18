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

    JButton btnConfirmer;
    int etapeSelection = 0; // 0: Faritany, 1: Faritra, 2: Distrika

    List<Faritany> faritanyList;

    public Fenetre() {
        setTitle("Election deputes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(12, 2, 5, 5));

        initializeComponents();

        add(new JLabel("Faritany :"));
        add(listFaritany);
        add(new JLabel(""));
        add(new JLabel(""));

        add(new JLabel("Faritra :"));
        add(listFaritra);
        add(new JLabel(""));
        add(new JLabel(""));

        add(new JLabel("Distrika :"));
        add(listDistrika);
        add(new JLabel(""));
        add(new JLabel(""));

        add(btnConfirmer);
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

        btnConfirmer = new JButton("Confirmer Faritany");

        // Charger Faritany
        faritanyList = getFaritanyFromFile("data/data.txt");
        for (Faritany f : faritanyList) {
            listFaritany.addItem(f);
        }

        listFaritra.setEnabled(false);
        listDistrika.setEnabled(false);
        listDepute.setEnabled(false);
        listBV.setEnabled(false);

        btnConfirmer.addActionListener(e -> {
            switch (etapeSelection) {
                case 0 -> {
                    Faritany selectedFaritany = (Faritany) listFaritany.getSelectedItem();
                    listFaritra.removeAllItems();
                    listDistrika.removeAllItems();
                    listDepute.removeAllItems();
                    listBV.removeAllItems();

                    listFaritra.setEnabled(false);
                    listDistrika.setEnabled(false);
                    listDepute.setEnabled(false);
                    listBV.setEnabled(false);

                    if (selectedFaritany != null) {
                        for (Faritra f : selectedFaritany.getListFaritra()) {
                            listFaritra.addItem(f);
                        }
                        listFaritra.setEnabled(true);
                        etapeSelection = 1;
                        btnConfirmer.setText("Confirmer Faritra");
                    }
                }
                case 1 -> {
                    Faritra selectedFaritra = (Faritra) listFaritra.getSelectedItem();
                    listDistrika.removeAllItems();
                    listDepute.removeAllItems();
                    listBV.removeAllItems();

                    listDistrika.setEnabled(false);
                    listDepute.setEnabled(false);
                    listBV.setEnabled(false);

                    if (selectedFaritra != null) {
                        for (Distrika d : selectedFaritra.getListDistrika()) {
                            listDistrika.addItem(d);
                        }
                        listDistrika.setEnabled(true);
                        etapeSelection = 2;
                        btnConfirmer.setText("Confirmer Distrika");
                    }
                }
                case 2 -> {
                    Distrika selectedDistrika = (Distrika) listDistrika.getSelectedItem();
                    listDepute.removeAllItems();
                    listBV.removeAllItems();

                    listDepute.setEnabled(false);
                    listBV.setEnabled(false);

                    if (selectedDistrika != null) {
                        for (Depute d : selectedDistrika.getDeputeDistrika()) {
                            listDepute.addItem(d);
                        }
                        listDepute.setEnabled(true);

                        List<BureauVote> bureaux = getBureauxFromDistrika(selectedDistrika);
                        for (BureauVote b : bureaux) {
                            listBV.addItem(b);
                        }
                        listBV.setEnabled(true);

                        etapeSelection = 3;
                        btnConfirmer.setText("Reinitialiser");
                    }
                }

                case 3 -> {
                    listFaritra.removeAllItems();
                    listDistrika.removeAllItems();
                    listDepute.removeAllItems();
                    listBV.removeAllItems();

                    listFaritra.setEnabled(false);
                    listDistrika.setEnabled(false);
                    listDepute.setEnabled(false);
                    listBV.setEnabled(false);

                    etapeSelection = 0;
                    btnConfirmer.setText("Confirmer Faritany");
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

                Faritany faritany = faritanyList.stream()
                        .filter(f -> f.getNomFaritany().equalsIgnoreCase(nomFaritany))
                        .findFirst()
                        .orElseGet(() -> {
                            Faritany f = new Faritany(nomFaritany, new ArrayList<>());
                            faritanyList.add(f);
                            return f;
                        });

                Faritra faritra = faritany.getListFaritra().stream()
                        .filter(f -> f.getNomFaritra().equalsIgnoreCase(nomFaritra))
                        .findFirst()
                        .orElseGet(() -> {
                            Faritra f = new Faritra(nomFaritra, faritany, new ArrayList<>());
                            faritany.getListFaritra().add(f);
                            return f;
                        });

                Distrika distrika = new Distrika(nomDistrika, faritra, deputes, nbDeputes);
                faritra.getListDistrika().add(distrika);

                for (Depute d : deputes) {
                    d.setDistrikaCandidat(distrika);
                    if (d.getSecondMembre() != null) {
                        d.getSecondMembre().setDistrikaCandidat(distrika);
                    }
                }

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
