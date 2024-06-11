import express from 'express'
import { CustomerRoleRepositoryImpl } from './repository/CustomerRoleRepositoryImpl.js'
import { createJwtRouter } from './web/routes/JwtRoutes.js'
import { CustomerMessage } from './message/CustomerMessages.js'

const app = express()
app.use(express.json())

app.use('/jwt', createJwtRouter(new CustomerRoleRepositoryImpl()))

const PORT = 3000

new CustomerMessage().onSaveCustomerRole()

const listen = app.listen(PORT, () => {
   console.log(`Server running on port ${PORT}`)
})

export { app, listen }
