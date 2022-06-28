import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PizzaService } from '../service/pizza.service';
import { IPizza, Pizza } from '../pizza.model';

import { PizzaUpdateComponent } from './pizza-update.component';

describe('Pizza Management Update Component', () => {
  let comp: PizzaUpdateComponent;
  let fixture: ComponentFixture<PizzaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pizzaService: PizzaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PizzaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PizzaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PizzaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pizzaService = TestBed.inject(PizzaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pizza: IPizza = { id: 456 };

      activatedRoute.data = of({ pizza });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pizza));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pizza>>();
      const pizza = { id: 123 };
      jest.spyOn(pizzaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pizza });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pizza }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pizzaService.update).toHaveBeenCalledWith(pizza);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pizza>>();
      const pizza = new Pizza();
      jest.spyOn(pizzaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pizza });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pizza }));
      saveSubject.complete();

      // THEN
      expect(pizzaService.create).toHaveBeenCalledWith(pizza);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pizza>>();
      const pizza = { id: 123 };
      jest.spyOn(pizzaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pizza });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pizzaService.update).toHaveBeenCalledWith(pizza);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
