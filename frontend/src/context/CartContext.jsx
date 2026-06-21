import { createContext, useContext, useState, useCallback, useEffect } from 'react';
import { cartAPI } from '../services/api';
import { useAuth } from './AuthContext';

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const { isAuthenticated } = useAuth();
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchCart = useCallback(async () => {
    if (!isAuthenticated) { setCart(null); return; }
    setLoading(true);
    try {
      const res = await cartAPI.get();
      setCart(res.data);
    } catch {
      setCart(null);
    } finally {
      setLoading(false);
    }
  }, [isAuthenticated]);

  useEffect(() => { fetchCart(); }, [fetchCart]);

  // addItem: { menuItemId, quantity }
  const addToCart = useCallback(async (menuItemId, quantity = 1) => {
    await cartAPI.addItem({ menuItemId, quantity });
    await fetchCart();
  }, [fetchCart]);

  // removeFromCart: uses the CartItem's own id (item.id in CartItemDto)
  const removeFromCart = useCallback(async (cartItemId) => {
    await cartAPI.removeItem(cartItemId);
    await fetchCart();
  }, [fetchCart]);

  // updateQuantity: uses CartItem's own id
  const updateQuantity = useCallback(async (cartItemId, quantity) => {
    await cartAPI.updateItem(cartItemId, quantity);
    await fetchCart();
  }, [fetchCart]);

  const clearCart = useCallback(async () => {
    await cartAPI.clear();
    await fetchCart();
  }, [fetchCart]);

  const items = cart?.items || [];
  const itemCount = items.reduce((sum, i) => sum + i.quantity, 0);
  const totalPrice = cart?.totalAmount ?? items.reduce((sum, i) => sum + (i.price * i.quantity), 0);

  return (
    <CartContext.Provider value={{
      cart, loading, fetchCart,
      addToCart, removeFromCart, updateQuantity, clearCart,
      itemCount, totalPrice,
    }}>
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCart must be used inside CartProvider');
  return ctx;
}
