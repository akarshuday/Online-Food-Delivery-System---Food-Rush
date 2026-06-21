package com.foodapp.controller;

import com.foodapp.dto.MenuItemDto;
import com.foodapp.dto.RestaurantDto;
import com.foodapp.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantDto>> searchRestaurants(@RequestParam String name) {
        return ResponseEntity.ok(restaurantService.searchRestaurants(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItemDto>> getMenuByRestaurantId(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getMenuByRestaurantId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantDto> createRestaurant(@RequestBody RestaurantDto dto) {
        return ResponseEntity.ok(restaurantService.createRestaurant(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDto dto) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/menu")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemDto> addMenuItem(@PathVariable Long id, @RequestBody MenuItemDto dto) {
        return ResponseEntity.ok(restaurantService.addMenuItem(id, dto));
    }
}
