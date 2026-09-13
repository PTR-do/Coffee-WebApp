package capp.modules.manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MaintenanceRequestDTO {
    @NotBlank(message = "Il nome del manutentore è obbligatoria")
    private String name;
    @NotNull(message = "La locazione è obbligatoria")
    private String location;
    @NotNull(message = "La data è obbligatoria")
    private LocalDate date;
    @NotNull(message = "La password è obbligatoria")
    private String password;

    // costruttore vuoto: necessario per Jackson
    public MaintenanceRequestDTO() {
    }
    public MaintenanceRequestDTO(String name, String location, LocalDate date, String password) {
        setName(name);
        setLocation(location);
        setDate(date);
        setPassword(password);
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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

