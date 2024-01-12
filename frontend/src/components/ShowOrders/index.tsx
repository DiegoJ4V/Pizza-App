import { useEffect, useState } from "react";
import { useShoppingCart } from "../../hooks/useShoppingCart"
import type { Pizza } from "../../types";
import { IncreaseQuantity } from "../IncreaseQuantity";
import Styles from './ShowOrders.module.css'

export function ShowOrders() {
   const [pizza, setPizza] = useState<Pizza[]>()
   const pizzaList = useShoppingCart((state) => state.pizza)
   const removePizza = useShoppingCart((state) => state.removePizza)

   useEffect(() => setPizza(pizzaList), [pizzaList])
   
   const removeElement = (id: string) => {
      if (id) {
         removePizza(id)

         const getLocalStorage = localStorage.getItem('allPizza') ?? '[]'
         const pizzaLocalStorage: Pizza[] = JSON.parse(getLocalStorage)

         localStorage.setItem('allPizza', JSON.stringify(pizzaLocalStorage?.filter((element) => element.id !== id)))
      }
   }
   

   return (
      <div>
         {pizza ? 
            pizza?.map((element) => (
               <article key={element.id}>
                  <h3>{element.name}</h3>
                  <p>{element.ingredients.map((ingredient) => ingredient.name).join(', ')}</p>
                  <IncreaseQuantity defaultValue={element.quantity} />
                  <button type='button' onClick={() => removeElement(element.id ?? '')}>X</button>
               </article>
            )) : <div role="progressbar" aria-labelledby="loading content" className={Styles.spin}></div>
         }
      </div>
   )
}