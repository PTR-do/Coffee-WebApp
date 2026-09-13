package controlcapp.domain;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.time.*;

@Entity("distributorControl")
public class DistributorDocument {
    @Id
    private Long id;
    private State state;
    private LocalDateTime time;
    private String location;
    private Coord coord;

    public DistributorDocument() {}
    public DistributorDocument(Long id, State state, LocalDateTime time, String location, Coord coord) {
        this.id = id;
        this.state = state;
        this.time = time;
        this.location = location;
        this.coord = coord;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Coord getCoord() { return coord; }
    public void setCoord(Coord coord) { this.coord = coord; }

    @Embedded
    public static class Coord {
        private double lat;
        private double lng;

        public Coord() {}

        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }

        public double getLng() { return lng; }
        public void setLng(double lng) { this.lng = lng; }
    }
}
