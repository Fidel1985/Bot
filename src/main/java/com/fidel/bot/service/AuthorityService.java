package com.fidel.bot.service;

import java.lang.reflect.Type;
import java.util.List;


import com.fidel.bot.api.Authority;
import com.fidel.bot.jpa.AuthorityRepository;
import com.fidel.bot.jpa.entity.AuthorityEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
    private static final Type LIST_OF_AUTHORITIES = new TypeToken<List<Authority>>() {}.getType();
    private AuthorityRepository authorityRepository;
    private ModelMapper modelMapper;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository, ModelMapper modelMapper) {
        this.authorityRepository = authorityRepository;
        this.modelMapper = modelMapper;
    }

    public List<Authority> getAll() {
        List<AuthorityEntity> authorities = authorityRepository.findAll();
        return modelMapper.map(authorities, LIST_OF_AUTHORITIES);
    }
}