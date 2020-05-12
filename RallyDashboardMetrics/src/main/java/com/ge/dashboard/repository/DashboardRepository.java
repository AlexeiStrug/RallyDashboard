package com.ge.dashboard.repository;

import com.ge.dashboard.model.DashboardEntity;
import com.ge.dashboard.model.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DashboardRepository extends JpaRepository<DashboardEntity, Long> {

    DashboardEntity getByIteration(String iterationName);

    @Query("FROM DashboardEntity AS d LEFT JOIN ProjectEntity AS p ON d.project = p " +
            "WHERE p.projectId =:projectId AND d.endDate =:endDate")
    DashboardEntity fetchDashboardByProjectIdAndEndDate(@Param("projectId") String project, @Param("endDate") Date endDate);

    List<DashboardEntity> getAllByProject(ProjectEntity projectEntity);

}
