package ru.car.api.claim.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.car.dto.client.ClientCarDto;
import ru.car.dto.client.ClientDto;

import java.util.List;

@FeignClient(name = "client-service", url = "${services.client.url:http://localhost:8082}")
public interface ClientFeignClient {

    @GetMapping("/api/v1/clients/{id}")
    ClientDto getClientById(@PathVariable Long id);

    @GetMapping("/api/client/{clientId}/vehicles")
    List<ClientCarDto> getClientVehicles(@PathVariable Long clientId);
}
