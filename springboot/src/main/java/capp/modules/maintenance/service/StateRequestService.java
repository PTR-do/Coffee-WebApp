package capp.modules.maintenance.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import capp.modules.maintenance.dto.DistributorStateListXmlDTO;
import capp.modules.maintenance.dto.DistributorStateXmlDTO;
import capp.domain.entity.Distributor;
import capp.domain.repository.DistributorRepository;

@Service
public class StateRequestService {
    private final DistributorRepository distributorRepository;
    public StateRequestService(DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
    }

    public DistributorStateListXmlDTO sendDistributorStateList(String id) {
        Long distributorId = Long.parseLong(id);
        if (!distributorRepository.existsById(distributorId)) {
            return new DistributorStateListXmlDTO(new ArrayList<>());
        }
        List<Distributor> list = distributorRepository.findAll();
        List<DistributorStateXmlDTO> dtoList = list.stream()
                .map(ds -> {
                    DistributorStateXmlDTO dto = new DistributorStateXmlDTO();
                    dto.setId(String.valueOf(ds.getId()));
                    dto.setState(ds.getState());
                    dto.setStateTime(ds.getStateTime());
                    dto.setUpdateMaintainer(ds.getUpdateMaintainer());
                    DistributorStateXmlDTO.ErrorsXmlDTO errors = new DistributorStateXmlDTO.ErrorsXmlDTO();
                    errors.e1 = (ds.getE1() != null && ds.getE1()) ? 1 : 0;
                    errors.e2 = (ds.getE2() != null && ds.getE2()) ? 1 : 0;
                    errors.e3 = (ds.getE3() != null && ds.getE3()) ? 1 : 0;
                    errors.e4 = (ds.getE4() != null && ds.getE4()) ? 1 : 0;
                    errors.e5 = (ds.getE5() != null && ds.getE5()) ? 1 : 0;
                    dto.setErrors(errors);
                    DistributorStateXmlDTO.SuppliesXmlDTO supplies = new DistributorStateXmlDTO.SuppliesXmlDTO();
                    supplies.bicchierini = ds.getBicchierini();
                    supplies.palettine = ds.getPalettine();
                    supplies.acqua = formatQuantity(ds.getAcqua());
                    supplies.zucchero = formatQuantity(ds.getZucchero());
                    supplies.caffe = formatQuantity(ds.getCaffe());
                    supplies.latte = formatQuantity(ds.getLatte());
                    supplies.ginseng = ds.getGinseng();
                    supplies.cioccolata = ds.getCioccolata();
                    supplies.vaniglia = ds.getVaniglia();
                    dto.setSupplies(supplies);
                    return dto;
                })
                .collect(Collectors.toList());
        return new DistributorStateListXmlDTO(dtoList);
    }

    private Double formatQuantity(Double value) {
        if (value == null) return 0.0;
        return Double.valueOf(String.format(java.util.Locale.US, "%.2f", value));
    }

}