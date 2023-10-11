import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './layouts/home/home.component';
import { StatusTarefaComponent } from './layouts/status-tarefa/status-tarefa.component';
import { FormTarefaComponent } from './layouts/forms/form-tarefa/form-tarefa.component';
import { ListUsuarioComponent } from './layouts/list-usuario/list-usuario.component';
import { FormUsuarioComponent } from './layouts/forms/form-usuario/form-usuario.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '/list-tarefa' },
  {
    path: 'list-tarefa',
    component: HomeComponent,
    children: [
      {
        path: '',
        component: StatusTarefaComponent
      }
    ]
  },
  {
    path: 'tarefa',
    component: HomeComponent,
    children: [
      {
        path: '',
        component: FormTarefaComponent
      }
    ],
  },
  {
    path: 'list-usuario',
    component: HomeComponent,
    children: [
      {
        path: '',
        component: ListUsuarioComponent
      }
    ],
  },
  {
    path: 'usuario',
    component: HomeComponent,
    children: [
      {
        path: '',
        component: FormUsuarioComponent
      }
    ],
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})

export class AppRoutingModule { }
