package com.app.travelo.repositories;

import com.app.travelo.model.entity.CommonParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonParameterRepository extends JpaRepository<CommonParameterEntity, Long> {

    @Query("select cp from CommonParameterEntity cp where cp.parameterGroup= :parameterGroup and cp.available=true")
    List<CommonParameterEntity> getParameters(@Param("parameterGroup") String parameterGroup);
}
