package button;

import candidat.Depute;
import delimitation.Distrika;
import delimitation.Faritany;
import delimitation.Faritra;
import elections.BureauVote;

import javax.swing.*;
import java.io.*;

public class InsertButton extends JButton {

    JComboBox<Faritany> listFaritany;
    JComboBox<Faritra> listFaritra;
    JComboBox<Distrika> listDistrika;
    JComboBox<Depute> listDepute;
    JComboBox<BureauVote> listBV;
    JTextField votes;

    public InsertButton(String label,
            JComboBox<Faritany> listFaritany,
            JComboBox<Faritra> listFaritra,
            JComboBox<Distrika> listDistrika,
            JComboBox<Depute> listDepute,
            JComboBox<BureauVote> listBV,
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

        String line = f.getNomFaritany() + "|"
                + r.getNomFaritra() + "|"
                + d.getNomDistrika() + "|"
                + dep.getNomDepute() + "|"
                + bv.getNomBV() + "|"
                + voteStr;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine();
            System.out.println("Vote enregistre");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur enregistrement de vote");
        }
    }
}
