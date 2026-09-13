package capp.modules.maintenance.dto;

import capp.domain.entity.State;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class DistributorStateXmlDTO {
    private String id;
    private State state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime stateTime;
    private String updateMaintainer;
    private ErrorsXmlDTO errors;
    private SuppliesXmlDTO supplies;

    public static class ErrorsXmlDTO {
        public Integer e1, e2, e3, e4, e5;
    }

    public static class SuppliesXmlDTO {
        public Double  acqua, zucchero, caffe, latte;
        public Integer bicchierini, palettine, ginseng, cioccolata, vaniglia;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    public LocalDateTime getStateTime() { return stateTime; }
    public void setStateTime(LocalDateTime stateTime) { this.stateTime = stateTime; }
    public String getUpdateMaintainer() { return updateMaintainer; }
    public void setUpdateMaintainer(String updateMaintainer) { this.updateMaintainer = updateMaintainer; }
    public ErrorsXmlDTO getErrors() { return errors; }
    public void setErrors(ErrorsXmlDTO errors) { this.errors = errors; }
    public SuppliesXmlDTO getSupplies() { return supplies; }
    public void setSupplies(SuppliesXmlDTO supplies) { this.supplies = supplies; }
}