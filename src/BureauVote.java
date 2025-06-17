package elections;

import java.util.List;
import candidat.Depute;
import delimitation.Distrika;

public class BureauVote {

    String nom;
    List<Depute> listDeputes;
    Distrika distrika;

    public BureauVote(String nom, List<Depute> list, Distrika d) {
        this.nom = nom;
        this.listDeputes = list;
        this.distrika = d;
    }

    public String getNomBV() {
        return this.nom;
    }

    public List<Depute> getDeputeBV() {
        return this.listDeputes;
    }

    public Distrika getDistrikaBV() {
        return this.distrika;
    }

    @Override
    public String toString() {
        return nom;
    }
}
