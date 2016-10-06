package com.fidel.bot.service;


import com.fidel.bot.api.BPMUser;
import com.fidel.bot.exception.ResourceNotFoundException;
import com.fidel.bot.jpa.UserRepository;
import com.fidel.bot.jpa.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;


@Service
public class UserService {

    private static final Type LIST_OF_JOB_TYPE = new TypeToken<List<BPMUser>>() {
    }.getType();

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
    }

    @Transactional
    public BPMUser save(BPMUser user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        checkIfUserExists(userEntity);
        String password = userEntity.getPassword();
        userEntity.setPassword(encoder.encode(password));
        userEntity = userRepository.save(userEntity);
        BPMUser created = modelMapper.map(userEntity, BPMUser.class);
        created.setPassword("");
        return created;
    }

    @Transactional(readOnly = true)
    public List<BPMUser> getAll() {
        Iterable<UserEntity> userEntities = userRepository.findAll();
        return modelMapper.map(userEntities, LIST_OF_JOB_TYPE);
    }

    @Transactional(readOnly = true)
    public BPMUser getByUsername(String username) {
        return modelMapper.map(userRepository
                        .getByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User '" + username + "' does not exist.")),
                BPMUser.class);
    }

    private void checkIfUserExists(UserEntity userEntity) {
        List<UserEntity> existedUsers = userRepository.getByEmailOrUsername(userEntity.getEmail(), userEntity.getUsername());
        if (!existedUsers.isEmpty()) {
            throw new IllegalArgumentException("Username or email already exist");
        }
    }
}
