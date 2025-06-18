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
        return this.faritany;
    }

    public String getFaritra() {
        return this.faritra;
    }

    public String getDistrika() {
        return this.distrika;
    }

    public String getTitulaire() {
        return this.titulaire;
    }

    public String getSecond() {
        return this.suppleant;
    }

    public String getBureauVote() {
        return this.bureauVote;
    }

    public int getNombreVote() {
        return this.nombreVotes;
    }

    public int getPeutEtreElus() {
        return this.nbCandidatDeputesPeutEtreElus;
    }
}
