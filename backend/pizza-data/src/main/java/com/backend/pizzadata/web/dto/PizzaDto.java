package com.backend.pizzadata.web.dto;

import com.backend.pizzadata.constants.Size;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PizzaDto(
        @NotBlank String pizzaName,
        @NotNull Size size,
        @Min(1) @NotNull int quantity,
        @NotEmpty List<@Valid  IngredientNameDto> ingredientNameDtoList
) {}
