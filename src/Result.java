package elections;

import javax.swing.JComboBox;
import java.io.*;
import java.util.*;

public class Result {

    JComboBox listFaritany;
    JComboBox listFaritra;
    JComboBox listDistrika;

    // 1. Lire tous les votes depuis le fichier
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
                String bv = parts[4].trim();
                int n = Integer.parseInt(parts[5].trim());

                liste.add(new Vote(f, r, d, dep, bv, n));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return liste;
    }

    // 2. Récupère tous les noms de distrika sans doublons
    public List<String> getNomsDistrika(List<Vote> votes) {
        List<String> noms = new ArrayList<>();
        for (Vote v : votes) {
            if (!noms.contains(v.getDistrika())) {
                noms.add(v.getDistrika());
            }
        }
        return noms;
    }

    // 3. Filtre les votes pour un distrika donné
    public List<Vote> filtrerVotesParDistrika(List<Vote> votes, String distrika) {
        List<Vote> filtrés = new ArrayList<>();
        for (Vote v : votes) {
            if (v.getDistrika().equalsIgnoreCase(distrika)) {
                filtrés.add(v);
            }
        }
        return filtrés;
    }

    // 4. Trouver l'élu dans un distrika donné (le député avec le plus de votes)
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

    // 5. Affiche tous les élus pour chaque distrika
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

    // Renvoyer liste Faritany
    public List<String> getNomsFaritany(List<Vote> votes) {
        List<String> noms = new ArrayList<>();
        for (Vote v : votes) {
            if (!noms.contains(v.getFaritany())) {
                noms.add(v.getFaritany());
            }
        }
        return noms;
    }

// Faritra dans Faritany
    public List<String> getNomsFaritraDansFaritany(List<Vote> votes, String faritany) {
        List<String> noms = new ArrayList<>();
        for (Vote v : votes) {
            if (v.getFaritany().equalsIgnoreCase(faritany) && !noms.contains(v.getFaritra())) {
                noms.add(v.getFaritra());
            }
        }
        return noms;
    }

// Distrika dans Faritra
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
