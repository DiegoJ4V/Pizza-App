export function getProfileLinks(customerId: number, role: '' | 'USER' | 'ADMIN', active: 'PROFILE' | 'ORDER' | 'INGREDIENT') {
   const profileLinks = [{
      url: '/client/customer/' + customerId,
      name: 'Profile',
      active: active === 'PROFILE'
   }, {
      url: `/client/customer/${customerId}/orders`,
      name: 'Orders',
      active: active === 'ORDER'
   }]

   if (role === 'ADMIN') profileLinks.push({
      url: '/client/customer/ingredient',
      name: 'Ingredient',
      active: active === 'INGREDIENT'
   })


   return profileLinks
}