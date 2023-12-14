package com.ead.course.services;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourserUserModel;

import java.util.UUID;

public interface CourserUserService {
    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    CourserUserModel save(CourserUserModel courserUserModel);

    CourserUserModel saveAndSendSubscriptionUserInCourse(CourserUserModel courserUserModel);
}
