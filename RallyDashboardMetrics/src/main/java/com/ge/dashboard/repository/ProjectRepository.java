package com.ge.dashboard.repository;

import com.ge.dashboard.model.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> getByProjectName(String projectName);

    Optional<ProjectEntity> getByProjectId(String projectId);
}
