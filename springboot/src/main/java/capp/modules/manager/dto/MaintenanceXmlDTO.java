package capp.modules.manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class MaintenanceXmlDTO {
    private Long id;
    private String name;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    // costruttore vuoto: necessario per Jackson
    public MaintenanceXmlDTO() {
    }
    public MaintenanceXmlDTO(Long id, String name, String location, LocalDate date) {
        setId(id);
        setName(name);
        setLocation(location);
        setDate(date);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}

