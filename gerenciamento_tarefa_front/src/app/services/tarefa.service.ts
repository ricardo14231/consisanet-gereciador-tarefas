import { HttpClient } from '@angular/common/http';

import { Injectable, Output } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Tarefa } from '../models/tarefa.model';
import { Observable, Subject } from 'rxjs';
import { situacaoTarefaEnum } from '../enums/situacaoTarefaEnum';

@Injectable({
  providedIn: 'root'
})
export class TarefaService {

  constructor(
    private http: HttpClient
  ) { }

  private readonly API = `${environment.API_APP}/tarefa`;

  private _tarefa!: Tarefa;

  private _isEdit: boolean = false;
  @Output() responseOnSave = new Subject<any>();

  listAllTarefa(): Observable<Tarefa[]> {
    return this.http.get<Tarefa[]>(`${this.API}/listAll`);
  }

  private createTarefa(tarefa: Tarefa): Observable<Tarefa> {
    return this.http.post<Tarefa>(`${this.API}/create`, tarefa);
  }

  deleteTarefa(tarefaId: number): Observable<any> {
    return this.http.delete<any>(`${this.API}/delete/${tarefaId}`);
  }

  private updateTarefa(tarefa: Tarefa): Observable<any> {
    return this.http.put<Tarefa>(`${this.API}/update`, tarefa);
  }

  findTarefaPerDate(initialDate: string, finalDate: string): Observable<Tarefa[]> {
    return this.http.get<Tarefa[]>(`${this.API}/filter/date/${initialDate}/${finalDate}`)
  }

  findTarefaAtrasada(): Observable<Tarefa[]> {
    return this.http.get<Tarefa[]>(`${this.API}/filter/late`)
  }

  findTarefaPerUsuario(usuarioId: number): Observable<Tarefa[]> {
    return this.http.get<Tarefa[]>(`${this.API}/filter/${usuarioId}`)
  }

  findTarefaPerUsuarioAndSituacaoTarefa(usuarioId: number, situacaoTarefa: situacaoTarefaEnum): Observable<Tarefa[]> {
    return this.http.get<Tarefa[]>(`${this.API}/filter/${usuarioId}/${situacaoTarefa}`)
  }

  get tarefa(): Tarefa {
    return this._tarefa;
  }
  
  set tarefa(value: Tarefa) {
    this._isEdit = true;
    this._tarefa = value;
  }

  get isEdit(): boolean {
    return this._isEdit;
  }

  set isEdit(value: boolean) {
    this._isEdit = value;
  }

  onSave(tarefa: Tarefa): void {
    if(this._isEdit) {
      this.updateTarefa(tarefa).subscribe({
        next: responseTarefa => {
          this.responseOnSave.next(responseTarefa);
          this._isEdit = false;
        },
        error: err => this.responseOnSave.error(err)
      })
    } else {
      this.createTarefa(tarefa).subscribe({
        next: responseTarefa => {
          this.responseOnSave.next(responseTarefa);          
        },
        error: err => this.responseOnSave.error(err)
      })
    }
  }
}
