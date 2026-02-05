package ru.car.api.claim.mapper;

import org.mapstruct.*;
import ru.car.dto.claim.ClaimDto;
import ru.car.dto.claim.CreateClaimRequest;
import ru.car.entity.claim.ClaimEntity;
import ru.car.entity.claim.ClaimPriority;

@Mapper(componentModel = "spring")
public interface ClaimMapper {

    // toDto
//    @Mapping(source = "clientId", target = "clientId")
//    @Mapping(source = "vehicleId", target = "vehicleId")
//    @Mapping(source = "masterId", target = "masterId")

    ClaimDto toDto(ClaimEntity claim);
    ClaimEntity toEntity(CreateClaimRequest request);

//    @Named("mapPriority")
//    default ClaimPriority mapPriority(String priority) {
//        if (priority == null || priority.isEmpty()) {
//            return ClaimPriority.NORMAL;
//        }
//        try {
//            return ClaimPriority.valueOf(priority.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            return ClaimPriority.NORMAL;
//        }
//    }
//
//    default String generateClaimNumber() {
//        return "CLM-" + (System.currentTimeMillis() % 1000000);
//    }
}