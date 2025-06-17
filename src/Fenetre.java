package gui;

import candidat.Depute;
import delimitation.Distrika;
import delimitation.Faritany;
import delimitation.Faritra;
import elections.BureauVote;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class Fenetre extends JFrame {

    JComboBox<Faritany> listFaritany;
    JComboBox<Faritra> listFaritra;
    JComboBox<Distrika> listDistrika;
    JComboBox<Depute> listDepute;
    JComboBox<BureauVote> listBV;

    public Fenetre() {
        setTitle("Election deputes");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2)); 

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
    }

    public void initializeComponents() {
        listFaritany = new JComboBox<>();
        listFaritra = new JComboBox<>();
        listDistrika = new JComboBox<>();
        listDepute = new JComboBox<>();
        listBV = new JComboBox<>();

        List<Faritany> faritanyList = getFaritanyData();
        for (Faritany f : faritanyList) {
            listFaritany.addItem(f);
        }

        // Listener : mise a jour des faritra
        listFaritany.addActionListener(e -> {
            Faritany selectedFaritany = (Faritany) listFaritany.getSelectedItem();
            listFaritra.removeAllItems();
            if (selectedFaritany != null) {
                for (Faritra f : selectedFaritany.getListFaritra()) {
                    listFaritra.addItem(f);
                }
            }
            listDistrika.removeAllItems();
            listDepute.removeAllItems();
            listBV.removeAllItems();
        });

        // Listener : mise a jour des distrika
        listFaritra.addActionListener(e -> {
            Faritra selectedFaritra = (Faritra) listFaritra.getSelectedItem();
            listDistrika.removeAllItems();
            if (selectedFaritra != null) {
                for (Distrika d : selectedFaritra.getListDistrika()) {
                    listDistrika.addItem(d);
                }
            }
            listDepute.removeAllItems();
            listBV.removeAllItems();
        });

        // Listener : mise a jour des deputes et bureaux
        listDistrika.addActionListener(e -> {
            Distrika selectedDistrika = (Distrika) listDistrika.getSelectedItem();
            listDepute.removeAllItems();
            listBV.removeAllItems();
            if (selectedDistrika != null) {
                for (Depute d : selectedDistrika.getDeputeDistrika()) {
                    listDepute.addItem(d);
                }

                for (BureauVote b : getBureauxFromDistrika(selectedDistrika)) {
                    listBV.addItem(b);
                }
            }
        });
    }

    private List<Faritany> getFaritanyData() {
        // Creation manuelle d'exemples pour Faritra, Distrika, Depute, BureauVote

        // Deputes
        List<Depute> deputes1 = List.of(new Depute("Rakoto", null), new Depute("Rabe", null));
        List<Depute> deputes2 = List.of(new Depute("Randria", null));

        // Distrika
        Distrika distrika1 = new Distrika("Distrika A", null, deputes1, deputes1.size());
        Distrika distrika2 = new Distrika("Distrika B", null, deputes2, deputes2.size());

        // Relier deputes aux distrika
        for (Depute d : deputes1) {
            d.setDistrikaCandidat(distrika1);
        }
        for (Depute d : deputes2) {
            d.setDistrikaCandidat(distrika2);
        }

        // Faritra
        Faritra faritra1 = new Faritra("Faritra 1", null, List.of(distrika1));
        Faritra faritra2 = new Faritra("Faritra 2", null, List.of(distrika2));

        // Relier distrika aux faritra
        distrika1.setFaritraDistrika(faritra1);
        distrika2.setFaritraDistrika(faritra2);

        // Faritany
        Faritany faritany1 = new Faritany("Faritany Nord", List.of(faritra1, faritra2));

        // Relier faritra aux faritany
        faritra1.setFaritanyFaritra(faritany1);
        faritra2.setFaritanyFaritra(faritany1);

        return List.of(faritany1);
    }

    private List<BureauVote> getBureauxFromDistrika(Distrika d) {
        return List.of(
                new BureauVote("BV 1 - " + d.getNomDistrika(), d.getDeputeDistrika(), d),
                new BureauVote("BV 2 - " + d.getNomDistrika(), d.getDeputeDistrika(), d)
        );
    }
}
