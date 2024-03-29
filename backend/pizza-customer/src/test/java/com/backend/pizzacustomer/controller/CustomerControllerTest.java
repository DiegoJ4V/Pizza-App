package com.backend.pizzacustomer.controller;

import com.backend.pizzacustomer.TestDataUtil;
import com.backend.pizzacustomer.domain.service.CustomerService;
import com.backend.pizzacustomer.web.CustomerController;
import com.backend.pizzacustomer.web.dto.CustomerDto;
import com.backend.pizzacustomer.web.dto.NecessaryValuesForChangeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @MockBean
   private CustomerService customerService;

   @Test
   @DisplayName("Should register a customer correctly")
   void registerCustomer() {
      var passwordErrorCustomer = new CustomerDto(
              "Name",
              "norepeat@name.com",
              "1234",
              "43252543",
              LocalDate.of(2004, 2, 2)
      );

      var successCustomer = new CustomerDto(
              "Name",
              "norepeat@name.com",
              "1234",
              "1234",
              LocalDate.of(2004, 2, 2)
      );

      var objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      assertAll(
              () -> mockMvc.perform(MockMvcRequestBuilders.post("/register")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(objectMapper.writeValueAsString(passwordErrorCustomer)))
                      .andExpect(MockMvcResultMatchers.status().isBadRequest())
                      .andExpect(MockMvcResultMatchers.content().string("Passwords don't match")),

              () -> Mockito.verify(customerService, Mockito.times(0))
                      .saveCustomer(Mockito.eq(successCustomer)),

              () -> mockMvc.perform(MockMvcRequestBuilders.post("/register")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(objectMapper.writeValueAsString(successCustomer)))
                      .andExpect(MockMvcResultMatchers.status().isCreated())
                      .andExpect(MockMvcResultMatchers.content().string("Account create successfully")),

              () -> Mockito.verify(customerService, Mockito.times(1))
                      .saveCustomer(Mockito.eq(successCustomer))

      );
   }

   @Test
   @DisplayName("Should return one customer in json format with a specific id using the service or return a not found")
   void getCustomerById() {
      Mockito.when(customerService.getCustomerById(3213))
              .thenReturn(Optional.of(TestDataUtil.getCustomer()));

      var objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      assertAll(
              () -> mockMvc.perform(MockMvcRequestBuilders.get("/54345216")
                              .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(MockMvcResultMatchers.status().isNotFound()),

              () -> mockMvc.perform(MockMvcRequestBuilders.get("/3213")
                              .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(MockMvcResultMatchers.status().isOk())
                      .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(TestDataUtil.getCustomer())))
      );
   }
}