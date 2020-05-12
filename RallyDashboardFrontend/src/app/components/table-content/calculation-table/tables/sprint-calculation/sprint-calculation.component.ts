import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {Dashboard} from '../../../../../models/dashboard.model';
import {DashboardService} from '../../../../../services/dashboard.service';
import {ResponseModel} from '../../../../../models/response.model';
import {KeyValue} from '@angular/common';

@Component({
  selector: 'app-sprint-calculation',
  templateUrl: './sprint-calculation.component.html',
  styleUrls: ['./sprint-calculation.component.scss']
})
export class SprintCalculationComponent implements OnInit {
  // @Input() projectId: string;
  // @Input() projectName: string;
  @Input() project: KeyValue<string, string>;

  projectId: string;
  projectName: string;
  sprintTableName = 'Sprint Calculation';
  storyPointFilter: number;
  storyPointTimeout: any;
  selectedColumns: any[];
  cols: any[];
  dashboard: Dashboard[] = [];
  dummyDashboards: Dashboard[] = [
    {
      id: 1,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 2,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 2,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 3,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 4,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 5,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 6,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 7,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 8,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 9,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    },
    {
      id: 10,
      iteration: 'Sprind 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    }, {
      id: 11,
      iteration: 'Sprint 04_20',
      type: 'min',
      storyPoint: 5.0,
      storyPointOfCalculation: 5.33333333333333,
      remainStoryPoints: null,
      startDate: '2020-02-17T23:00:00.000+0000',
      endDate: '2020-03-02T23:00:00.000+0000'
    }
  ];
  types = [
    {label: 'All Types', value: null},
    {label: 'Below', value: 'below'},
    {label: 'Vmin', value: 'min'},
    {label: 'Vavg', value: 'avg'},
    {label: 'Vmax', value: 'max'}
  ];

  constructor(private elementRef: ElementRef,
              private dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    // @ts-ignore
    this.projectName = this.project.key;
    // @ts-ignore
    this.projectId = this.project.value;
    this.cols = [
      {field: 'id', header: 'Id'},
      {field: 'iteration', header: 'Iteration'},
      {field: 'type', header: 'Vcalculation'},
      {field: 'storyPoint', header: 'Story Point'}
    ];

    this.selectedColumns = this.cols;
    if (this.projectId !== '') {
      this.dashboardService.getSprintData(this.projectId).subscribe(res => {
        // @ts-ignore
        this.dashboard = <ResponseModel> res.result;
      });
    } else {
      this.dashboard = this.dummyDashboards;
    }
  }


  onStoryPointChange(event, dt) {
    if (this.storyPointTimeout) {
      clearTimeout(this.storyPointTimeout);
    }

    this.storyPointTimeout = setTimeout(() => {
      dt.filter(event.value, 'storyPoint', 'gt');
    }, 250);
  }
}
