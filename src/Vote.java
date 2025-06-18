package elections;

public class Vote {
    String faritany;
    String faritra;
    String distrika;
    String depute;
    String bureauVote;
    int nombre;

    public Vote(String faritany, String faritra, String distrika, String depute, String bureauVote, int nombre) {
        this.faritany = faritany;
        this.faritra = faritra;
        this.distrika = distrika;
        this.depute = depute;
        this.bureauVote = bureauVote;
        this.nombre = nombre;
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

    public String getDepute() {
        return depute;
    }

    public String getBureauVote() {
        return bureauVote;
    }

    public int getNombre() {
        return nombre;
    }
}
