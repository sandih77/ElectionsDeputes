package gui.listeners;

import elections.Vote;
import gui.FenetreResultat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ConfirmerResultatListener implements ActionListener {

    FenetreResultat fenetre;

    public ConfirmerResultatListener(FenetreResultat fenetre) {
        this.fenetre = fenetre;

        fenetre.getButtonAfficher().addActionListener(e -> afficherElu());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int etape = fenetre.getEtapeSelection();

        switch (etape) {
            case 0 -> {
                String faritany = (String) fenetre.getComboFaritany().getSelection();
                if (faritany != null && !faritany.isBlank()) {
                    fenetre.remplirFaritra(faritany);
                    fenetre.getComboFaritra().setEnabled(true);
                    fenetre.getComboDistrika().vider();
                    fenetre.getComboDistrika().setEnabled(false);
                    fenetre.setEtapeSelection(1);
                    fenetre.getButtonConfirmer().setText("Confirmer Faritra");
                    fenetre.getTextArea().setText("");
                } else {
                    showMessage("Veuillez selectionner un Faritany.");
                }
            }

            case 1 -> {
                String faritany = (String) fenetre.getComboFaritany().getSelection();
                String faritra = (String) fenetre.getComboFaritra().getSelection();
                if (faritra != null && !faritra.isBlank()) {
                    fenetre.remplirDistrika(faritany, faritra);
                    fenetre.getComboDistrika().setEnabled(true);
                    fenetre.setEtapeSelection(2);
                    fenetre.getButtonConfirmer().setText("Reinitialiser");
                    fenetre.getTextArea().setText("");
                } else {
                    showMessage("Veuillez selectionner un Faritra.");
                }
            }

            case 2 -> {
                fenetre.getComboFaritany().setEnabled(true);
                fenetre.getComboFaritra().vider();
                fenetre.getComboFaritra().setEnabled(false);
                fenetre.getComboDistrika().vider();
                fenetre.getComboDistrika().setEnabled(false);
                fenetre.setEtapeSelection(0);
                fenetre.getButtonConfirmer().setText("Confirmer Faritany");
                fenetre.getTextArea().setText("");
            }
        }
    }

    public void afficherElu() {
        StringBuilder resultat = new StringBuilder();
        String faritany = (String) fenetre.getComboFaritany().getSelection();
        String faritra = (String) fenetre.getComboFaritra().getSelection();
        String distrika = (String) fenetre.getComboDistrika().getSelection();

        if (distrika != null && !distrika.isBlank()) {
            List<Vote> votes = fenetre.getResult().filtrerVotesParDistrika(fenetre.getListVote(), distrika);
            String elu = fenetre.getResult().trouverEluDansDistrika(votes);
            resultat.append("Distrika : ").append(distrika).append(" → elu : ").append(elu).append("\n\n");

            // Affichage nombre d'elus par parti
            Map<String, Integer> nbElusParParti = fenetre.getResult().getNbElusParParti(votes);
            resultat.append("Nombre d'elus par parti :\n");
            for (Map.Entry<String, Integer> entry : nbElusParParti.entrySet()) {
                resultat.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
            }

        } else if (faritra != null && !faritra.isBlank()) {
            List<String> distrikas = fenetre.getResult().getNomsDistrikaDansFaritra(fenetre.getListVote(), faritany, faritra);
            for (String d : distrikas) {
                List<Vote> votes = fenetre.getResult().filtrerVotesParDistrika(fenetre.getListVote(), d);
                String elu = fenetre.getResult().trouverEluDansDistrika(votes);
                resultat.append("Distrika : ").append(d).append(" → elu : ").append(elu).append("\n");
            }

        } else if (faritany != null && !faritany.isBlank()) {
            List<String> faritras = fenetre.getResult().getNomsFaritraDansFaritany(fenetre.getListVote(), faritany);
            for (String r : faritras) {
                List<String> distrikas = fenetre.getResult().getNomsDistrikaDansFaritra(fenetre.getListVote(), faritany, r);
                for (String d : distrikas) {
                    List<Vote> votes = fenetre.getResult().filtrerVotesParDistrika(fenetre.getListVote(), d);
                    String elu = fenetre.getResult().trouverEluDansDistrika(votes);
                    resultat.append("Distrika : ").append(d).append(" → elu : ").append(elu).append("\n");
                }
            }

        } else {
            resultat.append("Veuillez choisir au moins un Faritany.\n");
        }

        fenetre.getTextArea().setText(resultat.toString());
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(fenetre, message, "Attention", JOptionPane.WARNING_MESSAGE);
    }
}
