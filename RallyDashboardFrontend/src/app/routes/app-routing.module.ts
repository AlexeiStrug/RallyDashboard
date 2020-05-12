import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RallyMetricsDashboardComponent} from '../pages/rally-metrics-dashboard/rally-metrics-dashboard.component';


const routes: Routes = [
  {path: 'dashboard', component: RallyMetricsDashboardComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
