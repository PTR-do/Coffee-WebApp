package capp.modules.synchronization.dto;

import capp.domain.entity.State;

public record DistributorDTO(Long id, State state, String location, Double lat, Double lng) {}
