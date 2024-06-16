import { CustomInput } from '@/components/common/CustomInput'
import { CustomSelect } from '@/components/common/CustomSelect'
import { FormContainer } from '@/components/common/FormContainer'
import { ImgContainer } from '@/components/common/ImgContainer'
import { IngredientTypes } from '@/constants/ingredientTypes'
import { useServicePromise } from '@/hooks/useServicePromise'
import { saveIngredient } from '@/services/ingredientsService'
import { getFormValue } from '@/utils/getFormValue'
import { useState, type ChangeEvent } from 'react'
import Styles from './IngredientForm.module.css'

export function IngredientForm() {
   const { response, isLoading, error, setError, handlePromise } = useServicePromise<FormData, string>(saveIngredient)
   const [imageUrl, setImageUrl] = useState('')

   const labels = {
      uploadImage: 'Upload ingredient image',
      imageAuthor: 'Image author',
      ingredientName: 'Ingredient name',
      ingredientType: 'Ingredient type'
   }

   const onChangeImageInput = (event: ChangeEvent<HTMLInputElement>) => {
      if (event.target instanceof HTMLInputElement) {
         const imageFile = event.target.files?.[0]

         if (imageFile instanceof File) {
            if (imageFile?.size > 1000 * 2000) {
               setError({ uploadImage: 'Image need to be smaller (2MB or less)' })
            } else {
               setImageUrl(URL.createObjectURL(imageFile))            
            }
         }         
      }
   }

   const handleData = (formValues: FormData) => {
      const imageFile = getFormValue(labels.uploadImage, formValues)

      const ingredientDto = JSON.stringify({
         authorImage: getFormValue(labels.imageAuthor, formValues),
         ingredientName: getFormValue(labels.ingredientName, formValues),
         ingredientType: getFormValue(labels.ingredientType, formValues)
      })
      const bytes = new TextEncoder().encode(ingredientDto)
      const ingredientFile = new Blob([bytes], { type: 'application/json' })

      const ingredientData = new FormData()
      ingredientData.set('file', imageFile)
      ingredientData.set('ingredient', ingredientFile)

      handlePromise(ingredientData)
   }

   return (
      <>
         {imageUrl ?
            <ImgContainer styleClass={Styles['image-container']}>
               <img
                  src={imageUrl}
                  alt='Image to upload as the ingredient image'
                  onLoad={() => {URL.revokeObjectURL(imageUrl)}}
               />
            </ImgContainer>
            : null
         }
         <FormContainer
            response={response}
            handleData={handleData}
            submitButton={{
               label: 'Save ingredient',
               isLoading: isLoading
            }}
         >
            <CustomInput
               label={labels.uploadImage}
               description='jpeg, png, bmp, webmp, gif (2MB Max)'
               type='file'
               required={true}
               disable={isLoading}
               accept='image/jpeg, image/png, image/bmp, image/webmp, image/gif'
               error={error?.uploadImage}
               onChange={onChangeImageInput}
            />
            <CustomInput
               label={labels.imageAuthor}
               type='text'
               required={true}
               disable={isLoading}
            />
            <CustomInput
               label={labels.ingredientName}
               type='text'
               required={true}
               disable={isLoading}
            />
            <CustomSelect
               label={labels.ingredientType}
               values={Object.keys(IngredientTypes)}
               options={Object.values(IngredientTypes)}
               defaultValue={{ text: 'Choose a type', value: '' }}
               required={true}
               disable={isLoading}
            />
         </FormContainer>
      </>
   )
}