package delimitation;

import java.util.List;

public class Faritany {

    String nom;
    List<Faritra> list_Faritra;

    public Faritany(String nom, List<Faritra> listFaritra) {
        this.nom = nom;
        this.list_Faritra = listFaritra;
    }

    public String getNomFaritany() {
        return this.nom;
    }

    public List<Faritra> getListFaritra() {
        return this.list_Faritra;
    }

    @Override
    public String toString() {
        return nom;
    }
}
