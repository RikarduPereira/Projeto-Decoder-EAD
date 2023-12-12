package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourserUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseUserRepository extends JpaRepository<CourserUserModel, UUID> {
    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);
}
