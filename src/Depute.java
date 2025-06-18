package candidat;

import delimitation.Distrika;

public class Depute {

    String nom;
    Distrika candidatDistrika;
    Depute secondMembre;

    public Depute(String nom, Depute secondDepute, Distrika candidatDistrika) {
        this.nom = nom;
        this.secondMembre = secondDepute;
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

    public Depute getSecondMembre() {
        return this.secondMembre;
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
