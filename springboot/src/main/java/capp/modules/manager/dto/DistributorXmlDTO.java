package capp.modules.manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

import capp.domain.entity.State;

public class DistributorXmlDTO {
    private Long id;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private State state;

    public DistributorXmlDTO(Long id, String location, LocalDate date, State state) {
        setId(id);
        setLocation(location);
        setDate(date);
        setState(state);
    }
    public DistributorXmlDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

}

