import type { Quantity } from '@/constants/quantity'

interface Page {
   pageable: {
      pageNumber: number;
      pageSize:   number;
      offset:     number;
      paged:      boolean;
      unpaged:    boolean;
   },
   totalPages:       number;
   totalElements:    number;
   last:             boolean;
   first:            boolean;
   size:             number;
   number:           number;
   numberOfElements: number;
   empty:            boolean;
}

export interface Pizza {
   id?:         `${string}-${string}-${string}-${string}-${string}`;
   pizzaName:        string;
   image: string;
   size:        Size;
   quantity: number;
   ingredientNameDtoList: Ingredient[];
}

export interface Ingredient {
   id?: number;
   name: string;
   quantity: Quantity;
}

export interface IngredientRequest {
   idIngredient: number;
   ingredientName: string;
   ingredientType: IngredientTypes;
   authorImage: string;
   urlImage: string;
}

export interface CustomerLogIn {
   email: string;
   password: string;
}

export interface Customer {
   customerName: string;
   email: string;
   birthDate: string;
}

export interface ValuesForChangeDto {
   id: string,
   password: string
}

export interface CustomerDto {
   customerName: string;
   email: string;
   password: string;
   matchingPassword: string;
   birthDate: string;
}

export interface OrderDomain {
   orderId: string,
   orderTimestamp: string,
   totalProducts: number
   total: number
}

export interface Order {
   idCustomer: number;
   country: string;
   state: string;
   city: string;
   street: string;
   houseNumber: number;
   apartment: number | null;
   floor: number | null;
   pizzaList: Pizza[];
}

export interface PageOrder extends Page {
   content: OrderDomain[]
}

export interface UserInputProps {
   label: string;
   description?: string;
   required?: boolean;
   error?: string;
   disable?: boolean;
}

export interface ProfileLinks {
   url: string,
   name: string,
   active?: boolean
}