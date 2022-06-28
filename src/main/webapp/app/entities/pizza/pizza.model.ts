import { PizzaSize } from 'app/entities/enumerations/pizza-size.model';

export interface IPizza {
  id?: number;
  pizzaSize?: PizzaSize;
  price?: number;
}

export class Pizza implements IPizza {
  constructor(public id?: number, public pizzaSize?: PizzaSize, public price?: number) {}
}

export function getPizzaIdentifier(pizza: IPizza): number | undefined {
  return pizza.id;
}
