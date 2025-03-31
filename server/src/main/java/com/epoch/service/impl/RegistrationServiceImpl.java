package com.epoch.service.impl;

import com.epoch.entity.Registration;
import com.epoch.mapper.RegistrationMapper;
import com.epoch.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private RegistrationMapper registrationMapper;

    @Override
    public Integer countRegistrations() {
        return registrationMapper.countRegistrations();
    }
} 