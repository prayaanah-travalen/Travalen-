package com.app.travelo.repositories;

import com.app.travelo.model.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    LocationEntity findByLocation(String location);
}
