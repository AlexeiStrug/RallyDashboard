import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-shared-table',
  templateUrl: './shared-table.component.html',
  styleUrls: ['./shared-table.component.scss']
})
export class SharedTableComponent implements OnInit {

  @Input() tableData: any[];
  @Input() headerData: any[];
  @Input() tableName: string;
  selectedColumns: any[];

  constructor() { }

  ngOnInit(): void {
    this.selectedColumns = this.headerData;
  }

}
