import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PizzaDetailComponent } from './pizza-detail.component';

describe('Pizza Management Detail Component', () => {
  let comp: PizzaDetailComponent;
  let fixture: ComponentFixture<PizzaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PizzaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pizza: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PizzaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PizzaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pizza on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pizza).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
