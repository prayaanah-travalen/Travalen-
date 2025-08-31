package com.app.travelo.repositories;

import com.app.travelo.model.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    @Query("SELECT userRole FROM UserRoleEntity userRole where userRole.userId=:userId AND userRole.roleId in (:roleId)")
    List<UserRoleEntity> findUserRoleByRoleIdAndUserId(@Param("roleId") List<Long> roleId, @Param("userId") Long userId);
}
