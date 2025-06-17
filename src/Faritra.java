package delimitation;

import java.util.List;

public class Faritra {

    String nom;
    Faritany faritany;
    List<Distrika> list_Distrika;

    public Faritra(String nom, Faritany faritany, List<Distrika> list_Distrika) {
        this.nom = nom;
        this.faritany = faritany;
        this.list_Distrika = list_Distrika;
    }

    public String getNomFaritra() {
        return this.nom;
    }

    public Faritany getFaritanyFaritra() {
        return this.faritany;
    }

    public void setFaritanyFaritra(Faritany f) {
        this.faritany = f;
    }

    public List<Distrika> getListDistrika() {
        return this.list_Distrika;
    }

    @Override
    public String toString() {
        return nom;
    }
}
