package candidat;

import delimitation.Distrika;

public class Depute {

    String nom;
    Distrika candidatDistrika;

    public Depute(String nom, Distrika candidatDistrika) {
        this.nom = nom;
        this.candidatDistrika = candidatDistrika;
    }

    public String getNomDepute() {
        return this.nom;
    }

    public Distrika getDistrikaCandidat() {
        return this.candidatDistrika;
    }

    public void setDistrikaCandidat(Distrika d) {
        this.candidatDistrika = d;
    }

    @Override
    public String toString() {
        return nom;
    }
}
