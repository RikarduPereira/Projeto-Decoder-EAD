package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRespository;
import com.ead.course.services.ModulerService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModulerServiceImpl implements ModulerService {

    private final ModuleRespository moduleRespository;
    private final LessonRepository lessonRepository;

    public ModulerServiceImpl(ModuleRespository moduleRespository, LessonRepository lessonRepository) {
        this.moduleRespository = moduleRespository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {
        List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
        if (!lessonModelList.isEmpty()) {
            lessonRepository.deleteAll(lessonModelList);
        }
        moduleRespository.delete(moduleModel);
    }

    @Override
    public ModuleModel save(ModuleModel moduleModel) {
        return moduleRespository.save(moduleModel);
    }

    @Override
    public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
        return moduleRespository.findModuleIntoCourse(courseId, moduleId);
    }

    @Override
    public List<ModuleModel> findAllByCourse(UUID courseId) {
        return moduleRespository.findAllLModulesIntoCourse(courseId);
    }

    @Override
    public Optional<ModuleModel> findById(UUID moduleId) {
        return moduleRespository.findById(moduleId);
    }

    @Override
    public Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable) {
        return moduleRespository.findAll(spec, pageable);
    }
}
