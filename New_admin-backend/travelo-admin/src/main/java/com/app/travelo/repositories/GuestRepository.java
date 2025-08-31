package com.app.travelo.repositories;

import com.app.travelo.model.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Long> {
    GuestEntity findByPhone(String phoneNo);
}
