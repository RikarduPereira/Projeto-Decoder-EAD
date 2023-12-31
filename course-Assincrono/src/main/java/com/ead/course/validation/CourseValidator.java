package com.ead.course.validation;

import com.ead.course.configs.security.AuthenticationCurrentUserService;
import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    private final Validator validator;
    private final UserService userService;

    private final AuthenticationCurrentUserService authenticationCurrentUserService;


    public CourseValidator(@Qualifier("defaultValidator") Validator validator, UserService userService, AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.validator = validator;
        this.userService = userService;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object object, Errors errors) {
        CourseDto courseDto = (CourseDto) object;
        validator.validate(courseDto, errors);
        if (!errors.hasErrors()) {
            validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if (currentUserId.equals(userInstructor)) {
            Optional<UserModel> userModelOptional = userService.findById(userInstructor);
            if (!userModelOptional.isPresent()) {
                errors.rejectValue("userInstructor", "userInstructorError", "Instructor not found.");
            }
            if (UserType.STUDENT.toString().equals(userModelOptional.get().getUserType())) {
                errors.rejectValue("userInstructor", "userInstructorError", "User must be INSTRUCTOR or ADMIN.");
            }
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }
}
