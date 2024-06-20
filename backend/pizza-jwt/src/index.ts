import express from 'express'
import { CustomerMessageImpl } from './message/CustomerMessages.js'
import { CustomerRoleRepositoryImpl } from './repository/CustomerRoleRepositoryImpl.js'
import { createHealthRoute } from './web/routes/HealthRoutes.js'
import { createJwtRouter } from './web/routes/JwtRoutes.js'

const app = express()
const PORT = globalThis.process.env.SERVER_PORT ?? 0
app.use(express.json())

app.use('/jwt', createJwtRouter(new CustomerRoleRepositoryImpl()))
app.use('/health', createHealthRoute(new CustomerRoleRepositoryImpl(), new CustomerMessageImpl()))

const customerMessage = new CustomerMessageImpl()
customerMessage.onSaveCustomerRole()

const listen = app.listen(PORT, function() {
   console.log(`Server running on port ${this.address().port}`)
})

export { app, listen }
