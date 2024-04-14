import { useId } from 'react'
import type { UserInputProps } from '@/types'

interface Props extends UserInputProps {
   type?: string;
   placeholder: string;
}

export function CustomInput({
   label,
   type = 'text',
   required = false,
   placeholder = 'example',
   description,
   error = false,
   disable = true
}: Props) {
   const customInputId = useId()
   
   return (
      <div className='user__input'>
         <label htmlFor={customInputId}>{label}</label>
         {description ? <p id={customInputId + '-describe'}>{description}</p> : null}
         <input 
            id={customInputId} 
            type={type}
            required={required}
            placeholder={placeholder}
            disabled={disable}
            aria-invalid={Boolean(error)}
            aria-describedby={customInputId + '-describe'}
         />
      </div>
   )
}
