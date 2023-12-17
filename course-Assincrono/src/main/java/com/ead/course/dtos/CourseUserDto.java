package com.ead.course.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseUserDto {

    private UUID userId;
    @NotNull
    private UUID courseId;

}
