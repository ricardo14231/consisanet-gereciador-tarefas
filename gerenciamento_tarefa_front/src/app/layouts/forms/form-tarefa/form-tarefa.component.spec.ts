import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormTarefaComponent } from './form-tarefa.component';

describe('FormTarefaComponent', () => {
  let component: FormTarefaComponent;
  let fixture: ComponentFixture<FormTarefaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormTarefaComponent]
    });
    fixture = TestBed.createComponent(FormTarefaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
