package ru.car.api.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.client.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByEmail(String email);
    Optional<ClientEntity> findByPhone(String phone);

}
