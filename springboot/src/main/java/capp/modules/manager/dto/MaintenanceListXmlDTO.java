package capp.modules.manager.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

@JacksonXmlRootElement(localName = "maintenances")
public class MaintenanceListXmlDTO {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "maintenance")
    private List<MaintenanceXmlDTO> maintenance;
    public MaintenanceListXmlDTO() {    // costruttore vuoto: necessario per Jackson
    }
    public MaintenanceListXmlDTO(List<MaintenanceXmlDTO> maintenance) {
        this.maintenance = maintenance;
    }

    public List<MaintenanceXmlDTO> getMaintenance() {
        return maintenance;
    }
}

