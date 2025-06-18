package elections;

public class Vote {

    String faritany;
    String faritra;
    String distrika;
    String titulaire;
    String suppleant;
    String bureauVote;
    int nbCandidatDeputesPeutEtreElus;
    int nombreVotes;

    public Vote(String faritany, String faritra, String distrika, String candidat, String bureauVote, int nbElus, int nombreVotes) {
        this.faritany = faritany;
        this.faritra = faritra;
        this.distrika = distrika;
        this.bureauVote = bureauVote;
        this.nbCandidatDeputesPeutEtreElus = nbElus;
        this.nombreVotes = nombreVotes;

        String[] split = candidat.split(":");
        this.titulaire = split[0].trim();
        if (split.length > 1) {
            this.suppleant = split[1].trim();
        } else {
            this.suppleant = "";
        }
    }

    public String getFaritany() {
        return faritany;
    }

    public String getFaritra() {
        return faritra;
    }

    public String getDistrika() {
        return distrika;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public String getSecond() {
        return suppleant;
    }

    public String getBureauVote() {
        return bureauVote;
    }

    public int getNombre() {
        return nombreVotes;
    }

    public int getPeutEtreElus() {
        return nbCandidatDeputesPeutEtreElus;
    }
}
