import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ToppingDetailComponent } from './topping-detail.component';

describe('Topping Management Detail Component', () => {
  let comp: ToppingDetailComponent;
  let fixture: ComponentFixture<ToppingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ToppingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ topping: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ToppingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ToppingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load topping on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.topping).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
