package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourserUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourserUserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourserUserService {

    private final CourseUserRepository courseUserRepository;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourserUserModel save(CourserUserModel courserUserModel) {
        return courseUserRepository.save(courserUserModel);
    }
}
