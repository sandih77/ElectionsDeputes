package elections;

import javax.swing.JComboBox;
import java.io.*;
import java.util.*;

public class Result {

    JComboBox listFaritany;
    JComboBox listFaritra;
    JComboBox listDistrika;

    public List<Vote> lireVotes(String filename) {
        List<Vote> liste = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.trim().isEmpty() || ligne.startsWith("#")) {
                    continue;
                }

                String[] parts = ligne.split("\\|");
                if (parts.length != 6) {
                    continue;
                }

                String f = parts[0].trim();
                String r = parts[1].trim();
                String d = parts[2].trim();
                String dep = parts[3].trim();
                int nbPeutetreElus = Integer.parseInt(parts[4].trim());
                String bv = parts[5].trim();
                int n = Integer.parseInt(parts[6].trim());

                liste.add(new Vote(f, r, d, dep, bv, nbPeutetreElus, n));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return liste;
    }

    public List<String> getNomsDistrika(List<Vote> votes) {
        List<String> noms = new ArrayList<>();
        for (Vote v : votes) {
            if (!noms.contains(v.getDistrika())) {
                noms.add(v.getDistrika());
            }
        }
        return noms;
    }

    public List<Vote> filtrerVotesParDistrika(List<Vote> votes, String distrika) {
        List<Vote> filtrés = new ArrayList<>();
        for (Vote v : votes) {
            if (v.getDistrika().equalsIgnoreCase(distrika)) {
                filtrés.add(v);
            }
        }
        return filtrés;
    }

    public String trouverEluDansDistrika(List<Vote> votesDuDistrika) {
        List<String> deputes = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();

        for (Vote v : votesDuDistrika) {
            int index = deputes.indexOf(v.getDepute());
            if (index == -1) {
                deputes.add(v.getDepute());
                scores.add(v.getNombre());
            } else {
                scores.set(index, scores.get(index) + v.getNombre());
            }
        }

        int max = -1;
        String elu = "";
        for (int i = 0; i < deputes.size(); i++) {
            if (scores.get(i) > max) {
                max = scores.get(i);
                elu = deputes.get(i);
            }
        }

        return elu + " (" + max + " votes)";
    }

    public void afficherElusParDistrika(String filename) {
        List<Vote> tousLesVotes = lireVotes(filename);
        List<String> distrikas = getNomsDistrika(tousLesVotes);

        System.out.println("=== Résultats par Distrika ===");
        for (String d : distrikas) {
            List<Vote> votesDuDistrika = filtrerVotesParDistrika(tousLesVotes, d);
            String elu = trouverEluDansDistrika(votesDuDistrika);
            System.out.println("Distrika : " + d + " => Élu : " + elu);
        }
    }

    public List<String> getNomsFaritany(List<Vote> votes) {
        List<String> noms = new ArrayList<>();
        for (Vote v : votes) {
            if (!noms.contains(v.getFaritany())) {
                noms.add(v.getFaritany());
            }
        }
        return noms;
    }

    public List<String> getNomsFaritraDansFaritany(List<Vote> votes, String faritany) {
        List<String> noms = new ArrayList<>();
        for (Vote v : votes) {
            if (v.getFaritany().equalsIgnoreCase(faritany) && !noms.contains(v.getFaritra())) {
                noms.add(v.getFaritra());
            }
        }
        return noms;
    }

    public List<String> getNomsDistrikaDansFaritra(List<Vote> votes, String faritany, String faritra) {
        List<String> noms = new ArrayList<>();
        for (Vote v : votes) {
            if (v.getFaritany().equalsIgnoreCase(faritany)
                    && v.getFaritra().equalsIgnoreCase(faritra)
                    && !noms.contains(v.getDistrika())) {
                noms.add(v.getDistrika());
            }
        }
        return noms;
    }
}
