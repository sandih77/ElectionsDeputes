package delimitation;

import candidat.Depute;
import java.util.List;

public class Distrika {

    String nom;
    Faritra faritra;
    List<Depute> listDeputes;
    int nbCandidatDeputesPeutEtreElus;

    public Distrika(String nom, Faritra faritra, List<Depute> listDeputes, int nbCandidatDeputesPeutEtreElus) {
        this.nom = nom;
        this.faritra = faritra;
        this.listDeputes = listDeputes;
        this.nbCandidatDeputesPeutEtreElus = nbCandidatDeputesPeutEtreElus;
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

    public List<Depute> getDeputeDistrika() {
        return this.listDeputes;
    }
}
