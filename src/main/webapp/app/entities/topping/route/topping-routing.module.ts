import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ToppingComponent } from '../list/topping.component';
import { ToppingDetailComponent } from '../detail/topping-detail.component';
import { ToppingUpdateComponent } from '../update/topping-update.component';
import { ToppingRoutingResolveService } from './topping-routing-resolve.service';

const toppingRoute: Routes = [
  {
    path: '',
    component: ToppingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ToppingDetailComponent,
    resolve: {
      topping: ToppingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ToppingUpdateComponent,
    resolve: {
      topping: ToppingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ToppingUpdateComponent,
    resolve: {
      topping: ToppingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(toppingRoute)],
  exports: [RouterModule],
})
export class ToppingRoutingModule {}
