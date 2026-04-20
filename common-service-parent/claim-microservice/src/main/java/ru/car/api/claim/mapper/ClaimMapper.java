package ru.car.api.claim.mapper;

import org.mapstruct.*;
import ru.car.dto.claim.ClaimDto;
import ru.car.dto.claim.CreateClaimRequest;
import ru.car.entity.claim.ClaimEntity;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClaimMapper {

    // to dto
    ClaimDto toDto(ClaimEntity claim);

    // to entity - explicit mapping for claim DTO to entity fields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "claimNumber", ignore = true)
    @Mapping(target = "masterId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isApproved", ignore = true)
    @Mapping(target = "isPaid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "diagnosisDate", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "completionDate", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "workItems", ignore = true)
    @Mapping(target = "usedParts", ignore = true)
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "vehicleId", target = "vehicleId")
    @Mapping(source = "problemDescription", target = "problemDescription")
    @Mapping(source = "customerNotes", target = "customerNotes")
    @Mapping(source = "internalNotes", target = "internalNotes")
    @Mapping(source = "mileageAtEntry", target = "mileageAtEntry")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "scheduledDate", target = "scheduledDate")
    ClaimEntity toEntity(CreateClaimRequest request);


}