package ru.car.api.claim.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.car.dto.master.MasterDto;

import java.util.List;

@FeignClient(name = "master-service", url = "http://localhost:8083")
public interface MasterFeignClient {

    @GetMapping("/api/masters/{id}")
    MasterDto getMasterById(@PathVariable Long id);

    @GetMapping("/api/v1/masters")
    List<MasterDto> getAllMasters();

}
