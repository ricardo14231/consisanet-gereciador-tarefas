import { HttpClient } from '@angular/common/http';
import { Injectable, Output } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Usuario } from '../models/usuario.model';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioTarefaService {

  private readonly API = `${environment.API_APP}/usuario`;

  private _usuario!: Usuario;
  private _isEdit: boolean = false;
  @Output() responseOnSave = new Subject<any>();

  constructor(
    private http: HttpClient
  ) { }

  listAllUsuario(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.API}/listAll`);
  }

  private createUsuario(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.API}/create`, usuario);
  }

  deleteUsuario(usuarioId: number): Observable<any> {
    return this.http.delete<any>(`${this.API}/delete/${usuarioId}`);
  }

  private updateUsuario(usuario: Usuario): Observable<any> {
    return this.http.put<Usuario>(`${this.API}/update`, usuario);
  }

  get usuario(): Usuario {
    return this._usuario;
  }
  
  set usuario(value: Usuario) {
    this._isEdit = true;
    this._usuario = value;
  }

  get isEdit(): boolean {
    return this._isEdit;
  }

  set isEdit(value: boolean) {
    this._isEdit = value;
  }

  onSave(usuario: Usuario): void {
    if(this._isEdit) {
      this.updateUsuario(usuario).subscribe({
        next: response => {
          this.responseOnSave.next(response);
          this._isEdit = false;
        },
        error: err => this.responseOnSave.error(err)
      })
    } else {
      this.createUsuario(usuario).subscribe({
        next: response => {
          this.responseOnSave.next(response);          
        },
        error: err => this.responseOnSave.error(err)
      })
    }
  }
}
