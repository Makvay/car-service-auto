package ru.car.api.warehouse.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.car.api.warehouse.mapper.PartMapper;
import ru.car.api.warehouse.repository.PartRepository;
import ru.car.api.warehouse.service.PartService;
import ru.car.dto.warehouse.CreatePartRequest;
import ru.car.dto.warehouse.PartDto;
import ru.car.entity.warehouse.PartEntity;

//        create() - сохранить новую запчасть
//        getById() - найти по ID
//        getAll() - вернуть все запчасти
//        update() - обновить данные
//        delete() - пометить как неактивную или удалить
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final PartMapper partMapper;


    @Override
    public PartDto create(CreatePartRequest request) {
        log.info("Создать запчасть {}", request);
        PartEntity entity = partMapper.toEntity(request);
        entity.setIsActive(true);
        PartEntity saved = partRepository.save(entity);
        return partMapper.toDto(saved);
    }

    @Override
    public PartDto getById(Long id) {
        log.info("Найти товар: {}", id);
        PartEntity part = partRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Error - товар не найден: " + id));
        return partMapper.toDto(part);
    }


    @Override
    public List<PartDto> getAll() {
        log.info("Получить список всех товаров, количество: {}",
                partRepository.findAll().size());
        return partMapper.toDtoList(partRepository.findAll());
    }

    @Override
    public PartDto update(Long id, PartDto dto) {
        log.info("Обновить данные товара с ID: {}", id);

        PartEntity part = partRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Error - товар не найден: " + id));

        part.setName(dto.getName());
        part.setUnitPrice(dto.getUnitPrice());
        part.setCostPrice(dto.getCostPrice());
        part.setDescription(dto.getDescription());
        part.setBrand(dto.getBrand());

        PartEntity updatePart = partRepository.save(part);

        log.info("Данные обновлены");

        return partMapper.toDto(updatePart);
    }

    @Override
    public void delete(Long id) {
        log.info("Удалить товар: {}", id);

        PartEntity part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден: " + id));
        //  помечаем как неактивный
        part.setIsActive(false);
        partRepository.save(part);

        partRepository.deleteById(id);
        log.info("Товар {} deleted ", id);
    }
}
