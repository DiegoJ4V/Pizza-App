import { ingredientHandler } from '@/mocks/domains/ingredientHandler'
import { expect, test } from '@/test/e2e/utils/fixture'
import { findNavbarElements } from '@/test/e2e/utils/navbarUtils'
import { getProfileLinks } from '@/utils/getProfileLinks'

test.describe('Ingredient page tests', () => {
   test.beforeEach(async ({ page, context }) => {
      await context.addCookies([{ name: 'jwt', value: 'token', domain: 'localhost', path: '/' }])
      await page.goto('http://localhost:4321/client/customer/ingredient')
   })

   test('Should render ingredient page correctly', async ({ page }) => {
      await expect(page).toHaveTitle('Create a new ingredient')

      await findNavbarElements(page)

      getProfileLinks(32, 'ADMIN', 'INGREDIENT').forEach(async ({ name }) => {
         await expect(page.getByRole('link', { name })).toBeVisible()
      })
      await expect(page.getByRole('link', { name: 'Ingredient' })).toHaveClass('active')

      await expect(page.getByRole('heading', { name: 'Create a new ingredient' })).toBeVisible()

      const ingredientInputs = ['Upload ingredient image', 'Image author', 'Ingredient name', 'Ingredient type']
      ingredientInputs.forEach(async (label) => {
         await expect(page.getByLabel(label)).toBeVisible()
      })
      await expect(page.getByLabel('Upload ingredient image')).toHaveAccessibleDescription('jpeg, png, bmp, webmp, gif (2MB Max).')
      await expect(page.getByLabel('Upload ingredient image')).toHaveValue('')
      await expect(page.getByLabel('Ingredient type')).toHaveValue('')

      await expect(page.getByRole('button', { name: 'Save Ingredient' })).toBeVisible()
   })

   test.skip('Should get an error if upload an image bigger than 2MB', async ({ page }) => {
      const fileChooserPromise = page.waitForEvent('filechooser')
      await page.getByLabel('Upload ingredient image').click()
      const fileChooser = await fileChooserPromise
      await fileChooser.setFiles('src/test/e2e/static/test.jpg')
      
      await expect(page.getByLabel('IImage to upload as the ingredient image')).not.toBeVisible()
      await expect(page.getByLabel('Upload ingredient image')).toHaveValue('C:\\fakepath\\test.jpg')
      await expect(page.getByLabel('Upload ingredient image'))
         .toHaveAccessibleDescription('jpeg, png, bmp, webmp, gif (2MB Max). Image need to be smaller (2MB or less)')
   })

   test('Should show a card with ingredient characteristics', async ({ page }) => {
      const fileChooserPromise = page.waitForEvent('filechooser')
      await page.getByLabel('Upload ingredient image').click()
      const fileChooser = await fileChooserPromise
      await fileChooser.setFiles('src/test/e2e/static/test.jpg')
      await expect(page.getByRole('img', { name: 'Image to upload as the ingredient image' })).toBeVisible()
      
      await page.getByLabel('Image author').fill('Author')
      await expect(page.getByText('Author: Author')).toBeVisible()
      
      await page.getByLabel('Ingredient name').fill('Show')
      await expect(page.getByText('Ingredient name: Show')).toBeVisible()
      
      await page.getByLabel('Ingredient type').selectOption({ index: 2 })
      await expect(page.getByText('Ingredient type: Meat')).toBeVisible()
   })

   test('Should get an error if the name send is repeated', async ({ page, worker }) => {
      const fileChooserPromise = page.waitForEvent('filechooser')
      await page.getByLabel('Upload ingredient image').click()
      const fileChooser = await fileChooserPromise
      await fileChooser.setFiles('src/test/e2e/static/test.jpg')
      await expect(page.getByRole('img', { name: 'Image to upload as the ingredient image' })).toBeVisible()
      
      await page.getByLabel('Image author').fill('Author')
      await page.getByLabel('Ingredient name').fill('Repeated')
      await page.getByLabel('Ingredient type').selectOption({ index: 2 })
      
      await worker.use(...ingredientHandler)
      
      await page.getByRole('button', { name: 'Save Ingredient' }).click()

      await expect(page.getByRole('alert').getByText('Warning')).toBeVisible()
      await expect(page.getByRole('alert').getByText('Repeat names are not allowed')).toBeVisible()
   })

   test('Should save the ingredient correctly', async ({ page, worker }) => {
      const fileChooserPromise = page.waitForEvent('filechooser')
      await page.getByLabel('Upload ingredient image').click()
      const fileChooser = await fileChooserPromise
      await fileChooser.setFiles('src/test/e2e/static/test.jpg')
      await expect(page.getByRole('img', { name: 'Image to upload as the ingredient image' })).toBeVisible()
      
      await page.getByLabel('Image author').fill('Author')
      await page.getByLabel('Ingredient name').fill('New')
      await page.getByLabel('Ingredient type').selectOption({ index: 2 })
      
      await worker.use(...ingredientHandler)
      
      await page.getByRole('button', { name: 'Save Ingredient' }).click()

      await expect(page.getByRole('alert').getByText('Success')).toBeVisible()
      await expect(page.getByRole('alert').getByText('Ingredient save correctly')).toBeVisible()
   })
})