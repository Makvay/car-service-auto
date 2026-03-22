package ru.car.api.warehouse.service;

import ru.car.dto.client.ClientCarDto;
import ru.car.dto.client.ClientDto;
import ru.car.dto.client.CreateClientCarRequest;
import ru.car.dto.client.CreateClientRequest;

import java.util.List;

public interface ClientService {

    ClientDto createClient(CreateClientRequest request);

    ClientDto getClient(Long id);

    ClientCarDto createVehicle(CreateClientCarRequest request);

    List<ClientCarDto> getClientVehicles(Long clientId);

    ClientCarDto updateMileage(Long vehicleId, Integer mileage);




}
