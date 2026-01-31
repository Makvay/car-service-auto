package ru.car.api.claim.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.car.dto.claim.ClaimDto;
import ru.car.dto.claim.CreateClaimRequest;
import ru.car.entity.claim.ClaimEntity;
import ru.car.entity.client.ClientCarEntity;
import ru.car.entity.client.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClaimMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "claimNumber", target = "claimNumber")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "problemDescription", target = "problemDescription")
    @Mapping(source = "customerNotes", target = "customerNotes")
    @Mapping(source = "internalNotes", target = "internalNotes")
    @Mapping(source = "mileageAtEntry", target = "mileageAtEntry")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "isApproved", target = "isApproved")
    @Mapping(source = "isPaid", target = "isPaid")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "scheduledDate", target = "scheduledDate")
    @Mapping(source = "diagnosisDate", target = "diagnosisDate")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "completionDate", target = "completionDate")
    @Mapping(source = "updatedAt", target = "updatedAt")
    ClaimDto toDto(ClaimEntity claim);

    // Маппинг Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "claimNumber", expression = "java(generateClaimNumber())")
    @Mapping(target = "client", source = "client")
    @Mapping(target = "vehicle", source = "vehicle")
    @Mapping(target = "master", ignore = true)
    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "priority", expression = "java(mapPriority(request.getPriority()))")
    @Mapping(target = "isApproved", constant = "false")
    @Mapping(target = "isPaid", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "statusHistory", ignore = true)
    @Mapping(target = "workItems", ignore = true)
    @Mapping(target = "usedParts", ignore = true)
    ClaimEntity toEntity(CreateClaimRequest request,
                         @Context ClientEntity client,
                         @Context ClientCarEntity vehicle);
    }






