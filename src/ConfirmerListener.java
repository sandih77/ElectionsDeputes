package gui.listeners;

import delimitation.Distrika;
import delimitation.Faritany;
import delimitation.Faritra;
import elections.BureauVote;
import gui.Fenetre;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConfirmerListener implements ActionListener {

    Fenetre fenetre;

    public ConfirmerListener(Fenetre fenetre) {
        this.fenetre = fenetre;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int etape = fenetre.getEtapeSelection();

        switch (etape) {
            case 0 -> {
                Faritany selectedFaritany = (Faritany) fenetre.getListFaritany().getSelection();
                if (selectedFaritany == null) {
                    showMessage("Veuillez selectionner un Faritany.");
                    return;
                }
                fenetre.getListFaritra().remplir(selectedFaritany.getListFaritra().toArray());
                fenetre.getListFaritra().setEnabled(true);

                fenetre.getListDistrika().vider();
                fenetre.getListDistrika().setEnabled(false);
                fenetre.getListDepute().vider();
                fenetre.getListDepute().setEnabled(false);
                fenetre.getListBV().vider();
                fenetre.getListBV().setEnabled(false);

                fenetre.setEtapeSelection(1);
                fenetre.getBtnConfirmer().setText("Confirmer Faritra");
            }
            case 1 -> {
                Faritra selectedFaritra = (Faritra) fenetre.getListFaritra().getSelection();
                if (selectedFaritra == null) {
                    showMessage("Veuillez selectionner un Faritra.");
                    return;
                }

                fenetre.getListDistrika().remplir(selectedFaritra.getListDistrika().toArray());
                fenetre.getListDistrika().setEnabled(true);

                fenetre.getListDepute().vider();
                fenetre.getListDepute().setEnabled(false);
                fenetre.getListBV().vider();
                fenetre.getListBV().setEnabled(false);

                fenetre.setEtapeSelection(2);
                fenetre.getBtnConfirmer().setText("Confirmer Distrika");
            }
            case 2 -> {
                Distrika selectedDistrika = (Distrika) fenetre.getListDistrika().getSelection();
                if (selectedDistrika == null) {
                    showMessage("Veuillez selectionner un Distrika.");
                    return;
                }

                fenetre.getListDepute().remplir(selectedDistrika.getListDepute().toArray());
                fenetre.getListDepute().setEnabled(true);

                fenetre.getListBV().vider();
                List<BureauVote> bureauVotes = getBureauxFromDistrika(selectedDistrika);
                fenetre.getListBV().remplir(bureauVotes.toArray()); // <-- Ici la vraie source des BV
                fenetre.getListBV().setEnabled(true);

                fenetre.setEtapeSelection(3);
                fenetre.getBtnConfirmer().setText("Reinitialiser");
            }

            case 3 -> {
                fenetre.getListFaritany().setEnabled(true);
                fenetre.getListFaritra().vider();
                fenetre.getListFaritra().setEnabled(false);

                fenetre.getListDistrika().vider();
                fenetre.getListDistrika().setEnabled(false);

                fenetre.getListDepute().vider();
                fenetre.getListDepute().setEnabled(false);

                fenetre.getListBV().vider();
                fenetre.getListBV().setEnabled(false);

                fenetre.setEtapeSelection(0);
                fenetre.getBtnConfirmer().setText("Confirmer Faritany");
            }
        }
    }

    public List<BureauVote> getBureauxFromDistrika(Distrika d) {
        return List.of(
                new BureauVote("BV101 - " + d.getNomDistrika(), d.getListDepute(), d),
                new BureauVote("BV102 - " + d.getNomDistrika(), d.getListDepute(), d),
                new BureauVote("BV201 - " + d.getNomDistrika(), d.getListDepute(), d),
                new BureauVote("BV202 - " + d.getNomDistrika(), d.getListDepute(), d),
                new BureauVote("BV301 - " + d.getNomDistrika(), d.getListDepute(), d),
                new BureauVote("BV302 - " + d.getNomDistrika(), d.getListDepute(), d)
        );
    }

    private void showMessage(String message) {
        javax.swing.JOptionPane.showMessageDialog(fenetre, message, "Attention", javax.swing.JOptionPane.WARNING_MESSAGE);
    }
}
