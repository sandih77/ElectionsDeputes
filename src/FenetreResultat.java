package gui;

import elections.Result;
import elections.Vote;
import gui.listeners.ConfirmerResultatListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FenetreResultat extends JFrame {

    public FileDeroulante comboFaritany;
    public FileDeroulante comboFaritra;
    public FileDeroulante comboDistrika;
    public JButton btnConfirmer;
    public JButton boutonAfficher;
    public JTextArea textAreaResultat;

    public List<Vote> allVotes;
    public Result resultUtil;

    public int etapeSelection = 0;

    public FenetreResultat() {
        setTitle("Résultat des élections");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        resultUtil = new Result();
        allVotes = resultUtil.lireVotes("data/votes.txt");

        comboFaritany = new FileDeroulante();
        comboFaritra = new FileDeroulante();
        comboDistrika = new FileDeroulante();
        btnConfirmer = new JButton("Confirmer Faritany");
        boutonAfficher = new JButton("Afficher élu");
        textAreaResultat = new JTextArea();
        textAreaResultat.setEditable(false);
        JScrollPane scroll = new JScrollPane(textAreaResultat);

        JPanel panelHaut = new JPanel(new GridLayout(5, 2, 10, 5));

        remplirFaritany();
        comboFaritra.setEnabled(false);
        comboDistrika.setEnabled(false);

        btnConfirmer.addActionListener(new ConfirmerResultatListener(this));

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
        comboFaritany.remplir(resultUtil.getNomsFaritany(allVotes).toArray());
    }

    public void remplirFaritra(String faritany) {
        comboFaritra.remplir(resultUtil.getNomsFaritraDansFaritany(allVotes, faritany).toArray());
    }

    public void remplirDistrika(String faritany, String faritra) {
        comboDistrika.remplir(resultUtil.getNomsDistrikaDansFaritra(allVotes, faritany, faritra).toArray());
    }

    public JButton getButtonAfficher() {
        return this.boutonAfficher;
    }

    public JButton getButtonConfirmer() {
        return this.btnConfirmer;
    }

    public FileDeroulante getComboFaritany() {
        return this.comboFaritany;
    }

    public FileDeroulante getComboFaritra() {
        return this.comboFaritra;
    }

    public FileDeroulante getComboDistrika() {
        return this.comboDistrika;
    }

    public JTextArea getTextArea() {
        return this.textAreaResultat;
    }

    public int getEtapeSelection() {
        return this.etapeSelection;
    }

    public int setEtapeSelection(int s) {
        return this.etapeSelection = s;
    }

    public List<Vote> getListVote() {
        return this.allVotes;
    }

    public Result getResult() {
        return this.resultUtil;
    }
}
