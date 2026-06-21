package com.foodapp.service;

import com.foodapp.dto.CartDto;
import com.foodapp.dto.CartItemDto;
import com.foodapp.dto.CartItemRequest;
import com.foodapp.entity.Cart;
import com.foodapp.entity.CartItem;
import com.foodapp.entity.MenuItem;
import com.foodapp.entity.User;
import com.foodapp.repository.CartItemRepository;
import com.foodapp.repository.CartRepository;
import com.foodapp.repository.MenuItemRepository;
import com.foodapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    public CartDto getCartDtoByUserId(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return convertToDto(cart);
    }

    @Transactional
    public CartDto addItemToCart(Long userId, CartItemRequest request) {
        Cart cart = getOrCreateCart(userId);
        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + request.getMenuItemId()));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenuItem().getId().equals(request.getMenuItemId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItem.setPrice(menuItem.getPrice());
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(request.getQuantity())
                    .price(menuItem.getPrice())
                    .build();
            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem);
        }

        recalculateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }

    @Transactional
    public CartDto updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

        Cart cart = cartItem.getCart();

        if (quantity <= 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        recalculateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }

    @Transactional
    public CartDto removeItemFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

        Cart cart = cartItem.getCart();
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        recalculateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }

    @Transactional
    public CartDto clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotalAmount(0.0);
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
                    Cart newCart = Cart.builder()
                            .user(user)
                            .totalAmount(0.0)
                            .cartItems(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    private void recalculateCartTotal(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        cart.setTotalAmount(total);
    }

    private CartDto convertToDto(Cart cart) {
        List<CartItemDto> itemDtos = cart.getCartItems().stream()
                .map(item -> CartItemDto.builder()
                        .id(item.getId())
                        .menuItemId(item.getMenuItem().getId())
                        .menuItemName(item.getMenuItem().getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .imageUrl(item.getMenuItem().getImageUrl())
                        .build())
                .collect(Collectors.toList());

        return CartDto.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemDtos)
                .totalAmount(cart.getTotalAmount())
                .build();
    }
}
