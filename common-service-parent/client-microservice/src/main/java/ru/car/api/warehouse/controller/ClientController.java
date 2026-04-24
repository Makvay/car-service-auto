package ru.car.api.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.car.api.warehouse.service.ClientService;
import ru.car.dto.client.ClientCarDto;
import ru.car.dto.client.ClientDto;
import ru.car.dto.client.CreateClientCarRequest;
import ru.car.dto.client.CreateClientRequest;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping({"/api/client", "/api/clients"})
@Deprecated(forRemoval = false)
@Tag(name = "Client Management", description = "API for client and car")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "Создать клиента")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Клиент успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody CreateClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить клиента по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Клиент найден"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClientDto> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @PostMapping("/vehicle")
    @Operation(summary = "Добавить автомобиль клиенту")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Автомобиль успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClientCarDto> createVehicle(@Valid @RequestBody CreateClientCarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createVehicle(request));
    }


    @GetMapping("/{clientId}/vehicles")
    @Operation(summary = "Получить список автомобилей клиента")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список автомобилей"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<ClientCarDto>> getClientVehicles(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.getClientVehicles(clientId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные клиента")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Клиент успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClientDto> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody CreateClientRequest request) {
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }

    @PutMapping("/vehicle/{vehicleId}/mileage")
    @Operation(summary = "Обновить пробег автомобиля")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пробег успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "404", description = "Автомобиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClientCarDto> updateMileage(@PathVariable Long vehicleId, @RequestParam Integer mileage) {
        return ResponseEntity.ok(clientService.updateMileage(vehicleId, mileage));
    }
}
