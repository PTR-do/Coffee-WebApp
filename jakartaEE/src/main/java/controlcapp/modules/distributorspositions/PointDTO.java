package controlcapp.modules.distributorspositions;

public class PointDTO {
    public double lat;
    public double lng;
    public String posizione;
    public String stato;

    public PointDTO(double lat, double lng, String posizione, String stato) {
        this.lat = lat;
        this.lng = lng;
        this.posizione = posizione;
        this.stato = stato;
    }
}

