package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRespository;
import com.ead.course.services.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourserRepository courserRepository;
    private final ModuleRespository moduleRespository;

    private final LessonRepository lessonRepository;

    public CourseServiceImpl(CourserRepository courserRepository, ModuleRespository moduleRespository, LessonRepository lessonRepository) {
        this.courserRepository = courserRepository;
        this.moduleRespository = moduleRespository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModelList = moduleRespository.findAllModulesIntoCourse(courseModel.getCourserId());
        if (!moduleModelList.isEmpty()) {
            for (ModuleModel model : moduleModelList) {
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(model.getModuleId());
                if (!lessonModelList.isEmpty()) {
                    lessonRepository.deleteAll(lessonModelList);
                }
            }
            moduleRespository.deleteAll(moduleModelList);
        }
        courserRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courserRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courserRepository.findById(courseId);
    }

    @Override
    public List<CourseModel> findAll() {
        return courserRepository.findAll();
    }
}
