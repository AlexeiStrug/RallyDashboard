package com.ge.dashboard.repository;

import com.ge.dashboard.model.ProjectEntity;
import com.ge.dashboard.model.ReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {

    ReleaseEntity getByReleaseName(String name);

    List<ReleaseEntity> getAllByProject(ProjectEntity projectEntity);

}
