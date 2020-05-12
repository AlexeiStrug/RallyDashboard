import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class DashboardService {

  constructor(private http: HttpClient) {
  }

  getSprintData(projectId: string) {
    return this.http.get('http://localhost:8080/api/sprint-data?projectId=' + projectId);
  }

  getReleaseData(projectId: string) {
    return this.http.get('http://localhost:8080/api/release-data?projectId=' + projectId);
  }

  getRemainData(projectId: string) {
    return this.http.get('http://localhost:8080/api/remain-data?projectId=' + projectId);
  }


}
