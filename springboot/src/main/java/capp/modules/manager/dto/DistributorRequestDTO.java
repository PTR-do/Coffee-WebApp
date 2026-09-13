package capp.modules.manager.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import capp.domain.entity.State;

public class DistributorRequestDTO {
    @NotBlank(message = "La locazione è obbligatoria")
    private String location;
    @NotNull(message = "La data è obbligatoria")
    private LocalDate date;
    @NotNull(message = "Lo stato del distributore è obbligatoria")
    private State state;
    @NotNull(message = "La latitudine è obbligatoria")
    private double lat;
    @NotNull(message = "La longitudine è obbligatoria")
    private double lng;

    public DistributorRequestDTO(String location, LocalDate date, State state, double lat, double lng) {
        setLocation(location);
        setDate(date);
        setState(state);
        setLat(lat);
        setLng(lng);
    }
    public DistributorRequestDTO() {
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }
}
