package elections;

import java.io.*;
import java.util.*;

public class Result {

    public List<Vote> lireVotes(String filename) {
        List<Vote> liste = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.trim().isEmpty() || ligne.startsWith("#")) {
                    continue;
                }

                String[] parts = ligne.split("\\|");
                if (parts.length != 7) {
                    System.err.println("Ligne invalide : " + ligne);
                    continue;
                }

                String f = parts[0].trim();
                String r = parts[1].trim();
                String d = parts[2].trim();
                int nbPeutetreElus = Integer.parseInt(parts[3].trim());
                String deputeStr = parts[4].trim();
                String bv = parts[5].trim();
                int n = Integer.parseInt(parts[6].trim());

                liste.add(new Vote(f, r, d, deputeStr, bv, nbPeutetreElus, n));
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
        List<Vote> filtres = new ArrayList<>();
        for (Vote v : votes) {
            if (v.getDistrika().equalsIgnoreCase(distrika)) {
                filtres.add(v);
            }
        }
        return filtres;
    }

    public String trouverEluDansDistrika(List<Vote> votesDuDistrika) {
        List<String> titulaires = new ArrayList<>();
        List<String> suppleants = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();

        if (votesDuDistrika.isEmpty()) {
            return "Aucun vote trouvé";
        }

        int nbElus = votesDuDistrika.get(0).getPeutEtreElus();

        for (Vote v : votesDuDistrika) {
            String nom = v.getTitulaire();
            int index = titulaires.indexOf(nom);
            if (index == -1) {
                titulaires.add(nom);
                suppleants.add(v.getSecond());
                scores.add(v.getNombreVote());
            } else {
                scores.set(index, scores.get(index) + v.getNombreVote());
            }
        }

        if (titulaires.isEmpty()) {
            return "Aucun vote trouvé";
        }

        int first = -1, second = -1;
        for (int i = 0; i < scores.size(); i++) {
            if (first == -1 || scores.get(i) > scores.get(first)) {
                second = first;
                first = i;
            } else if (second == -1 || scores.get(i) > scores.get(second)) {
                second = i;
            }
        }

        if (nbElus == 1 || second == -1) {
            return titulaires.get(first) + " (" + scores.get(first) + " votes)";
        }

        int votePremier = scores.get(first);
        int voteSecond = scores.get(second);

        if (voteSecond * 2 > votePremier) {
            return titulaires.get(first) + " (" + votePremier + " votes) et "
                    + titulaires.get(second) + " (" + voteSecond + " votes)";
        } else {
            return titulaires.get(first) + " (" + votePremier + " votes) et son suppléant "
                    + suppleants.get(first);
        }
    }

    public Map<String, Integer> getNbElusParParti(List<Vote> votesDuDistrika) {
        Map<String, Integer> result = new HashMap<>();
        if (votesDuDistrika.isEmpty()) {
            return result;
        }

        int nbElus = votesDuDistrika.get(0).getPeutEtreElus();

        List<String> titulaires = new ArrayList<>();
        List<String> suppleants = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();

        for (Vote v : votesDuDistrika) {
            String titulaire = v.getTitulaire();
            int index = titulaires.indexOf(titulaire);
            if (index == -1) {
                titulaires.add(titulaire);
                suppleants.add(v.getSecond());
                scores.add(v.getNombreVote());
            } else {
                scores.set(index, scores.get(index) + v.getNombreVote());
            }
        }

        for (int i = 0; i < scores.size() - 1; i++) {
            for (int j = i + 1; j < scores.size(); j++) {
                if (scores.get(j) > scores.get(i)) {
                    int tmpScore = scores.get(i);
                    scores.set(i, scores.get(j));
                    scores.set(j, tmpScore);

                    String tmpTit = titulaires.get(i);
                    titulaires.set(i, titulaires.get(j));
                    titulaires.set(j, tmpTit);

                    String tmpSup = suppleants.get(i);
                    suppleants.set(i, suppleants.get(j));
                    suppleants.set(j, tmpSup);
                }
            }
        }

        for (int i = 0; i < titulaires.size(); i++) {
            result.put(titulaires.get(i), 0);
        }

        if (nbElus == 1 || titulaires.size() == 1) {
            result.put(titulaires.get(0), 1);
        } else {
            int vote1 = scores.get(0);
            int vote2 = scores.get(1);

            if (vote2 * 2 > vote1) {
                result.put(titulaires.get(0), 1);
                result.put(titulaires.get(1), 1);
            } else {
                result.put(titulaires.get(0), 2);
                result.put(titulaires.get(1), 0);
            }
        }

        return result;
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
