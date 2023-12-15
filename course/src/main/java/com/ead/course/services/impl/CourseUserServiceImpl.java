package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourserUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourserUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourserUserService {

    private final CourseUserRepository courseUserRepository;
    private final AuthUserClient authUserClient;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository, AuthUserClient authUserClient) {
        this.courseUserRepository = courseUserRepository;
        this.authUserClient = authUserClient;
    }

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourserUserModel save(CourserUserModel courserUserModel) {
        return courseUserRepository.save(courserUserModel);
    }

    @Transactional
    @Override
    public CourserUserModel saveAndSendSubscriptionUserInCourse(CourserUserModel courserUserModel) {
        courserUserModel = courseUserRepository.save(courserUserModel);
        authUserClient.postSubscriptionUserInCourse(courserUserModel.getCourse().getCourseId(), courserUserModel.getUserId());
        return courserUserModel;
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return courseUserRepository.existsByUserId(userId);
    }

    @Transactional
    @Override
    public void deleteCourseUserByUser(UUID userId) {
        courseUserRepository.deleteAllByUserId(userId);
    }
}
