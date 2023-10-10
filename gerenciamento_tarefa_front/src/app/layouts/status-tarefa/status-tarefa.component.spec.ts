import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusTarefaComponent } from './status-tarefa.component';

describe('StatusTarefaComponent', () => {
  let component: StatusTarefaComponent;
  let fixture: ComponentFixture<StatusTarefaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StatusTarefaComponent]
    });
    fixture = TestBed.createComponent(StatusTarefaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
