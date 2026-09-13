package capp.modules.distributor.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDateTime;

import capp.domain.entity.Connection;
import capp.domain.entity.Consumer;
import capp.domain.repository.ConnectionRepository;
import capp.domain.repository.ConsumerRepository;
import capp.modules.distributor.dto.*;
import capp.domain.entity.Distributor;
import capp.domain.repository.DistributorRepository;

@Service
public class DrinkVerificationService {
    private final ConnectionRepository connectionRepository;
    private final ConsumerRepository consumerRepository;
    private final DistributorRepository distributorRepository;
    public DrinkVerificationService(ConnectionRepository connectionRepository,
                                        ConsumerRepository consumerRepository,
                                        DistributorRepository distributorRepository) {
        this.connectionRepository = connectionRepository;
        this.consumerRepository = consumerRepository;
        this.distributorRepository = distributorRepository;
    }

    @Transactional
    public String allowDrink(SelectionRequestDTO request) {
        Long idDistributor = Long.parseLong(request.id());
        Optional<Connection> connectionOpt = connectionRepository.findById(idDistributor);
        if (connectionOpt.isEmpty())
            return "Not found";
        Connection connection = connectionOpt.get();
        LocalDateTime now = LocalDateTime.now();
        if (connectionRepository.isExpired(idDistributor, now) || connection.getUsername().isEmpty())
            return "Not found";
        Optional<Consumer> consumerOpt = consumerRepository.findById(connection.getUsername());
        if (consumerOpt.isEmpty())
            return "Not found";
        Consumer consumer = consumerOpt.get();
        Optional<Distributor> stateOpt = distributorRepository.findById(idDistributor);
        if (stateOpt.isEmpty())
            return "Not found";
        Distributor state = stateOpt.get();

        int quantity;
        quantity = Integer.parseInt(request.drinkNumber());
        if (quantity <= 0 || quantity >5) return "Invalid quantity";

        int sugarQty;
        sugarQty = Integer.parseInt(request.sugar());
        if (sugarQty < 0 || sugarQty > 3) return "Invalid quantity";
        double zucchero = sugarQty * 0.005 * quantity;

        double price;
        int bicchierini = quantity;
        int palettine = quantity;
        double acqua = 0;
        double caffe = 0;
        double latte = 0;
        int ginseng = 0;
        int cioccolata = 0;
        int vaniglia = 0;

        switch (request.drink()) {
            case "Espresso" -> { price = 0.60 * quantity; acqua = 0.05 * quantity; caffe = 0.007 * quantity; }
            case "Lungo" -> { price = 0.60 * quantity; acqua = 0.10 * quantity; caffe = 0.007 * quantity; }
            case "Cappuccino" -> { price = 0.80 * quantity; acqua = 0.05 * quantity; caffe = 0.007 * quantity; latte = 0.15 * quantity; }
            case "Latte Macchiato" -> { price = 0.80 * quantity; acqua = 0.05 * quantity; caffe = 0.005 * quantity; latte = 0.20 * quantity; }
            case "Ginseng" -> { price = 0.80 * quantity; acqua = 0.05 * quantity; ginseng = 20 * quantity; }
            case "Ginseng lungo" -> { price = 0.80 * quantity; acqua = 0.10 * quantity; ginseng = 25 * quantity; }
            case "Mocha" -> { price = 1.00 * quantity; acqua = 0.05 * quantity; caffe = 0.007 * quantity; cioccolata = 15 * quantity; }
            case "Vaniglia" -> { price = 1.00 * quantity; acqua = 0.05 * quantity; caffe = 0.007 * quantity; vaniglia = 10 * quantity; }
            default -> { return "error supply"; }
        }

        if (consumer.getCredit() < price) return "error supply";

        if (state.getBicchierini() < bicchierini) return "error supply";
        if (state.getPalettine() < palettine) return "error supply";
        if (state.getAcqua() < acqua) return "error supply";
        if (state.getZucchero() < zucchero) return "error supply";
        if (state.getCaffe() < caffe) return "error supply";
        if (state.getLatte() < latte) return "error supply";
        if (state.getGinseng() < ginseng) return "error supply";
        if (state.getCioccolata() < cioccolata) return "error supply";
        if (state.getVaniglia() < vaniglia) return "error supply";

        consumer.setCredit(consumer.getCredit() - price);
        state.setBicchierini(state.getBicchierini() - bicchierini);
        state.setPalettine(state.getPalettine() - palettine);
        state.setAcqua(state.getAcqua() - acqua);
        state.setZucchero(state.getZucchero() - zucchero);
        state.setCaffe(state.getCaffe() - caffe);
        state.setLatte(state.getLatte() - latte);
        state.setGinseng(state.getGinseng() - ginseng);
        state.setCioccolata(state.getCioccolata() - cioccolata);
        state.setVaniglia(state.getVaniglia() - vaniglia);
        consumerRepository.save(consumer);
        distributorRepository.save(state);
        return "done";
    }

}
