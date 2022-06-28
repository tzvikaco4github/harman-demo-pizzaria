export interface ITopping {
  id?: number;
  name?: string;
  price?: number;
  description?: string | null;
}

export class Topping implements ITopping {
  constructor(public id?: number, public name?: string, public price?: number, public description?: string | null) {}
}

export function getToppingIdentifier(topping: ITopping): number | undefined {
  return topping.id;
}
