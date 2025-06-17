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
        submit = new InsertButton("Submit");

        submit.addActionListener(e -> {
            saveVoteToFile("data/votes.txt");
        });

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

                String faritanyNom = parts[0].trim();
                String faritraNom = parts[1].trim();
                String distrikaNom = parts[2].trim();

                // Création liste deputes
                String[] deputesNames = parts[3].split(",");
                List<Depute> deputes = new ArrayList<>();
                for (int i = 0; i < deputesNames.length; i++) {
                    deputes.add(new Depute(deputesNames[i].trim(), null));
                }

                // Création liste bureaux de vote
                String[] bureauxNames = parts[4].split(",");
                List<BureauVote> bureaux = new ArrayList<>();
                for (int i = 0; i < bureauxNames.length; i++) {
                    bureaux.add(new BureauVote(bureauxNames[i].trim(), deputes, null));
                }

                // Cherche ou crée le Faritany
                Faritany faritany = null;
                for (int i = 0; i < faritanyList.size(); i++) {
                    Faritany f = faritanyList.get(i);
                    if (f.getNomFaritany().equalsIgnoreCase(faritanyNom)) {
                        faritany = f;
                        break;
                    }
                }
                if (faritany == null) {
                    faritany = new Faritany(faritanyNom, new ArrayList<>());
                    faritanyList.add(faritany);
                }

                // Cherche ou crée le Faritra
                Faritra faritra = null;
                List<Faritra> faritraList = faritany.getListFaritra();
                for (int i = 0; i < faritraList.size(); i++) {
                    Faritra f = faritraList.get(i);
                    if (f.getNomFaritra().equalsIgnoreCase(faritraNom)) {
                        faritra = f;
                        break;
                    }
                }
                if (faritra == null) {
                    faritra = new Faritra(faritraNom, faritany, new ArrayList<>());
                    faritraList.add(faritra);
                }

                // Crée le Distrika
                Distrika distrika = new Distrika(distrikaNom, faritra, deputes, deputes.size());
                for (int i = 0; i < deputes.size(); i++) {
                    deputes.get(i).setDistrikaCandidat(distrika);
                }
                for (int i = 0; i < bureaux.size(); i++) {
                    bureaux.get(i).setDistrika(distrika);
                }

                faritra.getListDistrika().add(distrika);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return faritanyList;
    }

    public void saveVoteToFile(String filename) {
        Faritany f = (Faritany) listFaritany.getSelectedItem();
        Faritra r = (Faritra) listFaritra.getSelectedItem();
        Distrika d = (Distrika) listDistrika.getSelectedItem();
        Depute dep = (Depute) listDepute.getSelectedItem();
        BureauVote bv = (BureauVote) listBV.getSelectedItem();
        String voteStr = votes.getText();

        if (f == null || r == null || d == null || dep == null || bv == null || voteStr.isEmpty()) {
            System.out.println("Case vide");
            return;
        }

        String line = f.getNomFaritany() + "|" + r.getNomFaritra() + "|" + d.getNomDistrika() + "|" + dep.getNomDepute() + "|" + bv.getNomBV() + "|" + voteStr;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine();
            System.out.println("Vote enregistre");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ecriture");
        }
    }
}
