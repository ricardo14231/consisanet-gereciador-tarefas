import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { situacaoTarefaEnum } from 'src/app/enums/situacaoTarefaEnum';
import { Tarefa } from 'src/app/models/tarefa.model';
import { Usuario } from 'src/app/models/usuario.model';
import { RelatoriosService } from 'src/app/services/relatorios.service';
import { TarefaService } from 'src/app/services/tarefa.service';
import { UsuarioTarefaService } from 'src/app/services/usuario-tarefa.service';
import { convertFormatDatePtBr, formatDate } from 'src/app/utils/formatDate';

@Component({
  selector: 'app-status-tarefa',
  templateUrl: './status-tarefa.component.html',
  styleUrls: ['./status-tarefa.component.css']
})
export class StatusTarefaComponent {

  FILTRO_POR_DATA = "FILTRO_POR_DATA";
  FILTRO_POR_TAREFA_ATRASADA = "FILTRO_POR_TAREFA_ATRASADA";
  FILTRO_POR_USUARIO = "FILTRO_POR_USUARIO";

  dataInicio!: string;
  dataFim!: string;
  situacaoTarefa!: situacaoTarefaEnum;
  usuarioTarefa!: Usuario;
  listUsuarioTarefa: Usuario[] = [];
  listTarefa: Tarefa[] = [];

  
  checkDataInfo: boolean = true;
  reload: boolean = false;

  selectedFilterType = this.FILTRO_POR_DATA;
  typeFilter = {
    value: [this.FILTRO_POR_DATA, this.FILTRO_POR_TAREFA_ATRASADA, this.FILTRO_POR_USUARIO],
    display: ['Filtrar por data', 'Tarefas atrasadas', 'Usuário']
  }

  displayedColumns: string[] = 
  [
    'id', 
    'nome', 
    'subTarefa', 
    'dataInicio', 
    'dataFim', 
    'situacaoTarefa', 
    'responsavel',
    'updateAt', 
    'createAt',
    'acoes'
  ];
  
  dataSource!: MatTableDataSource<Tarefa>;

  constructor(
    public dialog: MatDialog,
    private router: Router,
    private toastrService: ToastrService,
    private tarefaService: TarefaService,
    private usuarioTarefaService: UsuarioTarefaService,
    private relatorioService: RelatoriosService
  ) { }

  
  ngOnInit(): void {
    this.dataInicio = ''
    this.dataFim = ''
      
    this.findAllUsuarioTarefa();
    this.listAllTarefa();
  }
 

  searchTarefa(): void {
    this.reload = true;

    switch(this.selectedFilterType) {
      case this.FILTRO_POR_DATA: {
        if(!this.dataInicio || !this.dataFim) {
          this.toastrService.error(`Campo(s) data inválido!`, 'Filtro');
          break;
        }
        const dataInicioIn = formatDate(this.dataInicio);
        const dataFimIn = formatDate(this.dataFim);
        
        this.findTarefaPerDate(dataInicioIn, dataFimIn);
        break;
      }
      case this.FILTRO_POR_TAREFA_ATRASADA: {

        this.findTarefaAtrasada();

        break;
      }
      case this.FILTRO_POR_USUARIO: {

        if(this.situacaoTarefa !== undefined && this.usuarioTarefa?.id !== undefined) {
          this.findTarefaPerUsuarioAndSituacaoTarefa(
            this.usuarioTarefa.id, 
            this.situacaoTarefa);
            break;
        } 

        if(this.situacaoTarefa === undefined && this.usuarioTarefa?.id !== undefined) {
          this.findTarefaPerUsuario(this.usuarioTarefa.id);
            break;
        } 
        
        this.toastrService.error(`Campo(s) do filtro inválido!`, 'Filtro');

        break;
      }
      
    }
  }

  private findAllUsuarioTarefa() {
    this.usuarioTarefaService.listAllUsuario().subscribe({
      next: responseUsuario => {
        this.listUsuarioTarefa = responseUsuario;
      },
      error: (err) => this.toastrService.error("Erro ao listar os usuarios!", err.error.details ? err.error.details : "Listar")
    });
  }

  private listAllTarefa() {

    this.tarefaService.listAllTarefa().subscribe({
      next: responseTarefa => {
        this.dataSource = new MatTableDataSource<Tarefa>(responseTarefa);
        this.dataSource.data.length === 0 ? this.checkDataInfo = true : this.checkDataInfo = false;
      },
      error: (err) => this.toastrService.error("Erro ao listar as tarefas!", err.error.details ? err.error.details : "Listar")
    });
  }

  editTarefa(element: Tarefa): void {
    this.tarefaService.tarefa = element;
    this.router.navigate(['tarefa']);
  }

  deleteTarefa(tarefaId: number): void {

    this.tarefaService.deleteTarefa(tarefaId).subscribe({
      next: () => {
        this.toastrService.success('Sucesso na operação!', 'Delete');
        this.listAllTarefa();
      },
      error: err => this.toastrService.error('Erro ao deletar a tarefa', err.error.details ? err.error.details : "Delete")
    })
  }

  private findTarefaPerDate(dataInicioIn: string, dataFimIn: string) {
    this.tarefaService.findTarefaPerDate(dataInicioIn, dataFimIn).subscribe({
      next: response => {
        this.setDataInDataSource(response);
      },
      error: err => this.toastrService.error('Erro ao buscar tarefa por data', err.error.details ? err.error.details : "Consulta")
    })
  }

  private findTarefaAtrasada()  {
    this.tarefaService.findTarefaAtrasada().subscribe({
      next: response => {
        this.setDataInDataSource(response);
      },
      error: err => this.toastrService.error('Erro ao buscar tarefa por data', err.error.details ? err.error.details : "Consulta")});
  }

  private findTarefaPerUsuario(usuarioId: number)  {
    this.tarefaService.findTarefaPerUsuario(usuarioId).subscribe({
      next: response => {
        this.setDataInDataSource(response);
      },
      error: err => this.toastrService.error('Erro ao buscar tarefa por usuário', err.error.details ? err.error.details : "Consulta")});
  }

  private findTarefaPerUsuarioAndSituacaoTarefa(usuarioId: number, situacao: situacaoTarefaEnum)  {
    this.tarefaService.findTarefaPerUsuarioAndSituacaoTarefa(usuarioId, situacao).subscribe({
      next: response => {
        this.setDataInDataSource(response);
      },
      error: err => this.toastrService.error('Erro ao buscar tarefa por usuário e situação', err.error.details ? err.error.details : "Consulta")});
  }

  private setDataInDataSource(response: Tarefa[]) {
    this.dataSource.data = []
    this.dataSource = new MatTableDataSource<Tarefa>(response);
    this.dataSource.data.length === 0 ? this.checkDataInfo = true : this.checkDataInfo = false;
  }

  download() {
    this.relatorioService.downloadRelatorio(this.dataSource.data).subscribe((pdfBlob: Blob) => {
      const blobUrl = window.URL.createObjectURL(pdfBlob);
      window.open(blobUrl, '_blank');

    })
  }

  formatDatePtBr(date: string) {
    return convertFormatDatePtBr(date);
  }
}
