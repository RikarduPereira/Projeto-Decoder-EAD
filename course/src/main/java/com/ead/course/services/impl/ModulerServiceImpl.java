package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRespository;
import com.ead.course.services.ModulerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
