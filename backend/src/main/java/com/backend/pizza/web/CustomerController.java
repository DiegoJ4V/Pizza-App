package com.backend.pizza.web;

import com.backend.pizza.domain.service.CustomerService;
import com.backend.pizza.exceptions.NotAllowedException;
import com.backend.pizza.persistence.entity.CustomerEntity;
import com.backend.pizza.web.dto.CustomerDto;
import com.backend.pizza.web.dto.NecessaryValuesForChangeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

   private final CustomerService customerService;

   @Autowired
   public CustomerController(CustomerService customerService) {
      this.customerService = customerService;
   }

   @PostMapping(value = "/register", consumes = {"application/json"})
   public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDto customerDto) {
      if (!customerDto.password().equals(customerDto.matchingPassword())) {
         return new ResponseEntity<>("Passwords don't match", HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>("Account create successfully", HttpStatus.CREATED);
   }

   @GetMapping("/{id}")
   public ResponseEntity<CustomerEntity> getCustomerById(@PathVariable long id) {
      return customerService.getCustomerById(id)
              .map((customer) -> new ResponseEntity<>(customer, HttpStatus.OK))
              .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   @PatchMapping(value = "/change-name/{name}", consumes = {"application/json"})
   public ResponseEntity<String> changeName(
           @NotBlank @PathVariable String name,
           @Valid @RequestBody NecessaryValuesForChangeDto forChangeDto
   ) throws NotAllowedException {
      customerService.updateName(name, forChangeDto);

      return new ResponseEntity<>("Change name successfully", HttpStatus.OK);
   }

   @PatchMapping(value = "/change-password/{password}", consumes = {"application/json"})
   public ResponseEntity<String> changePassword(
           @NotBlank @PathVariable String password,
           @Valid @RequestBody NecessaryValuesForChangeDto forChangeDto
   ) throws NotAllowedException {
      customerService.updatePassword(password, forChangeDto);

      return new ResponseEntity<>("Change password successfully", HttpStatus.OK);
   }

   @PatchMapping(value = "/change-email/{email}", consumes = {"application/json"})
   public ResponseEntity<String> changeEmail(
           @NotBlank @PathVariable String email,
           @Valid @RequestBody NecessaryValuesForChangeDto forChangeDto
   ) throws NotAllowedException {
      customerService.updateEmail(email, forChangeDto);

      return new ResponseEntity<>("Change email successfully", HttpStatus.OK);
   }
}
