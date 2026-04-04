package ru.car.api.master.service;

import ru.car.dto.master.CreateMasterRequest;
import ru.car.dto.master.MasterDto;

import java.util.List;

public interface MasterService {
    MasterDto createMaster(CreateMasterRequest request);
    MasterDto getMasterById(Long id);
    MasterDto getMasterByEmployeeCode(String employeeCode);
    List<MasterDto> getAllMasters();
    List<MasterDto> getActiveMasters();
    List<MasterDto> getMastersBySpecialization(String specialization);
    MasterDto updateMaster(Long id, CreateMasterRequest request);
    void deactivateMaster(Long id);
    void activateMaster(Long id);
}
