package ru.car.api.nsi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.car.api.nsi.service.CarStampService;
import ru.car.api.nsi.service.PartCategoryService;
import ru.car.api.nsi.service.ServiceService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Совместимость с ТЗ фронтенда: GET /api/nsi/ -> агрегированный список справочников.
 */
@RestController
@RequestMapping("/api/nsi")
@RequiredArgsConstructor
public class NsiAggregateController {

    private final CarStampService carStampService;
    private final ServiceService serviceService;
    private final PartCategoryService partCategoryService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllNsi() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("vehicleBrands", carStampService.getAllActive());
        payload.put("services", serviceService.getAllActive());
        payload.put("partCategories", partCategoryService.getAllActive());
        return ResponseEntity.ok(payload);
    }
}

