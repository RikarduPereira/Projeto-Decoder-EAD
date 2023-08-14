package com.ead.course.services.impl;

import com.ead.course.repositories.ModuleRespository;
import com.ead.course.services.ModulerService;
import org.springframework.stereotype.Service;

@Service
public class ModulerServiceImpl implements ModulerService {

    private final ModuleRespository moduleRespository;

    public ModulerServiceImpl(ModuleRespository moduleRespository) {
        this.moduleRespository = moduleRespository;
    }
}
