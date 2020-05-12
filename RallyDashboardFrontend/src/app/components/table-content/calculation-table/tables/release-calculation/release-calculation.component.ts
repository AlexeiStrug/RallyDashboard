import {Component, Input, OnInit} from '@angular/core';
import {Release} from '../../../../../models/release.model';
import {DashboardService} from '../../../../../services/dashboard.service';
import {ResponseModel} from '../../../../../models/response.model';
import {KeyValue} from '@angular/common';

@Component({
  selector: 'app-release-calculation',
  templateUrl: './release-calculation.component.html',
  styleUrls: ['./release-calculation.component.scss']
})
export class ReleaseCalculationComponent implements OnInit {

  // @Input() projectName: string;
  // @Input() projectId: string;
  projectId: string;
  projectName: string;
  @Input() project: KeyValue<string, string>;
  releaseTableName = 'Release Calculation';
  cols: any[];
  releases: Release[] = [];
  dummyReleases: Release[] = [
    {
      id: 1,
      releaseName: 'eOM/OM Backlog',
      releaseStartDate: '2019-08-26',
      releaseDate: '2019-08-21',
      state: 'Planning',
      dayOfDiff: 182
    },
    {
      id: 2,
      releaseName: 'eOM/OM Backlog',
      releaseStartDate: '2019-08-26',
      releaseDate: '2019-08-28',
      state: 'Accepted',
      dayOfDiff: 174
    },
    {
      id: 3,
      releaseName: 'eOM/OM Backlog',
      releaseStartDate: '2019-08-26',
      releaseDate: '2019-08-27',
      state: 'Planning',
      dayOfDiff: 151
    },
  ];

  constructor(private dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    // @ts-ignore
    this.projectName = this.project.key;
    // @ts-ignore
    this.projectId = this.project.value;

    this.cols = [
      {field: 'id', header: 'Id'},
      {field: 'releaseName', header: 'Release Name'},
      {field: 'releaseStartDate', header: 'Release Start Date'},
      {field: 'releaseDate', header: 'Release Date'},
      {field: 'state', header: 'Current State'},
      {field: 'dayOfDiff', header: 'Days Of Diff'}
    ];

    if (this.projectId !== '') {
      this.dashboardService.getReleaseData(this.projectId).subscribe(res => {
        // @ts-ignore
        this.releases = <ResponseModel> res.result;
      });
    } else {
      this.releases = this.dummyReleases;
    }
  }

}
