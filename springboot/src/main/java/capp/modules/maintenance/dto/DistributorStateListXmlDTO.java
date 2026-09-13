package capp.modules.maintenance.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

@JacksonXmlRootElement(localName = "distributorState", namespace = "http://www.data.com/distributor")
public class DistributorStateListXmlDTO {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "distributor", namespace = "http://www.data.com/distributor")
    private List<DistributorStateXmlDTO> distributor;

    public DistributorStateListXmlDTO() {}
    public DistributorStateListXmlDTO(List<DistributorStateXmlDTO> distributor) {
        this.distributor = distributor;
    }

    public List<DistributorStateXmlDTO> getDistributor() {
        return distributor;
    }
    public void setDistributor(List<DistributorStateXmlDTO> distributor) {
        this.distributor = distributor;
    }
}

