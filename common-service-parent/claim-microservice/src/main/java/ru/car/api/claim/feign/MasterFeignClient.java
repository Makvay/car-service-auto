package ru.car.api.claim.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.car.dto.master.MasterDto;

@FeignClient(name = "master-service", url = "${services.master.url:http://localhost:8083}", fallback = MasterFeignClientFallback.class)
public interface MasterFeignClient {

    @GetMapping("/api/v1/masters/{id}")
    MasterDto getMasterById(@PathVariable Long id);
}
