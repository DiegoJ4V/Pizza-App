package com.backend.pizza.domain;

import com.backend.pizza.domain.service.CustomerService;
import com.backend.pizza.exceptions.NotAllowedException;
import com.backend.pizza.persistence.entity.CustomerEntity;
import com.backend.pizza.persistence.repository.CustomerRepository;
import com.backend.pizza.web.dto.CustomerDto;
import com.backend.pizza.web.dto.NecessaryValuesForChangeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

   private final CustomerRepository customerRepository;

   @Autowired
   public CustomerServiceImpl(CustomerRepository customerRepository) {
      this.customerRepository = customerRepository;
   }

   @Override
   public void saveCustomer(CustomerDto customerDto) throws NotAllowedException {
      if (customerDto.email().equals("norepeat@name.com")) throw new NotAllowedException("Email already used");
      else if (!customerDto.birthDate().plusYears(18).isBefore(LocalDate.now()))
         throw new NotAllowedException("No older enough");

      var customer = CustomerEntity.builder()
              .customerName(customerDto.customerName())
              .email(customerDto.email())
              .password(customerDto.password())
              .birthDate(customerDto.birthDate())
              .build();

      customerRepository.save(customer);
   }

   @Override
   public Optional<CustomerEntity> getCustomerById(long id) {
      return customerRepository.findById(id);
   }

   @Override
   public Map.Entry<Integer, String> changeName(String newName, NecessaryValuesForChangeDto forChangeDto) {
      var result = validateNecessaryValues(forChangeDto, "Change name correctly");

      if (result.getKey().equals(200)) customerRepository.changeName(newName, forChangeDto.id());

      return result;
   }

   @Override
   public Map.Entry<Integer, String> changePassword(String newPassword, NecessaryValuesForChangeDto forChangeDto){
      var result = validateNecessaryValues(forChangeDto, "Change password correctly");

      if (result.getKey().equals(200)) customerRepository.changePassword(newPassword, forChangeDto.id());

      return result;
   }

   @Override
   public Map.Entry<Integer, String> changeEmail(String newEmail, NecessaryValuesForChangeDto forChangeDto) {
      var result = validateNecessaryValues(forChangeDto, "Change email correctly");

      if (result.getKey().equals(200)) customerRepository.changeEmail(newEmail, forChangeDto.id());

      return result;
   }

   private Map.Entry<Integer, String> validateNecessaryValues(NecessaryValuesForChangeDto valuesForChangeDto, String desireMessage) {
      if (customerRepository.existsById(valuesForChangeDto.id())) return new AbstractMap.SimpleEntry<>(404, "Id doesn't exist");

      Optional<CustomerEntity> customerEntity = customerRepository.findById(valuesForChangeDto.id());
      if (!valuesForChangeDto.password().equals(customerEntity.get().getPassword())) {
         return new AbstractMap.SimpleEntry<>(400, "Incorrect password");
      }

      return new AbstractMap.SimpleEntry<>(200, desireMessage);
   }

}
