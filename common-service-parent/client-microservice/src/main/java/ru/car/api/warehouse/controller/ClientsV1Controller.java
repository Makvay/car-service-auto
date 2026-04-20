package ru.car.api.warehouse.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.car.api.warehouse.service.ClientService;
import ru.car.dto.client.ClientCarDto;
import ru.car.dto.client.ClientDto;
import ru.car.dto.client.CreateClientCarRequest;
import ru.car.dto.client.CreateClientRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clients")
public class ClientsV1Controller {

    private final ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<ClientV1Dto>> list() {
        List<ClientDto> clients = clientService.getAllClients();
        List<ClientV1Dto> payload = clients.stream()
                .map(client -> toDto(client, resolvePrimaryCar(clientService.getClientVehicles(client.getId()))))
                .toList();
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientV1Dto> get(@PathVariable Long id) {
        ClientDto client = clientService.getClient(id);
        List<ClientCarDto> cars = clientService.getClientVehicles(id);
        return ResponseEntity.ok(toDto(client, resolvePrimaryCar(cars)));
    }

    @PostMapping("/")
    public ResponseEntity<ClientV1Dto> create(@Valid @RequestBody UpsertClientV1Request request) {
        NameParts name = splitName(request.getName());

        CreateClientRequest createClientRequest = new CreateClientRequest();
        createClientRequest.setFirstName(name.firstName());
        createClientRequest.setLastName(name.lastName());
        createClientRequest.setPhone(request.getPhone());
        createClientRequest.setEmail(request.getEmail());

        ClientDto saved = clientService.createClient(createClientRequest);
        ClientCarDto car = upsertPrimaryCar(saved.getId(), request.getCarModel(), request.getCarNumber());

        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved, car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientV1Dto> update(@PathVariable Long id, @Valid @RequestBody UpsertClientV1Request request) {
        NameParts name = splitName(request.getName());

        CreateClientRequest updateClientRequest = new CreateClientRequest();
        updateClientRequest.setFirstName(name.firstName());
        updateClientRequest.setLastName(name.lastName());
        updateClientRequest.setPhone(request.getPhone());
        updateClientRequest.setEmail(request.getEmail());

        ClientDto saved = clientService.updateClient(id, updateClientRequest);
        ClientCarDto car = upsertPrimaryCar(saved.getId(), request.getCarModel(), request.getCarNumber());

        return ResponseEntity.ok(toDto(saved, car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    private ClientCarDto upsertPrimaryCar(Long clientId, String carModel, String carNumber) {
        List<ClientCarDto> existingCars = clientService.getClientVehicles(clientId);
        ClientCarDto primary = resolvePrimaryCar(existingCars);

        if ((carModel == null || carModel.isBlank()) && (carNumber == null || carNumber.isBlank())) {
            return primary;
        }

        if (primary == null) {
            CreateClientCarRequest createCarRequest = new CreateClientCarRequest();
            createCarRequest.setClientId(clientId);
            createCarRequest.setVin(generateVin17());
            createCarRequest.setModel(carModel);
            createCarRequest.setLicensePlate(carNumber);
            createCarRequest.setMileage(0);
            return clientService.createVehicle(createCarRequest);
        }

        return primary;
    }

    private ClientV1Dto toDto(ClientDto client, ClientCarDto primaryCar) {
        ClientV1Dto dto = new ClientV1Dto();
        dto.setId(client.getId());
        dto.setName((nullToEmpty(client.getFirstName()) + " " + nullToEmpty(client.getLastName())).trim());
        dto.setPhone(client.getPhone());
        dto.setEmail(client.getEmail());
        dto.setCarModel(primaryCar != null ? primaryCar.getModel() : null);
        dto.setCarNumber(primaryCar != null ? primaryCar.getLicensePlate() : null);
        return dto;
    }

    private ClientCarDto resolvePrimaryCar(List<ClientCarDto> cars) {
        if (cars == null || cars.isEmpty()) {
            return null;
        }
        return cars.get(cars.size() - 1);
    }

    private static NameParts splitName(String name) {
        String trimmed = name == null ? "" : name.trim();
        if (trimmed.isEmpty()) {
            return new NameParts("-", "-");
        }
        String[] parts = trimmed.split("\\s+", 2);
        if (parts.length == 1) {
            return new NameParts(parts[0], "-");
        }
        return new NameParts(parts[0], parts[1]);
    }

    private static String generateVin17() {
        String raw = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return raw.substring(0, 17);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private record NameParts(String firstName, String lastName) {
    }

    @Data
    public static class ClientV1Dto {
        private Long id;
        private String name;
        private String phone;
        private String email;
        private String carModel;
        private String carNumber;
    }

    @Data
    public static class UpsertClientV1Request {
        @NotBlank
        @Size(max = 200)
        private String name;

        @NotBlank
        @Size(max = 20)
        private String phone;

        @Email
        @Size(max = 255)
        private String email;

        @Size(max = 100)
        private String carModel;

        @Size(max = 20)
        private String carNumber;
    }
}
