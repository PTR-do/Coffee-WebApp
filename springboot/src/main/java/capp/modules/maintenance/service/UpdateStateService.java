package capp.modules.maintenance.service;

import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import jakarta.transaction.Transactional;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;
import java.util.*;

import capp.domain.entity.Distributor;
import capp.domain.repository.DistributorRepository;
import capp.modules.maintenance.dto.DistributorStateListXmlDTO;
import capp.modules.maintenance.dto.DistributorStateXmlDTO;

@Service
public class UpdateStateService {
    private final DistributorRepository distributorRepository;
    public UpdateStateService(DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
    }

    @Transactional
    public boolean updateDistributorState(String xml) {
        try {
            // Validazione XSD
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(
                    new ClassPathResource("/ExampleXml/distributor_state.xsd").getInputStream()
            ));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));

            // Deserializzazione
            XmlMapper mapper = new XmlMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            DistributorStateListXmlDTO rootDto = mapper.readValue(xml, DistributorStateListXmlDTO.class);
            if (rootDto == null || rootDto.getDistributor() == null || rootDto.getDistributor().isEmpty()) {
                return false;
            }

            // Verifica
            DistributorStateXmlDTO dto = rootDto.getDistributor().getFirst();
            Long id = Long.valueOf(dto.getId());
            Optional<Distributor> optionalDistributor = distributorRepository.findById(id);
            if (optionalDistributor.isEmpty()) {
                return false;
            }
            // Aggiornamento DistributorState
            Distributor d = optionalDistributor.get();
            d.setNotify(false);
            d.setState(dto.getState());
            d.setStateTime(dto.getStateTime());
            d.setUpdateMaintainer(dto.getUpdateMaintainer());
            if (dto.getErrors() != null) {
                d.setE1(dto.getErrors().e1 == 1);
                d.setE2(dto.getErrors().e2 == 1);
                d.setE3(dto.getErrors().e3 == 1);
                d.setE4(dto.getErrors().e4 == 1);
                d.setE5(dto.getErrors().e5 == 1);
            }
            if (dto.getSupplies() != null && dto.getSupplies().bicchierini != -1) {
                var s = dto.getSupplies();
                d.setBicchierini(s.bicchierini);
                d.setPalettine(s.palettine);
                d.setAcqua(s.acqua);
                d.setZucchero(s.zucchero);
                d.setCaffe(s.caffe);
                d.setLatte(s.latte);
                d.setGinseng(s.ginseng);
                d.setCioccolata(s.cioccolata);
                d.setVaniglia(s.vaniglia);
            }

            distributorRepository.save(d);
            return true;

        } catch (Exception e) {
            org.springframework.transaction.interceptor.TransactionAspectSupport
                    .currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e);
        }
    }
}