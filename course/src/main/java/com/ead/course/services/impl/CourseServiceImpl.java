package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourserUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.CourserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRespository;
import com.ead.course.services.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourserRepository courserRepository;
    private final ModuleRespository moduleRespository;
    private final CourseUserRepository courseUserRepository;
    private final LessonRepository lessonRepository;
    private final AuthUserClient authUserClient;

    public CourseServiceImpl(CourserRepository courserRepository, ModuleRespository moduleRespository, CourseUserRepository courseUserRepository, LessonRepository lessonRepository, AuthUserClient authUserClient) {
        this.courserRepository = courserRepository;
        this.moduleRespository = moduleRespository;
        this.courseUserRepository = courseUserRepository;
        this.lessonRepository = lessonRepository;
        this.authUserClient = authUserClient;
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {

        List<ModuleModel> moduleModelList = moduleRespository.findAllLModulesIntoCourse(courseModel.getCourseId());
        if (!moduleModelList.isEmpty()) {
            for (ModuleModel model : moduleModelList) {
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(model.getModuleId());
                if (!lessonModelList.isEmpty()) {
                    lessonRepository.deleteAll(lessonModelList);
                }
            }
            moduleRespository.deleteAll(moduleModelList);
        }
        List<CourserUserModel> courserUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());
        if (!courserUserModelList.isEmpty()) {
            courseUserRepository.deleteAll(courserUserModelList);
            deleteCouserInAuthUser(courseModel);
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
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {

        return courserRepository.findAll(spec, pageable);
    }

    private void deleteCouserInAuthUser(CourseModel courseModel) {
        authUserClient.deleteCouserInAuthUser(courseModel.getCourseId());
    }
}
