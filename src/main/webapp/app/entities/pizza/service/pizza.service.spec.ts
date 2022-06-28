import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PizzaSize } from 'app/entities/enumerations/pizza-size.model';
import { IPizza, Pizza } from '../pizza.model';

import { PizzaService } from './pizza.service';

describe('Pizza Service', () => {
  let service: PizzaService;
  let httpMock: HttpTestingController;
  let elemDefault: IPizza;
  let expectedResult: IPizza | IPizza[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PizzaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      pizzaSize: PizzaSize.SMALL,
      price: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Pizza', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Pizza()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pizza', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          pizzaSize: 'BBBBBB',
          price: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pizza', () => {
      const patchObject = Object.assign(
        {
          price: 1,
        },
        new Pizza()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pizza', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          pizzaSize: 'BBBBBB',
          price: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Pizza', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPizzaToCollectionIfMissing', () => {
      it('should add a Pizza to an empty array', () => {
        const pizza: IPizza = { id: 123 };
        expectedResult = service.addPizzaToCollectionIfMissing([], pizza);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pizza);
      });

      it('should not add a Pizza to an array that contains it', () => {
        const pizza: IPizza = { id: 123 };
        const pizzaCollection: IPizza[] = [
          {
            ...pizza,
          },
          { id: 456 },
        ];
        expectedResult = service.addPizzaToCollectionIfMissing(pizzaCollection, pizza);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pizza to an array that doesn't contain it", () => {
        const pizza: IPizza = { id: 123 };
        const pizzaCollection: IPizza[] = [{ id: 456 }];
        expectedResult = service.addPizzaToCollectionIfMissing(pizzaCollection, pizza);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pizza);
      });

      it('should add only unique Pizza to an array', () => {
        const pizzaArray: IPizza[] = [{ id: 123 }, { id: 456 }, { id: 62158 }];
        const pizzaCollection: IPizza[] = [{ id: 123 }];
        expectedResult = service.addPizzaToCollectionIfMissing(pizzaCollection, ...pizzaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pizza: IPizza = { id: 123 };
        const pizza2: IPizza = { id: 456 };
        expectedResult = service.addPizzaToCollectionIfMissing([], pizza, pizza2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pizza);
        expect(expectedResult).toContain(pizza2);
      });

      it('should accept null and undefined values', () => {
        const pizza: IPizza = { id: 123 };
        expectedResult = service.addPizzaToCollectionIfMissing([], null, pizza, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pizza);
      });

      it('should return initial array if no Pizza is added', () => {
        const pizzaCollection: IPizza[] = [{ id: 123 }];
        expectedResult = service.addPizzaToCollectionIfMissing(pizzaCollection, undefined, null);
        expect(expectedResult).toEqual(pizzaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
