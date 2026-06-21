package com.foodapp.service;

import com.foodapp.dto.MenuItemDto;
import com.foodapp.dto.RestaurantDto;
import com.foodapp.entity.MenuItem;
import com.foodapp.entity.Restaurant;
import com.foodapp.repository.MenuItemRepository;
import com.foodapp.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findByIsActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<RestaurantDto> searchRestaurants(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RestaurantDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + id));
        return convertToDto(restaurant);
    }

    @Transactional
    public RestaurantDto createRestaurant(RestaurantDto dto) {
        Restaurant restaurant = Restaurant.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .cuisineType(dto.getCuisineType())
                .imageUrl(dto.getImageUrl())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
        Restaurant saved = restaurantRepository.save(restaurant);
        return convertToDto(saved);
    }

    @Transactional
    public RestaurantDto updateRestaurant(Long id, RestaurantDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + id));

        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setAddress(dto.getAddress());
        restaurant.setCuisineType(dto.getCuisineType());
        restaurant.setImageUrl(dto.getImageUrl());
        if (dto.getIsActive() != null) {
            restaurant.setIsActive(dto.getIsActive());
        }

        Restaurant updated = restaurantRepository.save(restaurant);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant not found with id: " + id);
        }
        restaurantRepository.deleteById(id);
    }

    public List<MenuItemDto> getMenuByRestaurantId(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurant not found with id: " + restaurantId);
        }
        return menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId).stream()
                .map(this::convertMenuItemToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuItemDto addMenuItem(Long restaurantId, MenuItemDto dto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + restaurantId));

        MenuItem menuItem = MenuItem.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .category(dto.getCategory())
                .isAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true)
                .restaurant(restaurant)
                .build();

        MenuItem saved = menuItemRepository.save(menuItem);
        return convertMenuItemToDto(saved);
    }

    private RestaurantDto convertToDto(Restaurant restaurant) {
        List<MenuItemDto> menuItemDtos = null;
        if (restaurant.getMenuItems() != null) {
            menuItemDtos = restaurant.getMenuItems().stream()
                    .map(this::convertMenuItemToDto)
                    .collect(Collectors.toList());
        }
        return RestaurantDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .cuisineType(restaurant.getCuisineType())
                .imageUrl(restaurant.getImageUrl())
                .isActive(restaurant.getIsActive())
                .menuItems(menuItemDtos)
                .build();
    }

    private MenuItemDto convertMenuItemToDto(MenuItem menuItem) {
        return MenuItemDto.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .imageUrl(menuItem.getImageUrl())
                .category(menuItem.getCategory())
                .isAvailable(menuItem.getIsAvailable())
                .restaurantId(menuItem.getRestaurant() != null ? menuItem.getRestaurant().getId() : null)
                .build();
    }
}
