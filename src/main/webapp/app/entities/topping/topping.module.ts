import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ToppingComponent } from './list/topping.component';
import { ToppingDetailComponent } from './detail/topping-detail.component';
import { ToppingUpdateComponent } from './update/topping-update.component';
import { ToppingDeleteDialogComponent } from './delete/topping-delete-dialog.component';
import { ToppingRoutingModule } from './route/topping-routing.module';

@NgModule({
  imports: [SharedModule, ToppingRoutingModule],
  declarations: [ToppingComponent, ToppingDetailComponent, ToppingUpdateComponent, ToppingDeleteDialogComponent],
  entryComponents: [ToppingDeleteDialogComponent],
})
export class ToppingModule {}
