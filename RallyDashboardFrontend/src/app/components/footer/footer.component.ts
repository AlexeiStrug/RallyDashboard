import { Component, OnInit } from '@angular/core';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  nameBuild: string = environment.nameBuild;
  buildTime: string = environment.timeBuild;

  constructor() { }

  ngOnInit(): void {
  }

}
