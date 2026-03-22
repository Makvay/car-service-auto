package ru.car.api.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.client.ClientCarEntity;

import java.util.List;

@Repository
public interface ClientCarRepository extends JpaRepository<ClientCarEntity, Long> {

    List<ClientCarEntity> findByClientId(Long clientId);

}
