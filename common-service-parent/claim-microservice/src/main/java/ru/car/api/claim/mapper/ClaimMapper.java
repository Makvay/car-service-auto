package ru.car.api.claim.mapper;

import org.mapstruct.*;
import ru.car.dto.claim.ClaimDto;
import ru.car.dto.claim.CreateClaimRequest;
import ru.car.entity.claim.ClaimEntity;
import ru.car.entity.claim.ClaimPriority;

@Mapper(componentModel = "spring")
public interface ClaimMapper {

    // toDto
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "vehicleId", target = "vehicleId")
    @Mapping(source = "masterId", target = "masterId")
    ClaimDto toDto(ClaimEntity claim);

    // toEntity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "claimNumber", expression = "java(generateClaimNumber())")
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "vehicleId", source = "vehicleId")
    @Mapping(target = "masterId", ignore = true)
    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "mapPriority")
    @Mapping(target = "isApproved", constant = "false")
    @Mapping(target = "isPaid", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "workItems", ignore = true)
    @Mapping(target = "usedParts", ignore = true)
    @Mapping(target = "diagnosisDate", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "completionDate", ignore = true)
    ClaimEntity toEntity(CreateClaimRequest request);

    @Named("mapPriority")
    default ClaimPriority mapPriority(String priority) {
        if (priority == null || priority.isEmpty()) {
            return ClaimPriority.NORMAL;
        }
        try {
            return ClaimPriority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ClaimPriority.NORMAL;
        }
    }

    default String generateClaimNumber() {
        return "CLM-" + (System.currentTimeMillis() % 1000000);
    }
}