import {Component, Input, OnInit} from '@angular/core';
import {Dashboard} from '../../../../../models/dashboard.model';
import {ResponseModel} from '../../../../../models/response.model';
import {DashboardService} from '../../../../../services/dashboard.service';
import {KeyValue} from '@angular/common';

@Component({
  selector: 'app-remain-usp-calculation',
  templateUrl: './remain-usp-calculation.component.html',
  styleUrls: ['./remain-usp-calculation.component.scss']
})
export class RemainUspCalculationComponent implements OnInit {

  // @Input() projectName: string;
  // @Input() projectId: string;
  projectId: string;
  projectName: string;
  @Input() project: KeyValue<string, string>;
  remainTableName = 'Remain Calculation';
  cols: any[];
  remains: Dashboard[] = [];
  dummyRemains: Dashboard[] = [
    {
      id: 1,
      iteration: 'Sprint 04_20',
      storyPoint: 5,
      remainStoryPoints: 2
    },
    {
      id: 2,
      iteration: 'Sprint 03_20',
      storyPoint: 10,
      remainStoryPoints: 1
    },
    {
      id: 3,
      iteration: 'Sprint 02_20',
      storyPoint: 12,
      remainStoryPoints: 0
    },
    {
      id: 4,
      iteration: 'Sprint 01_20',
      storyPoint: 15,
      remainStoryPoints: 3
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
      {field: 'iteration', header: 'Iteration'},
      {field: 'storyPoint', header: 'SP on the start'},
      {field: 'remainStoryPoints', header: 'Remain SP'}
    ];

    if (this.projectId !== '') {
      this.dashboardService.getRemainData(this.projectId).subscribe(res => {
        // @ts-ignore
        this.remains = <ResponseModel> res.result;
      });
    } else {
      this.remains = this.dummyRemains;
    }
  }

}
