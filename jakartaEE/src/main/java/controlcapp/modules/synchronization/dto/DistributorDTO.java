package controlcapp.modules.synchronization.dto;

import controlcapp.domain.State;

public class DistributorDTO {
    private Long id;
    private String location;
    private State state;
    private Double lat;
    private Double lng;

    public DistributorDTO(Long id, String location, State state, Double lat, Double lng) {
        this.id = id;
        this.location = location;

        this.state = state;
        this.lat = lat;
        this.lng = lng;
    }
    public DistributorDTO() {}

    public Long getId() {
        return id;
    }
    public String getLocation() {
        return location;
    }
    public State getState() {
        return state;
    }
    public Double getLat() {
        return lat;
    }
    public Double getLng() {
        return lng;
    }
    public void setId(Long id) { this.id = id; }
    public void setLocation(String location) { this.location = location; }
    public void setState(State state) { this.state = state; }
    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }
}
