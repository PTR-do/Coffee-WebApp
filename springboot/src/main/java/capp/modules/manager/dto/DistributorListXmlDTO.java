package capp.modules.manager.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

@JacksonXmlRootElement(localName = "distributors")
public class DistributorListXmlDTO {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "distributor")
    private List<DistributorXmlDTO> distributor;
    public DistributorListXmlDTO() {    // costruttore vuoto: necessario per Jackson
    }
    public DistributorListXmlDTO(List<DistributorXmlDTO> distributor) {
        this.distributor = distributor;
    }

    public List<DistributorXmlDTO> getDistributor() {
        return distributor;
    }
}
