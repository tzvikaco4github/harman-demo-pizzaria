import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPizza, getPizzaIdentifier } from '../pizza.model';

export type EntityResponseType = HttpResponse<IPizza>;
export type EntityArrayResponseType = HttpResponse<IPizza[]>;

@Injectable({ providedIn: 'root' })
export class PizzaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pizzas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pizza: IPizza): Observable<EntityResponseType> {
    return this.http.post<IPizza>(this.resourceUrl, pizza, { observe: 'response' });
  }

  update(pizza: IPizza): Observable<EntityResponseType> {
    return this.http.put<IPizza>(`${this.resourceUrl}/${getPizzaIdentifier(pizza) as number}`, pizza, { observe: 'response' });
  }

  partialUpdate(pizza: IPizza): Observable<EntityResponseType> {
    return this.http.patch<IPizza>(`${this.resourceUrl}/${getPizzaIdentifier(pizza) as number}`, pizza, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPizza>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPizza[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPizzaToCollectionIfMissing(pizzaCollection: IPizza[], ...pizzasToCheck: (IPizza | null | undefined)[]): IPizza[] {
    const pizzas: IPizza[] = pizzasToCheck.filter(isPresent);
    if (pizzas.length > 0) {
      const pizzaCollectionIdentifiers = pizzaCollection.map(pizzaItem => getPizzaIdentifier(pizzaItem)!);
      const pizzasToAdd = pizzas.filter(pizzaItem => {
        const pizzaIdentifier = getPizzaIdentifier(pizzaItem);
        if (pizzaIdentifier == null || pizzaCollectionIdentifiers.includes(pizzaIdentifier)) {
          return false;
        }
        pizzaCollectionIdentifiers.push(pizzaIdentifier);
        return true;
      });
      return [...pizzasToAdd, ...pizzaCollection];
    }
    return pizzaCollection;
  }
}
