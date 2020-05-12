import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './routes/app-routing.module';
import {AppComponent} from './app.component';
import {RallyMetricsDashboardComponent} from './pages/rally-metrics-dashboard/rally-metrics-dashboard.component';
import {HttpClientModule} from '@angular/common/http';
import {NgHttpLoaderModule} from 'ng-http-loader';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {CardModule, DropdownModule, MultiSelectModule, SliderModule, TableModule, TabViewModule, ToolbarModule} from 'primeng';
import {TableContentComponent} from './components/table-content/table-content.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule} from '@angular/forms';
import {SprintCalculationComponent} from './components/table-content/calculation-table/tables/sprint-calculation/sprint-calculation.component';
import {ReleaseCalculationComponent} from './components/table-content/calculation-table/tables/release-calculation/release-calculation.component';
import {RemainUspCalculationComponent} from './components/table-content/calculation-table/tables/remain-usp-calculation/remain-usp-calculation.component';
import {SharedTableComponent} from './components/table-content/calculation-table/shared-table/shared-table.component';
import {ProjectTableComponent} from './components/table-content/project-table/project-table.component';
import {DashboardService} from './services/dashboard.service';

@NgModule({
  declarations: [
    AppComponent,
    RallyMetricsDashboardComponent,
    HeaderComponent,
    FooterComponent,
    TableContentComponent,
    SprintCalculationComponent,
    ReleaseCalculationComponent,
    RemainUspCalculationComponent,
    SharedTableComponent,
    ProjectTableComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgHttpLoaderModule.forRoot(),
    ToolbarModule,
    CardModule,
    TabViewModule,
    TableModule,
    DropdownModule,
    BrowserAnimationsModule,
    SliderModule,
    FormsModule,
    MultiSelectModule
  ],
  providers: [DashboardService],
  bootstrap: [AppComponent]
})
export class AppModule { }
