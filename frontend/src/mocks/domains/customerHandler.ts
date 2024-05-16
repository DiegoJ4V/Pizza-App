import { http, HttpResponse, type PathParams } from 'msw'
import type { CustomerDto, CustomerLogIn } from '@/types'

const API = 'http://localhost:8765/customer'

export const customerHandler = [
   http.get(`${API}/:id`, ({ params }) => {
      const { id } = params
      if (id === '32') {
         return HttpResponse.json({
            'customerName': 'Customer',
            'email': 'random@random.com',
            'birthDate': '2002-06-12'
         })
      }

      return new HttpResponse(null, { status: 404 })
   }),

   http.post<PathParams<never>, CustomerLogIn>(`${API}/auth/login`, async ({ request }) => {
      const newCustomer = await request.json()

      if (newCustomer.password === '1234') {
         return new HttpResponse('4', {
            status: 200,
            headers: {
               'set-cookie': 'jwt=token; Domain=localhost; Path=/;',
            },
         })
      }

      return new HttpResponse(null, { status: 403 })
   }),

   http.post<PathParams<never>, CustomerDto>(`${API}/register`, async ({ request }) => {
      const newCustomer = await request.json()

      if (newCustomer.password === newCustomer.matchingPassword) {
         return HttpResponse.text('Account create successfully')
      }

      return HttpResponse.text("Passwords don't match")
   })
]