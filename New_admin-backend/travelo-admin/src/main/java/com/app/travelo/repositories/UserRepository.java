package com.app.travelo.repositories;

import com.app.travelo.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT user FROM UserEntity user where user.emailId=:emailId")
    UserEntity getUser(String emailId);

    @Query("SELECT user FROM UserEntity user where user.emailId=:emailId and user.password=:password")
    UserEntity findUserByCred(@Param("emailId") String emailId, @Param("password") String password);

    List<UserEntity> findUserByUserType(String userType);

}
