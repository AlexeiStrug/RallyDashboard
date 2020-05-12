package com.ge.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "release", schema = "public", catalog = "postgres")
public class ReleaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "release_name")
    private String releaseName;
    @Column(name = "release_start_date")
    private Date releaseStartDate;
    @Column(name = "release_date")
    private Date releaseDate;
    @Column(name = "state")
    private String state;
    @Column(name = "day_of_diff")
    private long dayOfDiff;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

}
