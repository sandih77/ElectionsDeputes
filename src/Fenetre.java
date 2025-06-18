package gui;

import candidat.Depute;
import delimitation.Distrika;
import delimitation.Faritany;
import delimitation.Faritra;
import elections.BureauVote;
import gui.button.InsertButton;
import gui.listeners.ConfirmerListener;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Fenetre extends JFrame {

    FileDeroulante listFaritany;
    FileDeroulante listFaritra;
    FileDeroulante listDistrika;
    FileDeroulante listDepute;
    FileDeroulante listBV;
    JTextField votes;
    InsertButton submit;
    JButton btnConfirmer;
    int etapeSelection = 0; // 0: Faritany, 1: Faritra, 2: Distrika, 3: fin
    List<Faritany> faritanyList;

    public Fenetre() {
        setTitle("Election deputes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(12, 2, 5, 5));

        initializeComponents();
        layoutComponents();
        attachListeners();
    }

    public void initializeComponents() {
        listFaritany = new FileDeroulante();
        listFaritra = new FileDeroulante();
        listDistrika = new FileDeroulante();
        listDepute = new FileDeroulante();
        listBV = new FileDeroulante();
        votes = new JTextField();
        submit = new InsertButton("Submit", listFaritany, listFaritra, listDistrika, listDepute, listBV, votes);

        btnConfirmer = new JButton("Confirmer Faritany");

        faritanyList = getFaritanyFromFile("data/data.txt");
        if (faritanyList != null) {
            listFaritany.remplir(faritanyList.toArray());
        }

        listFaritra.setEnabled(false);
        listDistrika.setEnabled(false);
        listDepute.setEnabled(false);
        listBV.setEnabled(false);
    }

    public void layoutComponents() {
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

    public void attachListeners() {
        btnConfirmer.addActionListener(new ConfirmerListener(this));
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
                        second = new Depute(couple[1].trim(), null, null);
                    }
                    deputes.add(new Depute(nomTitulaire, second, null));
                }

                String[] nomBureaux = parts[4].trim().split(",");
                List<BureauVote> bureaux = new ArrayList<>();
                for (String nomBV : nomBureaux) {
                    bureaux.add(new BureauVote(nomBV.trim(), deputes, null));
                }

                // Chercher ou créer Faritany
                Faritany faritany = faritanyList.stream()
                        .filter(f -> f.getNomFaritany().equalsIgnoreCase(nomFaritany))
                        .findFirst()
                        .orElseGet(() -> {
                            Faritany f = new Faritany(nomFaritany, new ArrayList<>());
                            faritanyList.add(f);
                            return f;
                        });

                // Chercher ou créer Faritra
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

    public int getEtapeSelection() {
        return etapeSelection;
    }

    public void setEtapeSelection(int etapeSelection) {
        this.etapeSelection = etapeSelection;
    }

    public FileDeroulante getListFaritany() {
        return listFaritany;
    }

    public FileDeroulante getListFaritra() {
        return listFaritra;
    }

    public FileDeroulante getListDistrika() {
        return listDistrika;
    }

    public FileDeroulante getListDepute() {
        return listDepute;
    }

    public FileDeroulante getListBV() {
        return listBV;
    }

    public JButton getBtnConfirmer() {
        return btnConfirmer;
    }
}
