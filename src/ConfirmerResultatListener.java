package gui.listeners;

import elections.Vote;
import gui.FenetreResultat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConfirmerResultatListener implements ActionListener {

    private final FenetreResultat fenetre;

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
                    showMessage("Veuillez sélectionner un Faritany.");
                }
            }

            case 1 -> {
                String faritany = (String) fenetre.getComboFaritany().getSelection();
                String faritra = (String) fenetre.getComboFaritra().getSelection();
                if (faritra != null && !faritra.isBlank()) {
                    fenetre.remplirDistrika(faritany, faritra);
                    fenetre.getComboDistrika().setEnabled(true);
                    fenetre.setEtapeSelection(2);
                    fenetre.getButtonConfirmer().setText("Réinitialiser");
                    fenetre.getTextArea().setText("");
                } else {
                    showMessage("Veuillez sélectionner un Faritra.");
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

    private void afficherElu() {
        String resultat = "";
        String faritany = (String) fenetre.getComboFaritany().getSelection();
        String faritra = (String) fenetre.getComboFaritra().getSelection();
        String distrika = (String) fenetre.getComboDistrika().getSelection();

        if (distrika != null && !distrika.isBlank()) {
            List<Vote> votes = fenetre.getResult().filtrerVotesParDistrika(fenetre.getListVote(), distrika);
            String elu = fenetre.getResult().trouverEluDansDistrika(votes);
            resultat += "Distrika : " + distrika + " → élu : " + elu + "\n";

        } else if (faritra != null && !faritra.isBlank()) {
            List<String> distrikas = fenetre.getResult().getNomsDistrikaDansFaritra(fenetre.getListVote(), faritany, faritra);
            for (String d : distrikas) {
                List<Vote> votes = fenetre.getResult().filtrerVotesParDistrika(fenetre.getListVote(), d);
                String elu = fenetre.getResult().trouverEluDansDistrika(votes);
                resultat += "Distrika : " + d + " → élu : " + elu + "\n";
            }

        } else if (faritany != null && !faritany.isBlank()) {
            List<String> faritras = fenetre.getResult().getNomsFaritraDansFaritany(fenetre.getListVote(), faritany);
            for (String r : faritras) {
                List<String> distrikas = fenetre.getResult().getNomsDistrikaDansFaritra(fenetre.getListVote(), faritany, r);
                for (String d : distrikas) {
                    List<Vote> votes = fenetre.getResult().filtrerVotesParDistrika(fenetre.getListVote(), d);
                    String elu = fenetre.getResult().trouverEluDansDistrika(votes);
                    resultat += "Distrika : " + d + " → élu : " + elu + "\n";
                }
            }

        } else {
            resultat += "Veuillez choisir au moins un Faritany.\n";
        }

        fenetre.getTextArea().setText(resultat);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(fenetre, message, "Attention", JOptionPane.WARNING_MESSAGE);
    }
}
