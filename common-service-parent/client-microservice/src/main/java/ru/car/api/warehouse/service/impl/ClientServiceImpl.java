package ru.car.api.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.car.api.warehouse.mapper.ClientCarMapper;
import ru.car.api.warehouse.mapper.ClientMapper;
import ru.car.api.warehouse.repository.ClientRepository;
import ru.car.api.warehouse.service.ClientService;
import ru.car.dto.client.*;
import ru.car.entity.client.ClientCarEntity;
import ru.car.entity.client.ClientEntity;
import ru.car.api.warehouse.kafka.KafkaProducer;
import ru.car.dto.client.ClientCreatedEvent;
import ru.car.api.warehouse.repository.ClientCarRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final KafkaProducer kafkaProducer;
    private final ClientCarRepository clientCarRepository;
    private final ClientCarMapper clientCarMapper;

    @Override
    public ClientDto createClient(CreateClientRequest request) {

        log.info("Создание клиента: {}", request.getFirstName());

        ClientEntity client = clientMapper.toEntity(request);
        ClientEntity savedClient = clientRepository.save(client);

        // Отправка в Kafka
        ClientCreatedEvent event = new ClientCreatedEvent(savedClient.getId(), savedClient.getPhone());
        kafkaProducer.send("client.registered", event);


        log.info("клиент создан: {}", savedClient.getFirstName());
        return clientMapper.toDto(savedClient);
    }

    @Override
    public ClientDto getClient(Long id) {
        log.info("получить клиента {} " , id);
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Клиент не найден: " + id));

        return clientMapper.toDto(client);
    }

    @Override
    public ClientCarDto createVehicle(CreateClientCarRequest request) {
        log.info("Добавить авто {} " , request.getModel());

        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(()-> new RuntimeException("Клиент не найден"));

        ClientCarEntity car = new ClientCarEntity();
        car.setClient(client);
        car.setVin(request.getVin());
        car.setBrandName(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setLicensePlate(request.getLicensePlate());
        car.setMileage(request.getMileage());

        ClientCarEntity savedCar = clientCarRepository.save(car);

        VehicleRegisteredEvent event = new VehicleRegisteredEvent(savedCar.getId(), client.getId(), savedCar.getVin());
        kafkaProducer.send("vehicle.register", event);

        log.info("Авто добавлено: {}", savedCar.getVin());
        return clientCarMapper.toDto(savedCar);
    }


    @Override
    public List<ClientCarDto> getClientVehicles(Long clientId) {
        log.info("Получить авто клиента {}" , clientId);

        List<ClientCarEntity> cars = clientCarRepository.findByClientId(clientId);
        return cars.stream()
                .map(clientCarMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientCarDto updateMileage(Long vehicleId, Integer mileage) {
        log.info("Обновление пробега машины {} на {}", vehicleId, mileage);

         ClientCarEntity car = clientCarRepository.findById(vehicleId)
                .orElseThrow(()-> new RuntimeException("Автомобиль не найден: " + vehicleId));

         car.setMileage(mileage);
         ClientCarEntity updateCar = clientCarRepository.save(car);

         // Kafka
        MileageUpdatedEvent event = new MileageUpdatedEvent(updateCar.getId(), updateCar.getMileage());
        kafkaProducer.send("mileage.updated", event);

        log.info("Пробег обновлен для машины {}: {}", vehicleId, mileage);
        return clientCarMapper.toDto(updateCar);

    }
}
