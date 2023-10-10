import { Usuario } from "./usuario.model";

export interface Tarefa {
  id?: number,
  nomeTarefa: string,
  situacaoTarefa: string,
  dataInicio?: string,
  dataFim: string,
  usuarioResponsavel?: Usuario,
  tarefaPrincipal?: Tarefa,
  updateAt?: string,
  createAt: string
}