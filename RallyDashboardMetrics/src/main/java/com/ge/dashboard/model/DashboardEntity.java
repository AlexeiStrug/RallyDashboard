package com.ge.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dashboard", schema = "public", catalog = "postgres")
public class DashboardEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "iteration")
    private String iteration;
    @Column(name = "type")
    private String type;
    @Column(name = "story_point")
    private Double storyPoint;
    @Column(name = "story_point_of_calculation")
    private Double storyPointOfCalculation;
    @Column(name = "remain_story_point")
    private Double remainStoryPoints;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;
}
