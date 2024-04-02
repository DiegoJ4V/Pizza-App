import { fireEvent, render, screen } from '@testing-library/react'
import { SelectQuantity } from '../../components/SelectQuantity'

describe('SelectQuantity component tests', () => { 
   it('Should render correctly', () => {
      render(<SelectQuantity />)

      expect(screen.getByRole('button', { name: 'Subtract pizza' })).toBeDefined()
      expect(screen.getByText(1)).toBeDefined()
      expect(screen.getByRole('button', { name: 'Add pizza' })).toBeDefined()
   })

   it('Should subtract the number correctly', () => {
      render(<SelectQuantity />)

      fireEvent.click(screen.getByRole('button', { name: 'Add pizza' }))
      expect(screen.getByText(2)).toBeDefined()
   })

   it('Should increase the number correctly', () => {
      render(<SelectQuantity/>)

      fireEvent.click(screen.getByRole('button', { name: 'Add pizza' }))
      fireEvent.click(screen.getByRole('button', { name: 'Add pizza' }))
      expect(screen.getByText(3)).toBeDefined()
   })
})