package com.ead.course.services.impl;

import com.ead.course.repositories.CourserRepository;
import com.ead.course.services.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourserRepository courserRepository;

    public CourseServiceImpl(CourserRepository courserRepository) {
        this.courserRepository = courserRepository;
    }
}
