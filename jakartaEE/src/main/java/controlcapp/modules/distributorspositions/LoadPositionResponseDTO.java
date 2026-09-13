package controlcapp.modules.distributorspositions;

import java.util.List;

public class LoadPositionResponseDTO {
    public boolean result;
    public List<PointDTO> data;
    public LoadPositionResponseDTO(boolean result, List<PointDTO> data) {
        this.result = result;
        this.data = data;
    }
}
