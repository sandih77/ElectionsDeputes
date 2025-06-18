package gui.button;

import candidat.Depute;
import delimitation.Distrika;
import delimitation.Faritany;
import delimitation.Faritra;
import elections.BureauVote;
import gui.FileDeroulante;

import javax.swing.*;
import java.io.*;

public class InsertButton extends JButton {

    FileDeroulante listFaritany;
    FileDeroulante listFaritra;
    FileDeroulante listDistrika;
    FileDeroulante listDepute;
    FileDeroulante listBV;
    JTextField votes;

    public InsertButton(String label,
            FileDeroulante listFaritany,
            FileDeroulante listFaritra,
            FileDeroulante listDistrika,
            FileDeroulante listDepute,
            FileDeroulante listBV,
            JTextField votes) {
        super(label);
        this.listFaritany = listFaritany;
        this.listFaritra = listFaritra;
        this.listDistrika = listDistrika;
        this.listDepute = listDepute;
        this.listBV = listBV;
        this.votes = votes;

        addActionListener(e -> saveVoteToFile("data/votes.txt"));
    }

    private void saveVoteToFile(String filename) {
        Faritany f = (Faritany) listFaritany.getSelection();
        Faritra r = (Faritra) listFaritra.getSelection();
        Distrika d = (Distrika) listDistrika.getSelection();
        Depute dep = (Depute) listDepute.getSelection();
        BureauVote bv = (BureauVote) listBV.getSelection();
        String voteStr = votes.getText().trim();

        if (f == null || r == null || d == null || dep == null || bv == null || voteStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Champ vide", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int nbVotes;
        try {
            nbVotes = Integer.parseInt(voteStr);
            if (nbVotes < 0) {
                throw new NumberFormatException("Nombre négatif");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre de votes valide (entier positif).", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String line = String.join("|",
                f.getNomFaritany(),
                r.getNomFaritra(),
                d.getNomDistrika(),
                String.valueOf(d.getNbDeputes()),
                dep.getNomDepute() + ":" + (dep.getSecondMembre() != null ? dep.getSecondMembre().getNomDepute() : "Aucun"),
                bv.getNomBV(),
                String.valueOf(nbVotes)
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Vote enregistré avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement du vote : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
