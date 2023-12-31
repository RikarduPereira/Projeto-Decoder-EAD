package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModulerService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    private final ModulerService modulerService;

    private final CourseService courseService;


    public ModuleController(ModulerService modulerService, CourseService courseService) {
        this.modulerService = modulerService;
        this.courseService = courseService;

    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
                                             @RequestBody @Valid ModuleDto moduleDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!!");
        }
        var moduleModel = new ModuleModel();
        //junção entre o DTO e um Model
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModelOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(modulerService.save(moduleModel));

    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {

        Optional<ModuleModel> moduleModelOptional = modulerService.findModuleIntoCourse(courseId, moduleId);
        if (!moduleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this Course!!");
        }
        modulerService.delete(moduleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully");

    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto) {
        Optional<ModuleModel> moduleModelOptional = modulerService.findModuleIntoCourse(courseId, moduleId);
        if (!moduleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this Course!!");
        }
        var moduleModel = moduleModelOptional.get();
        moduleModel.setTitle(moduleDto.getTitle());
        moduleModel.setDescription(moduleDto.getDescription());

        return ResponseEntity.status(HttpStatus.OK).body(modulerService.save(moduleModel));
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/courses/{courseId}/modules/")
    ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId,
                                                    SpecificationTemplate.ModuleSpec spec,
                                                    @PageableDefault(page = 0, size = 10, sort = "moduleId",
                                                            direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(modulerService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId,
                                        @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> moduleModelOptional = modulerService.findModuleIntoCourse(courseId, moduleId);
        if (!moduleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this Course!!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
    }
}
