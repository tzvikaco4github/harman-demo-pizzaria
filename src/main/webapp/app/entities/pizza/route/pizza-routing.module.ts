import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PizzaComponent } from '../list/pizza.component';
import { PizzaDetailComponent } from '../detail/pizza-detail.component';
import { PizzaUpdateComponent } from '../update/pizza-update.component';
import { PizzaRoutingResolveService } from './pizza-routing-resolve.service';

const pizzaRoute: Routes = [
  {
    path: '',
    component: PizzaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PizzaDetailComponent,
    resolve: {
      pizza: PizzaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PizzaUpdateComponent,
    resolve: {
      pizza: PizzaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PizzaUpdateComponent,
    resolve: {
      pizza: PizzaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pizzaRoute)],
  exports: [RouterModule],
})
export class PizzaRoutingModule {}
