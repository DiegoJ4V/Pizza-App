package com.backend.pizzaorder.web.dto;

import com.backend.pizzaorder.constants.Size;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PizzaDto(
        @NotBlank String pizzaName,
        @NotBlank String pizzaImageName,
        @NotBlank String pizzaImageAuthor,
        @NotNull Size size,
        @Min(1) @NotNull int quantity,
        @NotEmpty List<@Valid  IngredientNameDto> pizzaIngredients
) {}
