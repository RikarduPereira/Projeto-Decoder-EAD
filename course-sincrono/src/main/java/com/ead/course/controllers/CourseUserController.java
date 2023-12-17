package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourserUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourserUserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {
    private final AuthUserClient authUserClient;
    private final CourseService courseService;
    private final CourserUserService courserUserService;

    public CourseUserController(AuthUserClient authUserClient, CourseService courseService, CourserUserService courserUserService) {
        this.authUserClient = authUserClient;
        this.courseService = courseService;
        this.courserUserService = courserUserService;
    }

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId",
            direction = Sort.Direction.ASC) Pageable pageable, @PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllUsersByCourse(courseId, pageable));


    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                               @RequestBody @Valid SubscriptionDto subscriptionDto) {

        ResponseEntity<UserDto> responseUser;
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        if (courserUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDto.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        try {
            responseUser = authUserClient.getOneUserById(subscriptionDto.getUserId());
            if (UserStatus.BLOCKED.equals(Objects.requireNonNull(responseUser.getBody()).getUserStatus())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked!");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
            }
        }
        CourserUserModel courserUserModel = courserUserService.saveAndSendSubscriptionUserInCourse(courseModelOptional.get().convertToCourseUserModel(subscriptionDto.getUserId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(courserUserModel);

    }

    @DeleteMapping("/courses/users/{userId}")
    public ResponseEntity<Object> deleteCourseUserByUser(@PathVariable(value = "userId") UUID userId) {
        if (!courserUserService.existsByUserId(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser Not Found");
        }
        courserUserService.deleteCourseUserByUser(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("CourseUser deleted successfully");
    }
}
