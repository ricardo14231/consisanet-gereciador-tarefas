import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { Usuario } from 'src/app/models/usuario.model';
import { UsuarioTarefaService } from 'src/app/services/usuario-tarefa.service';
import { stringEmptyValidator } from 'src/app/utils/validString';

@Component({
  selector: 'app-form-usuario',
  templateUrl: './form-usuario.component.html',
  styleUrls: ['./form-usuario.component.css']
})
export class FormUsuarioComponent {

  usuarioForm!: FormGroup;

  private usuario!: Usuario;
  
  private _subscription!: Subscription;

  constructor(
    public usuarioService: UsuarioTarefaService,
    private toastrService: ToastrService,
    private router: Router,
    private formBuider: FormBuilder
  ) { }

  ngOnInit(): void {

    this.initEmptyObjUsuario();
    this.createFormControl(this.usuario);

    if (this.usuarioService.isEdit) {
      this.usuario = this.usuarioService.usuario;
    
      this.usuarioForm.patchValue({
        nome: this.usuario.nome,
      });
    }
  }

  createFormControl(usuario: Usuario) {
    //TODO adicionar validação data
    const datePattern = /^\d{2}\/\d{2}\/\d{4}$/gm;

    this.usuarioForm = this.formBuider.group({
      nome: [usuario.nome, [Validators.required, Validators.maxLength(100), stringEmptyValidator()]]
    });
  }

  onSave() {
    
    this.captureFormData();
    this.usuarioService.onSave(this.usuario);

    this._subscription = this.usuarioService.responseOnSave.subscribe(() => {
      this.toastrService.success("Sucesso ao criar o usuário!", "Cadastro");
      this.router.navigate(['/list-usuario']);

    }, () => this.toastrService.error("Erro ao gravar o usuário!", 'Cadastro'));
  }

  private captureFormData() {

    this.usuario.nome = this.usuarioForm.getRawValue().nome;   
  }

  public resetForm(): void {
    this.usuarioForm.reset();
  }

  public cancelForm(): void {
    this.usuarioService.isEdit = false;
    this.router.navigate(['/list-usuario']);
  }

  private initEmptyObjUsuario(): void {
    this.usuario = {
      id: undefined,
      nome: "",
      createAt: ""
    };
  }

  ngOnDestroy(): void {
    this._subscription?.unsubscribe();
  }
}
