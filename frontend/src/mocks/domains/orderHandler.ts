import { HttpResponse, http, type PathParams } from 'msw'
import type { Order } from '@/types'

const API = 'http://localhost/data/order'

export const orderHandler = [
   http.post<PathParams<never>, Order>(`${API}/save`, async ({ request }) => {
      const { idCustomer } = await request.json()

      if (String(idCustomer) === '234') {
         return new HttpResponse('Order save correctly', { status: 201 })
      }      

      return HttpResponse.json({
         desc: 'Invalid Request Content',
         fieldError: {
            name: 'Name must not be blank'
         }
      }, { status: 400 })
   })
]