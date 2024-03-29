import { Quantity } from '@/constants/quantity'
import { saveOrder } from '@/services/orderService'

describe('Order service tests', () => { 
   it('Should be a function', () => {
      expect(typeof saveOrder).toBe('function')
   }),

   it('Should reject the send value', async () => {
      await expect(saveOrder({
         idCustomer: 4235,
         country: 'México',
         city: 'cdmx',
         street: 'some',
         houseNumber: 3213,
         floor: null,
         apartment: null,
         pizzaList: [{
            id: '480870a9-af45-4d2c-bda2-7a6e6e3a1ab8',
            pizzaName: 'Pepperoni',
            size: 'LARGE',
            ingredientNameDtoList: [{
               name: 'Pepperoni',
               quantity: Quantity.NORMAL
            }, {
               name: 'Mozzarella',
               quantity: Quantity.EXTRA
            }]
         }]
      })).rejects.toThrow('Something bad happen')
   })

   it('Should accept the send value', async () => {
      const responseValue = await saveOrder({
         idCustomer: 234,
         country: 'México',
         city: 'cdmx',
         street: 'some',
         houseNumber: 3213,
         floor: null,
         apartment: null,
         pizzaList: [{
            id: '480870a9-af45-4d2c-bda2-7a6e6e3a1ab8',
            pizzaName: 'Pepperoni',
            size: 'LARGE',
            ingredientNameDtoList: [{
               name: 'Pepperoni',
               quantity: Quantity.NORMAL
            }, {
               name: 'Mozzarella',
               quantity: Quantity.EXTRA
            }]
         }]
      })

      expect(responseValue).toStrictEqual(201)
   })
})