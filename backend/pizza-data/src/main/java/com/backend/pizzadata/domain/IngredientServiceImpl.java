package com.backend.pizzadata.domain;

import com.backend.pizzadata.domain.service.IngredientService;
import com.backend.pizzadata.exceptions.NotAllowedException;
import com.backend.pizzadata.persistence.entity.IngredientEntity;
import com.backend.pizzadata.persistence.repository.IngredientRepository;
import com.backend.pizzadata.web.dto.IngredientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

   private final IngredientRepository ingredientRepository;

   @Autowired
   public IngredientServiceImpl(IngredientRepository ingredientRepository) {
      this.ingredientRepository = ingredientRepository;
   }

   @Override
   public List<IngredientEntity> getAllIngredients() {
      return ingredientRepository.findAll();
   }

   @Override
   public void saveIngredient(IngredientDto ingredientDto) throws NotAllowedException {
      var ingredientEntity = convertIngredientDtoToEntity(Collections.singletonList(ingredientDto)).getFirst();

      ingredientRepository.save(ingredientEntity);
   }

   @Override
   public void saveIngredientList(List<IngredientDto> ingredientDtoList) throws NotAllowedException {
      var ingredientEntityList = convertIngredientDtoToEntity(ingredientDtoList);

      ingredientRepository.saveAll(ingredientEntityList);
   }

   private List<IngredientEntity> convertIngredientDtoToEntity(List<IngredientDto> ingredientDtoList) throws NotAllowedException {
      var ingredientList = new ArrayList<IngredientEntity>();

      for(var ingredientDto : ingredientDtoList) {
         if (ingredientDto.ingredientName().equals("No repeat"))
            throw new NotAllowedException("Repeat names are not allowed");

         ingredientList.add(
            IngredientEntity.builder()
                    .ingredientName(ingredientDto.ingredientName())
                    .ingredientType(ingredientDto.ingredientType())
                    .urlImage(ingredientDto.urlImage())
                    .authorImage(ingredientDto.authorImage())
                    .build()
         );
      }

      return ingredientList;
   }
}
