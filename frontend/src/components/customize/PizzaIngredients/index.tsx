import { CardContainer } from '@/components/common/CardContainer'
import { SelectQuantity } from '@/components/order/SelectQuantity'
import { useDesireIngredients } from '@/hooks/useDesireIngredients'
import ingredientList from '@/mocks/fixtures/ingredients.json'
import { useState } from 'react'
import Styles from './PizzaIngredients.module.css'

const ingredientTypeList = Object.freeze(['ALL', 'VEGETABLES', 'MEAT', 'CHEESE', 'SAUCES'])

interface Props {
   prebuildIngredients: string[]
}

export function PizzaIngredients({ prebuildIngredients }: Props) {
   const ingredients = useDesireIngredients((state) => state.ingredients)
   const addIngredient = useDesireIngredients((state) => state.addIngredient)
   const removeIngredient = useDesireIngredients((state) => state.removeIngredient)
   const [desiredType, setDesiredType] = useState(ingredientTypeList[0])

   return (
      <>
         <div className={Styles['ingredient-buttons']}>
            {ingredientTypeList.map((type) => (
               <button 
                  key={type}
                  className={type === desiredType ? Styles['active'] : ''}
                  onClick={() => setDesiredType(type)}
               >
                  {type[0] + type.substring(1).toLowerCase()}
               </button>
            ))}
         </div>
         {ingredientList.filter(({ ingredientType }) => (desiredType === ingredientTypeList[0] || desiredType === ingredientType))
         .map((ingredient) => (
            <CardContainer key={ingredient.idIngredient} styleClass={Styles['ingredients-card']}>
               <>
                  <figure>
                     <img 
                        src={`/client/images/${ingredient.urlImage}.jpg`}
                        alt={ingredient.ingredientName}
                        width='130'
                        height='80'
                        loading='lazy'
                        decoding='async'
                        />
                     <figcaption>
                        <p>{ingredient.authorImage}</p>
                     </figcaption>
                  </figure>
                  <h3>{ingredient.ingredientName}</h3>
                  <p>Quantity</p>
                  <SelectQuantity 
                     valueToShow={ingredients.length === 0 ? 
                        prebuildIngredients?.includes(ingredient.ingredientName) ? 1 : 0 
                        : ingredients.filter((element) => element?.name === ingredient.ingredientName )[0]?.quantity ?? 0
                     }
                     minValue={0}
                     maxValue={2}
                     increase={{
                        label: 'Increase quantity',
                        fun: () => addIngredient(ingredient.ingredientName)
                     }}
                     decrease={{
                        label: 'Decrease quantity',
                        fun: () => removeIngredient(ingredient.ingredientName)
                     }}
                  />
               </>
            </CardContainer>
         ))}
      </>
   )
}