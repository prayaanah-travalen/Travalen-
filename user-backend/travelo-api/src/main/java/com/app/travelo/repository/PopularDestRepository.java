package com.app.travelo.repository;


import com.app.travelo.model.entity.PopularDestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularDestRepository extends JpaRepository<PopularDestEntity, String>{
}
