package ru.car.api.claim.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.car.dto.nsi.ServiceDto;
import ru.car.dto.nsi.VehicleType;

import java.util.List;

@FeignClient(name = "nsi-service", url = "http://localhost:8086")
public interface NsiFeignClient {

    @GetMapping("/api/nsi/services")
    List<ServiceDto> getServices();

    @GetMapping("/api/nsi/vehicle-brands")
    List<?> getVehicleBrands();

    @GetMapping("/api/nsi/part-categories")
    List<?> getPartCategories();

}
