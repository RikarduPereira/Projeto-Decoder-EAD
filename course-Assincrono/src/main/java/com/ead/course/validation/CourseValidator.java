package com.ead.course.validation;

import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.UserDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    private final Validator validator;


    public CourseValidator(@Qualifier("defaultValidator") Validator validator) {
        this.validator = validator;
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
        ResponseEntity<UserDto> responseUserInstructor;
//        try {
//            responseUserInstructor = authUserClient.getOneUserById(userInstructor);
//            if (UserType.STUDENT.equals(Objects.requireNonNull(responseUserInstructor.getBody()).getUserType())) {
//                errors.rejectValue("userInstructor", "userInstructorError", "User must be INSTRUCTOR or ADMIN.");
//            }
//        } catch (HttpStatusCodeException e) {
//            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
//                errors.rejectValue("userInstructor", "userInstructorError", "Instructor not found.");
//            }
//        }
    }
}
