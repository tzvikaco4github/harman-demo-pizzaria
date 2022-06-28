import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITopping, Topping } from '../topping.model';

import { ToppingService } from './topping.service';

describe('Topping Service', () => {
  let service: ToppingService;
  let httpMock: HttpTestingController;
  let elemDefault: ITopping;
  let expectedResult: ITopping | ITopping[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ToppingService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      price: 0,
      description: 'AAAAAAA',
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

    it('should create a Topping', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Topping()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Topping', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          price: 1,
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Topping', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          price: 1,
        },
        new Topping()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Topping', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          price: 1,
          description: 'BBBBBB',
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

    it('should delete a Topping', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addToppingToCollectionIfMissing', () => {
      it('should add a Topping to an empty array', () => {
        const topping: ITopping = { id: 123 };
        expectedResult = service.addToppingToCollectionIfMissing([], topping);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(topping);
      });

      it('should not add a Topping to an array that contains it', () => {
        const topping: ITopping = { id: 123 };
        const toppingCollection: ITopping[] = [
          {
            ...topping,
          },
          { id: 456 },
        ];
        expectedResult = service.addToppingToCollectionIfMissing(toppingCollection, topping);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Topping to an array that doesn't contain it", () => {
        const topping: ITopping = { id: 123 };
        const toppingCollection: ITopping[] = [{ id: 456 }];
        expectedResult = service.addToppingToCollectionIfMissing(toppingCollection, topping);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(topping);
      });

      it('should add only unique Topping to an array', () => {
        const toppingArray: ITopping[] = [{ id: 123 }, { id: 456 }, { id: 24333 }];
        const toppingCollection: ITopping[] = [{ id: 123 }];
        expectedResult = service.addToppingToCollectionIfMissing(toppingCollection, ...toppingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const topping: ITopping = { id: 123 };
        const topping2: ITopping = { id: 456 };
        expectedResult = service.addToppingToCollectionIfMissing([], topping, topping2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(topping);
        expect(expectedResult).toContain(topping2);
      });

      it('should accept null and undefined values', () => {
        const topping: ITopping = { id: 123 };
        expectedResult = service.addToppingToCollectionIfMissing([], null, topping, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(topping);
      });

      it('should return initial array if no Topping is added', () => {
        const toppingCollection: ITopping[] = [{ id: 123 }];
        expectedResult = service.addToppingToCollectionIfMissing(toppingCollection, undefined, null);
        expect(expectedResult).toEqual(toppingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
