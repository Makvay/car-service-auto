package ru.car.api.master.service;

import ru.car.dto.master.CreateMasterRequest;
import ru.car.dto.master.MasterDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MasterService {
    MasterDto createMaster(CreateMasterRequest request);
    MasterDto getMasterById(Long id);
    MasterDto getMasterByEmployeeCode(String employeeCode);
    Page<MasterDto> getAllMasters(Pageable pageable);
    Page<MasterDto> getActiveMasters(Pageable pageable);
    Page<MasterDto> getMastersBySpecialization(String specialization, Pageable pageable);
    MasterDto updateMaster(Long id, CreateMasterRequest request);
    void deactivateMaster(Long id);
    void activateMaster(Long id);
}
