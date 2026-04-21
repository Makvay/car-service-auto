package ru.car.api.claim.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.car.dto.warehouse.DeductRequest;

@FeignClient(name = "warehouse-service", url = "${services.warehouse.url:http://localhost:8084}")
public interface WarehouseFeignClient {

    @PostMapping("/api/v1/inventory/deduct")
    void deductInventory(@RequestBody DeductRequest request);
}
