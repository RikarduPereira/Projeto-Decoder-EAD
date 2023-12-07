package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuleRespository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

    @Query(value = "select * from tb_modules where courser_courser_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseID") UUID courseId);

    @Query(value = "select * from tb_modules where course_course_id =?1 and module_id =?2", nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);
}
