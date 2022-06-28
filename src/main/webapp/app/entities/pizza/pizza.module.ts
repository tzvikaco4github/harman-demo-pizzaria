import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PizzaComponent } from './list/pizza.component';
import { PizzaDetailComponent } from './detail/pizza-detail.component';
import { PizzaUpdateComponent } from './update/pizza-update.component';
import { PizzaDeleteDialogComponent } from './delete/pizza-delete-dialog.component';
import { PizzaRoutingModule } from './route/pizza-routing.module';

@NgModule({
  imports: [SharedModule, PizzaRoutingModule],
  declarations: [PizzaComponent, PizzaDetailComponent, PizzaUpdateComponent, PizzaDeleteDialogComponent],
  entryComponents: [PizzaDeleteDialogComponent],
})
export class PizzaModule {}
