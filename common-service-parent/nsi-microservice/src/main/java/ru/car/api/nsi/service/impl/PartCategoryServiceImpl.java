package ru.car.api.nsi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.nsi.mapper.PartCategoryMapper;
import ru.car.api.nsi.repository.PartCategoryRepository;
import ru.car.api.nsi.service.PartCategoryService;
import ru.car.dto.nsi.PartCategoryDto;
import ru.car.entity.nsi.PartCategoryEntity;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartCategoryServiceImpl implements PartCategoryService {

    private final PartCategoryRepository partCategoryRepository;
    private final PartCategoryMapper partCategoryMapper;

    @Override
    public PartCategoryDto getById(Long id) {
        PartCategoryEntity entity = partCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part category not found with id: " + id));
        return partCategoryMapper.toDto(entity);
    }

    @Override
    public PartCategoryDto getByCode(String code) {
        PartCategoryEntity entity = partCategoryRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Part category not found with code: " + code));
        return partCategoryMapper.toDto(entity);
    }

    @Override
    public List<PartCategoryDto> getAll() {
        return partCategoryRepository.findAll().stream()
                .map(partCategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartCategoryDto> getAllActive() {
        return partCategoryRepository.findByIsActiveTrue().stream()
                .map(partCategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartCategoryDto> getRootCategories() {
        return partCategoryRepository.findByParentIdIsNull().stream()
                .map(partCategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartCategoryDto> getByParentId(Long parentId) {
        return partCategoryRepository.findByParentId(parentId).stream()
                .map(partCategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}