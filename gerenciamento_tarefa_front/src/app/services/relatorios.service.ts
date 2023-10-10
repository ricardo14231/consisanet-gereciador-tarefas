import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Tarefa } from '../models/tarefa.model';

@Injectable({
  providedIn: 'root'
})
export class RelatoriosService {

  private readonly API = `${environment.API_APP}/reports`

  constructor(
    private http: HttpClient
  ) { }

  downloadRelatorio(data: Tarefa[]) {
    console.log(this.API)
    return this.http.post(this.API, data, { responseType: 'blob' });
  }
}
