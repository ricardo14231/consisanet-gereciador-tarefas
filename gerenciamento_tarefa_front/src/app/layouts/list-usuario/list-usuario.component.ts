import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Usuario } from 'src/app/models/usuario.model';
import { UsuarioTarefaService } from 'src/app/services/usuario-tarefa.service';
import { convertFormatDatePtBr } from 'src/app/utils/formatDate';

@Component({
  selector: 'app-list-usuario',
  templateUrl: './list-usuario.component.html',
  styleUrls: ['./list-usuario.component.css']
})
export class ListUsuarioComponent {
    
  checkDataInfo: boolean = true;
  reload: boolean = false;

  displayedColumns: string[] = 
  [
    'id', 
    'nome', 
    'updateAt', 
    'createAt',
    'acoes'
  ];
  
  dataSource!: MatTableDataSource<Usuario>;

  constructor(
    public dialog: MatDialog,
    private router: Router,
    private toastrService: ToastrService,
    private usuarioTarefaService: UsuarioTarefaService
  ) { }

  
  ngOnInit(): void {      
    this.listAllUsuario();
  }
 
  private listAllUsuario() {
    this.usuarioTarefaService.listAllUsuario().subscribe({
      next: response => {
        this.dataSource = new MatTableDataSource<Usuario>(response);
        this.dataSource.data.length === 0 ? this.checkDataInfo = true : this.checkDataInfo = false;
      },
      error: () => this.toastrService.error("Erro ao listar os usuarios!", 'dangerMessage')
    });
  }

  editUsuario(element: Usuario): void {
    this.usuarioTarefaService.usuario = element;
    this.router.navigate(['usuario']);
  }

  deleteUsuario(usuarioId: number): void {

    this.usuarioTarefaService.deleteUsuario(usuarioId).subscribe({
      next: () => {
        this.toastrService.success('Sucesso na operação!', 'Delete');
        this.listAllUsuario();
      },
      error: err => this.toastrService.error('Erro ao deletar o usuário', 'Delete')
    })
  }

  formatDatePtBr(date: string) {
    return convertFormatDatePtBr(date);
  }
}
