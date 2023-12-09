package com.ead.authuser.controllers;

import com.ead.authuser.models.UserModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

    @GetMapping("/users/{userId/courses}")
    public ResponseEntity<Page<UserModel>> getAllCoursesByUser() {

    }
}
