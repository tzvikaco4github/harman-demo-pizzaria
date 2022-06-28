import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ToppingService } from '../service/topping.service';
import { ITopping, Topping } from '../topping.model';

import { ToppingUpdateComponent } from './topping-update.component';

describe('Topping Management Update Component', () => {
  let comp: ToppingUpdateComponent;
  let fixture: ComponentFixture<ToppingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let toppingService: ToppingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ToppingUpdateComponent],
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
      .overrideTemplate(ToppingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ToppingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    toppingService = TestBed.inject(ToppingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const topping: ITopping = { id: 456 };

      activatedRoute.data = of({ topping });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(topping));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Topping>>();
      const topping = { id: 123 };
      jest.spyOn(toppingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ topping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: topping }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(toppingService.update).toHaveBeenCalledWith(topping);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Topping>>();
      const topping = new Topping();
      jest.spyOn(toppingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ topping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: topping }));
      saveSubject.complete();

      // THEN
      expect(toppingService.create).toHaveBeenCalledWith(topping);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Topping>>();
      const topping = { id: 123 };
      jest.spyOn(toppingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ topping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(toppingService.update).toHaveBeenCalledWith(topping);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
