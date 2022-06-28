import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITopping, Topping } from '../topping.model';
import { ToppingService } from '../service/topping.service';

@Injectable({ providedIn: 'root' })
export class ToppingRoutingResolveService implements Resolve<ITopping> {
  constructor(protected service: ToppingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITopping> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((topping: HttpResponse<Topping>) => {
          if (topping.body) {
            return of(topping.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Topping());
  }
}
