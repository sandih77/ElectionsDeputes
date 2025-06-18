package delimitation;

import candidat.Depute;
import java.util.List;
import elections.BureauVote;

public class Distrika {

    String nom;
    Faritra faritra;
    List<Depute> listDeputes;
    List<BureauVote> bureauVote;
    int nbCandidatDeputesPeutEtreElus;

    public Distrika(String nom, Faritra faritra, List<Depute> listDeputes, int nbCandidatDeputesPeutEtreElus) {
        this.nom = nom;
        this.faritra = faritra;
        this.listDeputes = listDeputes;
        this.nbCandidatDeputesPeutEtreElus = nbCandidatDeputesPeutEtreElus;
    }

    public List<BureauVote> getListBureauVote() {
        return this.bureauVote;
    }

    public String getNomDistrika() {
        return this.nom;
    }

    @Override
    public String toString() {
        return this.nom;
    }

    public Faritra getFaritraDistrika() {
        return this.faritra;
    }

    public void setFaritraDistrika(Faritra f) {
        this.faritra = f;
    }

    public int getNbDeputes() {
        return this.nbCandidatDeputesPeutEtreElus;
    }

    public List<Depute> getListDepute() {
        return this.listDeputes;
    }

    // Setter si besoin
    public void setListDepute(List<Depute> listDepute) {
        this.listDeputes = listDepute;
    }
}
