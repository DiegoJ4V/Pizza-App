import { useShoppingCart } from '@/hooks/useShoppingCart'
import { useEffect, useState } from 'react'
import Styles from './shoppingCart.module.css'

export function ShoppingCart() {
   const [isOpen, setIsOpen] = useState(false)
   const pizzaList = useShoppingCart((state) => state.pizza)
   const addPizza = useShoppingCart((state) => state.addPizza)
   
   useEffect(() => {
      if (pizzaList.length === 0) {
         const getLocalStorage = localStorage.getItem('allPizza') ?? ''
         if (getLocalStorage) {
            const pizza = JSON.parse(getLocalStorage)
   
            for (const pizzaElement of pizza) {
               addPizza(pizzaElement)
            }
         }
      }
   }, [])
   
   return (
      <button aria-label='Shopping cart' onClick={() => setIsOpen(!isOpen)}>
         <svg xmlns='http://www.w3.org/2000/svg' fill='none' stroke='currentColor' viewBox='0 0 24 24' className='humbleicons hi-cart'>
            <circle xmlns='http://www.w3.org/2000/svg' cx='7.5' cy='18.5' r='1.5' fill='currentColor'/><circle xmlns='http://www.w3.org/2000/svg' cx='16.5' cy='18.5' r='1.5' fill='currentColor'/><path xmlns='http://www.w3.org/2000/svg' stroke='currentColor' strokeLinecap='round' strokeLinejoin='round' strokeWidth='2' d='M3 5h2l.6 3m0 0L7 15h10l2-7H5.6z'/>
         </svg>
         <span className={Styles.quantity__identifier}>
            {pizzaList?.map((pizza) => pizza.quantity).reduce((accumulator, currentValue) => accumulator + currentValue, 0)}
         </span>
      </button>
   )
}