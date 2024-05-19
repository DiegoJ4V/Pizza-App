package com.backend.pizzadata.service;

import com.backend.pizzadata.TestDataUtil;
import com.backend.pizzadata.constants.Size;
import com.backend.pizzadata.domain.service.OrderService;
import com.backend.pizzadata.exceptions.NotAllowedException;
import com.backend.pizzadata.persistence.entity.OrderEntity;
import com.backend.pizzadata.persistence.entity.PizzaEntity;
import com.backend.pizzadata.setup.containers.SetUpForServiceWithContainers;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableFeignClients
@ImportAutoConfiguration(FeignAutoConfiguration.class)
class OrderServiceImplTest extends SetUpForServiceWithContainers {

   @Autowired
   private OrderService orderService;

   static WireMockServer mockService = new WireMockServer(8765);

   @BeforeAll
   public static void setupMockCustomerResponse() {
      mockService.start();

      mockService.stubFor(WireMock.head(WireMock.urlPathMatching("/customer/exist/2"))
              .willReturn(WireMock.status(200)));
   }

   @Test
   @DisplayName("Should return the desire order with its specific id using the repository")
   void getOrderById() {
      var orderId = orderService.getOrdersByCustomerId(4234L, 0).get().getContent().getFirst().getIdOrder();
      var order = orderService.getOrderById(orderId).get();
      var emptyOrder = orderService.getOrderById(UUID.fromString("abdd83f3-61ea-4428-991d-f80b5479736e"));

      assertAll(
              () -> assertEquals(TestDataUtil.getOrderList().getContent().getFirst().toString(), order.toString()),
              () -> assertTrue(emptyOrder.isEmpty())
      );
   }

   @Test
   @DisplayName("Should return all orders with the a specific customer id using the repository")
   void getOrdersByCustomerId() {
      var listOrder = orderService.getOrdersByCustomerId(4234L, 0).get();
      var listEmpty = orderService.getOrdersByCustomerId(86579L, 0).get();

      assertAll(
              () -> assertThat(TestDataUtil.getOrderList().stream().map(OrderEntity::toString).toList())
                      .hasSameElementsAs(listOrder.stream().map(OrderEntity::toString).toList()),
              () -> assertTrue(listEmpty.isEmpty())
      );
   }

   @Test
   @DisplayName("Should convert one orderDto to orderEntity, and send it to the repository")
   void saveOrder() throws NotAllowedException {
      assertEquals(0, orderService.getOrdersByCustomerId(2L, 0).get().getTotalElements());
      orderService.saveOrder(TestDataUtil.getOrderDto(), TestDataUtil.getCookie());

      var pageOrderSaved = orderService.getOrdersByCustomerId(2L, 0).get();
      var order = pageOrderSaved.get().toList().getFirst();

      var orderString = List.of(OrderEntity.builder()
              .idCustomer(2L)
              .country("México")
              .state("State")
              .city("City")
              .street("Street")
              .houseNumber(42342)
              .total(460)
              .build()).toString();

      var pizzaString = List.of(
              PizzaEntity.builder()
                      .pizzaName("Pepperoni")
                      .price(140)
                      .size(Size.MEDIUM)
                      .quantity(1)
                      .build(),
              PizzaEntity.builder()
                      .pizzaName("Hawaiana")
                      .price(320)
                      .size(Size.MEDIUM)
                      .quantity(2)
                      .build()
      ).toString();

      assertAll(
              () -> assertEquals(1, pageOrderSaved.getTotalElements()),
              () -> assertEquals(orderString, Collections.singletonList(order).toString()),
              () -> assertEquals(pizzaString, order.getPizzaList().toString()),
              () -> assertEquals(5, order.getPizzaList().stream().map(PizzaEntity::getPizzaIngredients).flatMap(Collection::stream).toList().size())
      );
   }
}