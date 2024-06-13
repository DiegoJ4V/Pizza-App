import { getJSON } from '../utils/getJSON.mjs'
import express from 'express'

const customerApp = express()
customerApp.use(express.json())

const CUSTOMER_PORT = 8765

customerApp.get('/customer/32', (_req, res) => {
   res.status(200).json(getJSON('../mocks/fixtures/customer.json'))
})

customerApp.listen(CUSTOMER_PORT)

const dataApp = express()
dataApp.use(express.json())
dataApp.use((_req, res, next) => {
   res.setHeader('Access-Control-Allow-Origin', 'http://localhost:4321')
   res.setHeader('Access-Control-Allow-Credentials', true)
   res.setHeader('Access-Control-Allow-Methods', 'GET', 'POST', 'PUT', 'DELETE', 'PATCH')
   res.setHeader('Access-Control-Allow-Headers', 'Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers')
   next()
})

const DATA_PORT = 4436

function getModifyOrders(pageNumber) {
   const { content, pageable, ...rest } = getJSON('../mocks/fixtures/orders.json')

   return {
      content: content.map(({ orderId, ...rest}) => ({ orderId: crypto.randomUUID(), ...rest })),
      pageable: {
         ...pageable,
         pageNumber
      },
      ...rest
   }
}

dataApp.get('/order/customer/32', (req, res) => {
   const { page } = req.query
   
   if (page == 0) {
      res.status(200).json(getJSON('../mocks/fixtures/orders.json'))
   }
   else if (page == 1) {
      res.status(200).json(getModifyOrders(1))
   }
   else if (page == 2) {
      res.status(200).json(getModifyOrders(2))
   }
   else if (page == 3) {
      res.status(200).json(getModifyOrders(3))
   }
   else if (page == 4) {
      const { last, ...rest } = getModifyOrders(4)
      res.status(200).json({
         ...rest,
         last: true
      })
   }
})

dataApp.listen(DATA_PORT)