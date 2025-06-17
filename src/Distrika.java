package delimitation;

import candidat.Depute;
import java.util.List;

public class Distrika {

    String nom;
    Faritra faritra;
    List<Depute> listDeputes;
    int nbDeputes;

    public Distrika(String nom, Faritra faritra, List<Depute> listDeputes, int nbDeputes) {
        this.nom = nom;
        this.faritra = faritra;
        this.listDeputes = listDeputes;
        this.nbDeputes = nbDeputes;
    }

    public String getNomDistrika() {
        return this.nom;
    }

    @Override
    public String toString() {
        return nom;
    }

    public Faritra getFaritraDistrika() {
        return this.faritra;
    }

    public void setFaritraDistrika(Faritra f) {
        this.faritra = f;
    }

    public int getNbDeputes() {
        return this.nbDeputes;
    }

    public List<Depute> getDeputeDistrika() {
        return this.listDeputes;
    }
}
