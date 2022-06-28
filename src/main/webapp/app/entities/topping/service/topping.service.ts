import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITopping, getToppingIdentifier } from '../topping.model';

export type EntityResponseType = HttpResponse<ITopping>;
export type EntityArrayResponseType = HttpResponse<ITopping[]>;

@Injectable({ providedIn: 'root' })
export class ToppingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/toppings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(topping: ITopping): Observable<EntityResponseType> {
    return this.http.post<ITopping>(this.resourceUrl, topping, { observe: 'response' });
  }

  update(topping: ITopping): Observable<EntityResponseType> {
    return this.http.put<ITopping>(`${this.resourceUrl}/${getToppingIdentifier(topping) as number}`, topping, { observe: 'response' });
  }

  partialUpdate(topping: ITopping): Observable<EntityResponseType> {
    return this.http.patch<ITopping>(`${this.resourceUrl}/${getToppingIdentifier(topping) as number}`, topping, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITopping>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITopping[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addToppingToCollectionIfMissing(toppingCollection: ITopping[], ...toppingsToCheck: (ITopping | null | undefined)[]): ITopping[] {
    const toppings: ITopping[] = toppingsToCheck.filter(isPresent);
    if (toppings.length > 0) {
      const toppingCollectionIdentifiers = toppingCollection.map(toppingItem => getToppingIdentifier(toppingItem)!);
      const toppingsToAdd = toppings.filter(toppingItem => {
        const toppingIdentifier = getToppingIdentifier(toppingItem);
        if (toppingIdentifier == null || toppingCollectionIdentifiers.includes(toppingIdentifier)) {
          return false;
        }
        toppingCollectionIdentifiers.push(toppingIdentifier);
        return true;
      });
      return [...toppingsToAdd, ...toppingCollection];
    }
    return toppingCollection;
  }
}
