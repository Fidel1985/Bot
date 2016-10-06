package com.fidel.bot.jpa;

import java.util.List;
import java.util.Optional;

import com.fidel.bot.jpa.entity.UserEntity;

public interface UserRepository extends BaseRepository<UserEntity, Long> {

    Optional<UserEntity> getByUsername(String username);

    List<UserEntity> getByEmailOrUsername(String email, String username);
}