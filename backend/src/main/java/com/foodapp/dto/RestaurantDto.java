package com.foodapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String cuisineType;
    private String imageUrl;
    private Boolean isActive;
    private List<MenuItemDto> menuItems;
}
