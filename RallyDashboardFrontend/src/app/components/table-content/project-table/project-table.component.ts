import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-project-table',
  templateUrl: './project-table.component.html',
  styleUrls: ['./project-table.component.scss']
})
export class ProjectTableComponent implements OnInit {

  projects: string[] = ['SalesIQ', 'Service Experience Portal', 'Global Reveal', 'SCORE 2020'];
  mapOfProject: Map<string, string>;

  constructor() {
  }

  ngOnInit(): void {
    this.mapOfProject = new Map([
      ['SalesIQ', '/project/79325321000'],
      ['Service Experience Portal', '/project/277044010640'],
      ['Global Reveal', '/project/129693482836'],
      ['SCORE 2020', '/project/353097419232'],
      ['eOM', '/project/331643800960']
    ]);
  }

}
