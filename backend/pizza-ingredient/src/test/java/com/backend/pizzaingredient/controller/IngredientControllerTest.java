package com.backend.pizzaingredient.controller;

import com.backend.pizzaingredient.TestIngredientUtil;
import com.backend.pizzaingredient.advice.PizzaDataExceptionHandler;
import com.backend.pizzaingredient.constants.IngredientType;
import com.backend.pizzaingredient.domain.service.IngredientService;
import com.backend.pizzaingredient.exceptions.NotAllowedException;
import com.backend.pizzaingredient.web.IngredientController;
import com.backend.pizzaingredient.web.dto.IngredientDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@ActiveProfiles("test")
class IngredientControllerTest {

   private MockMvc mockMvc;

   @Autowired
   private IngredientController ingredientController;

   @MockBean
   private IngredientService ingredientService;

   @BeforeEach
   public void setUp() {
      this.mockMvc = MockMvcBuilders.standaloneSetup(ingredientController)
              .setControllerAdvice(new PizzaDataExceptionHandler())
              .build();
   }

   @Test
   @DisplayName("Should return a not found status instead of all ingredients")
   void getAllIngredients__NOT__FOUND() throws Exception {
      Mockito.when(ingredientService.getAllIngredients())
              .thenReturn(List.of());

      mockMvc.perform(MockMvcRequestBuilders.get("/")
                      .contentType(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isNotFound());
   }

   @Test
   @DisplayName("Should return all ingredients available using the repository if exist")
   void getAllIngredients__OK() throws Exception {
      Mockito.when(ingredientService.getAllIngredients())
              .thenReturn(TestIngredientUtil.getIngredientList());

      var objectMapper = new ObjectMapper();

      mockMvc.perform(MockMvcRequestBuilders.get("/")
                      .contentType(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(TestIngredientUtil.getIngredientList())));
   }

   @Test
   @DisplayName("Should return one IngredientDomain using the repository if the id exist")
   void getNameById() {
      Mockito.when(ingredientService.getIdByIngredientName("not-found"))
              .thenReturn(Optional.empty());

      Mockito.when(ingredientService.getIdByIngredientName("Pepperoni"))
              .thenReturn(Optional.of(33));

      assertAll(
              () -> mockMvc.perform(MockMvcRequestBuilders.get("/name/not-found")
                              .contentType(MediaType.APPLICATION_JSON_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isNotFound()),

              () -> Mockito.verify(ingredientService, Mockito.times(1))
                      .getIdByIngredientName("not-found"),

              () -> mockMvc.perform(MockMvcRequestBuilders.get("/name/Pepperoni")
                              .contentType(MediaType.APPLICATION_JSON_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isOk())
                      .andExpect(MockMvcResultMatchers.content().string("33")),

              () -> Mockito.verify(ingredientService, Mockito.times(1))
                      .getIdByIngredientName("Pepperoni")
      );
   }

   @Test
   @DisplayName("Should return the desire id with the name using the service if exist")
   void getIdByIngredientName() {
      Mockito.when(ingredientService.getIdByIngredientName("not-found"))
              .thenReturn(Optional.empty());

      Mockito.when(ingredientService.getIdByIngredientName("Pepperoni"))
              .thenReturn(Optional.of(33));

      assertAll(
              () -> mockMvc.perform(MockMvcRequestBuilders.get("/name/not-found")
                              .contentType(MediaType.APPLICATION_JSON_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isNotFound()),

              () -> Mockito.verify(ingredientService, Mockito.times(1))
                      .getIdByIngredientName("not-found"),

              () -> mockMvc.perform(MockMvcRequestBuilders.get("/name/Pepperoni")
                              .contentType(MediaType.APPLICATION_JSON_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isOk())
                      .andExpect(MockMvcResultMatchers.content().string("33")),

              () -> Mockito.verify(ingredientService, Mockito.times(1))
                      .getIdByIngredientName("Pepperoni")
      );
   }

   @Test
   @DisplayName("Should return the desire name with the id using the service if exist")
   void getIngredientNameById() {
      Mockito.when(ingredientService.getIngredientNameById(45325))
              .thenReturn(Optional.empty());

      Mockito.when(ingredientService.getIngredientNameById(1))
              .thenReturn(Optional.of("Pepperoni"));

      assertAll(
              () -> mockMvc.perform(MockMvcRequestBuilders.get("/id/45325")
                              .contentType(MediaType.APPLICATION_JSON_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isNotFound()),

              () -> Mockito.verify(ingredientService, Mockito.times(1))
                      .getIngredientNameById(45325),

              () -> mockMvc.perform(MockMvcRequestBuilders.get("/id/1")
                              .contentType(MediaType.APPLICATION_JSON_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isOk())
                      .andExpect(MockMvcResultMatchers.content().string("Pepperoni")),

              () -> Mockito.verify(ingredientService, Mockito.times(1))
                      .getIngredientNameById(1)
      );
   }

   @Test
   @DisplayName("Should save one ingredientDto using the service or catch an error with a bad message")
   void saveIngredient() throws Exception {
      var ingredientDtoError = new IngredientDto(
              "Repeat name",
              IngredientType.VEGETABLE,
              "Author"
      );

      var ingredientDto = new IngredientDto(
              "Good",
              IngredientType.VEGETABLE,
              "Author"
      );

      Mockito.doThrow(new NotAllowedException("No repeat name")).when(ingredientService)
              .saveIngredient(Mockito.eq(ingredientDtoError), Mockito.isA(MockMultipartFile.class));

      Mockito.doNothing().when(ingredientService)
              .saveIngredient(Mockito.eq(ingredientDto), Mockito.isA(MockMultipartFile.class));

      assertAll(
              () -> mockMvc.perform(MockMvcRequestBuilders.multipart("/")
                              .file(TestIngredientUtil.getIngredientFile(ingredientDto))
                              .file(new MockMultipartFile("file", "image.jpg",
                                      MediaType.IMAGE_JPEG_VALUE, (byte[]) null))
                              .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isBadRequest())
                      .andExpect(MockMvcResultMatchers.content().string("Image is required")),

              () -> Mockito.verify(ingredientService, Mockito.times(0))
                      .saveIngredient(Mockito.eq(ingredientDto), Mockito.isA(MockMultipartFile.class)),

              () -> mockMvc.perform(MockMvcRequestBuilders.multipart("/")
                              .file(TestIngredientUtil.getIngredientFile(ingredientDto))
                              .file(new MockMultipartFile("file", "image.webp",
                                      "image/webp", Files.readAllBytes(Path.of("src/test/resources/test.webp"))
                              ))
                              .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isBadRequest())
                      .andExpect(MockMvcResultMatchers.content().string("Image type is not one of the following supported: jpeg, png, bmp, webmp, gif")),

              () -> Mockito.verify(ingredientService, Mockito.times(0))
                      .saveIngredient(Mockito.eq(ingredientDto), Mockito.isA(MockMultipartFile.class)),

              () -> mockMvc.perform(MockMvcRequestBuilders.multipart("/")
                              .file(TestIngredientUtil.getIngredientFile(ingredientDtoError))
                              .file(TestIngredientUtil.getImageFile())
                              .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isBadRequest())
                      .andExpect(MockMvcResultMatchers.content().string("{\"desc\":\"No repeat name\",\"fieldError\":null}")),

              () -> Mockito.verify(ingredientService, Mockito.times(1))
                      .saveIngredient(Mockito.eq(ingredientDtoError), Mockito.isA(MockMultipartFile.class)),

              () -> mockMvc.perform(MockMvcRequestBuilders.multipart("/")
                              .file(TestIngredientUtil.getIngredientFile(ingredientDto))
                              .file(TestIngredientUtil.getImageFile())
                              .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                      .andExpect(MockMvcResultMatchers.status().isOk())
                      .andExpect(MockMvcResultMatchers.content().string("Ingredient save correctly")),

              () -> Mockito.verify(ingredientService, Mockito.times(1))
                      .saveIngredient(Mockito.eq(ingredientDto), Mockito.isA(MockMultipartFile.class))
      );
   }
}