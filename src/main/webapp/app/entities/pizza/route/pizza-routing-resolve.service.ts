import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPizza, Pizza } from '../pizza.model';
import { PizzaService } from '../service/pizza.service';

@Injectable({ providedIn: 'root' })
export class PizzaRoutingResolveService implements Resolve<IPizza> {
  constructor(protected service: PizzaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPizza> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pizza: HttpResponse<Pizza>) => {
          if (pizza.body) {
            return of(pizza.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pizza());
  }
}
