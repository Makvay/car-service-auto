package ru.car.api.claim.mapper;

import org.mapstruct.*;
import ru.car.dto.claim.ClaimDto;
import ru.car.dto.claim.CreateClaimRequest;
import ru.car.entity.claim.ClaimEntity;


@Mapper(componentModel = "spring")
public interface ClaimMapper {

    // to dto
    ClaimDto toDto(ClaimEntity claim);
    // to entity
    ClaimEntity toEntity(CreateClaimRequest request);


}