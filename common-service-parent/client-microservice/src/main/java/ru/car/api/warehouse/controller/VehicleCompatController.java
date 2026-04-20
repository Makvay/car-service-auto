package ru.car.api.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.car.api.warehouse.service.ClientService;
import ru.car.dto.client.ClientCarDto;
import ru.car.dto.client.CreateClientCarRequest;

@RestController
@RequiredArgsConstructor
public class VehicleCompatController {

    private final ClientService clientService;

    @PostMapping("/api/vehicles")
    public ResponseEntity<ClientCarDto> createVehicle(@Valid @RequestBody CreateClientCarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createVehicle(request));
    }
}
