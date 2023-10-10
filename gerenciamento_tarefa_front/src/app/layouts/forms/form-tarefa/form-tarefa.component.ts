import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { situacaoTarefaEnum } from 'src/app/enums/situacaoTarefaEnum';
import { Tarefa } from 'src/app/models/tarefa.model';
import { Usuario } from 'src/app/models/usuario.model';
import { TarefaService } from 'src/app/services/tarefa.service';
import { UsuarioTarefaService } from 'src/app/services/usuario-tarefa.service';
import { formatDate } from 'src/app/utils/formatDate';
import { stringEmptyValidator } from 'src/app/utils/validString';

@Component({
  selector: 'app-form-tarefa',
  templateUrl: './form-tarefa.component.html',
  styleUrls: ['./form-tarefa.component.css']
})
export class FormTarefaComponent {

  tarefaForm!: FormGroup;

  private tarefa!: Tarefa;
  public listUsuarioTarefa: Usuario[] = [];
  public listTarefaPrincipal: Tarefa[] = [];
  private _subscription!: Subscription;

  constructor(
    public tarefaService: TarefaService,
    private toastrService: ToastrService,
    private router: Router,
    private formBuider: FormBuilder,
    private usuarioTarefaService: UsuarioTarefaService
  ) { }

  ngOnInit(): void {

    this.findAllUsuarioTarefa();
    this.findAllPrincipalTarefa();
    this.initEmptyObjTarefa();
    this.createFormControl(this.tarefa);

    if (this.tarefaService.isEdit) {
      this.tarefa = this.tarefaService.tarefa;
    
      this.tarefaForm.patchValue({
        nomeTarefa: this.tarefa.nomeTarefa,
        situacaoTarefa: this.tarefa.situacaoTarefa,
        dataInicio: this.tarefa.dataInicio,
        dataFim: this.tarefa.dataFim,
        usuarioResponsavel: this.tarefa.usuarioResponsavel,
        tarefaPrincipal: this.tarefa.tarefaPrincipal
      })
    }
  }

  private findAllUsuarioTarefa() {
    this.usuarioTarefaService.listAllUsuario().subscribe({
      next: responseUsuario => {
        this.listUsuarioTarefa = responseUsuario;
      },
      error: () => this.toastrService.error("Erro ao listar os usuarios!", 'Listar')
    })
  }

  private findAllPrincipalTarefa() {
    this.tarefaService.listAllTarefa().subscribe({
      next: responseTarefa => {
        this.listTarefaPrincipal = responseTarefa;
      },
      error: () => this.toastrService.error("Erro ao listar os tarefas principais!", 'Listar')
    })
  }

  createFormControl(tarefa: Tarefa) {
    //TODO adicionar validação data
    const datePattern = /^\d{2}\/\d{2}\/\d{4}$/gm;

    this.tarefaForm = this.formBuider.group({
      nomeTarefa: [tarefa.nomeTarefa, [Validators.required, Validators.maxLength(150), stringEmptyValidator()]],
      situacaoTarefa: [tarefa.situacaoTarefa, [Validators.required]],
      dataInicio: [tarefa.dataInicio, []],
      dataFim: [tarefa.dataFim, [Validators.required]],
      usuarioResponsavel: [this.listUsuarioTarefa],
      tarefaPrincipal: [this.listTarefaPrincipal]
    });
  }
  
  onSave() {
    
    this.captureFormData();
    this.tarefaService.onSave(this.tarefa);

    this._subscription = this.tarefaService.responseOnSave.subscribe(() => {
      this.toastrService.success("Sucesso ao criar a tarefa!", "Cadastro");
      this.router.navigate(['/list-tarefa']);

    }, (err) => {
      console.log(err)
      this.toastrService.error("Erro ao gravar a tarefa!", 'Cadastro');
    })
  }

  private captureFormData() {

    const nomeTarefa = this.tarefaForm.getRawValue().nomeTarefa;
    const dataFim = formatDate(this.tarefaForm.getRawValue().dataFim);
    const situacaoTarefa =  this.tarefaForm.getRawValue().situacaoTarefa;
    const usuarioResponsavel = this.tarefaForm.getRawValue().usuarioResponsavel;
    const tarefaPrincipal = this.tarefaForm.getRawValue().tarefaPrincipal;

    this.tarefa.nomeTarefa = nomeTarefa ? nomeTarefa : null;
    this.tarefa.dataFim = dataFim ? dataFim : "0000-00-00";
    this.tarefa.situacaoTarefa = situacaoTarefa ? situacaoTarefa : situacaoTarefaEnum.AGUARDANDO
    if(this.isTarefaVazia(usuarioResponsavel)) {
      delete this.tarefa.usuarioResponsavel;
    } else {
      this.tarefa.usuarioResponsavel = usuarioResponsavel;
    }
  
    if(this.isTarefaVazia(tarefaPrincipal)) {
      delete this.tarefa.tarefaPrincipal;
    } else {
      this.tarefa.tarefaPrincipal = tarefaPrincipal;
    }
  }

  private isTarefaVazia(tarefa: Tarefa | any): boolean {
    for (const prop in tarefa) {
      if (tarefa.hasOwnProperty(prop) && tarefa[prop] !== null && tarefa[prop] !== undefined) {
        return false;
      }
    }
    return true;
  }


  public resetForm(): void {
    this.tarefaForm.reset();
  }

  public cancelForm(): void {
    this.tarefaService.isEdit = false;
    this.router.navigate(['/list-tarefa']);
  }

  public compareUsuarioOrTarefa(object1: Usuario | Tarefa, object2: Usuario | Tarefa) {
    return object1 && object2 && object1.id == object2.id;
  }

  private initEmptyObjTarefa(): void {
    this.tarefa = {
      id: undefined,
      nomeTarefa: "",
      dataInicio: "",
      dataFim: "",
      situacaoTarefa: "",
      createAt: ""
    };
  }

  ngOnDestroy(): void {
    this._subscription?.unsubscribe();
  }
}
