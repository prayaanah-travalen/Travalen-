package com.app.travelo.repositories;


import com.app.travelo.model.entity.PopularDestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularDestRepository extends JpaRepository<PopularDestEntity, Long>{
}
