import { useCart } from '../context/CartContext';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { Trash2, ShoppingBag, ShoppingCart, Minus, Plus } from 'lucide-react';
import { cartAPI } from '../services/api';
import { useState } from 'react';
import './CartPage.css';

export default function CartPage() {
  const { cart, loading, removeFromCart, fetchCart, itemCount, totalPrice } = useCart();
  const navigate = useNavigate();
  const [removingId, setRemovingId] = useState(null);
  const [updatingId, setUpdatingId] = useState(null);

  const handleRemove = async (cartItemId, name) => {
    setRemovingId(cartItemId);
    try {
      await removeFromCart(cartItemId);
      toast.success(`${name} removed`);
    } catch {
      toast.error('Failed to remove item');
    } finally {
      setRemovingId(cartItemId);
    }
  };

  const handleQtyChange = async (cartItemId, newQty) => {
    if (newQty < 1) return;
    setUpdatingId(cartItemId);
    try {
      await cartAPI.updateItem(cartItemId, newQty);
      await fetchCart();
    } catch {
      toast.error('Failed to update quantity');
    } finally {
      setUpdatingId(null);
    }
  };

  if (loading) return <div className="spinner-wrap"><div className="spinner" /></div>;

  const items = cart?.items || [];

  return (
    <div className="container">
      <h1 className="page-title">Your Cart</h1>
      <p className="page-sub">{itemCount} item{itemCount !== 1 ? 's' : ''} in your cart</p>

      {items.length === 0 ? (
        <div className="empty-state">
          <ShoppingCart size={56} />
          <h3>Your cart is empty</h3>
          <p>Add some delicious items from a restaurant</p>
          <button className="btn btn-primary" onClick={() => navigate('/home')}>
            <ShoppingBag size={16} /> Browse Restaurants
          </button>
        </div>
      ) : (
        <div className="cart-layout">
          <div className="cart-items">
            {items.map(item => (
              <div key={item.id} className="glass-card cart-item">
                <div className="cart-item-details">
                  <h4>{item.menuItemName || item.name}</h4>
                  <span className="cart-item-price">${Number(item.price).toFixed(2)} each</span>
                </div>
                <div className="cart-item-actions">
                  <div className="qty-control">
                    <button className="qty-btn" onClick={() => handleQtyChange(item.id, item.quantity - 1)}
                      disabled={updatingId === item.id || item.quantity <= 1}>
                      <Minus size={14} />
                    </button>
                    <span className="qty-value">{item.quantity}</span>
                    <button className="qty-btn" onClick={() => handleQtyChange(item.id, item.quantity + 1)}
                      disabled={updatingId === item.id}>
                      <Plus size={14} />
                    </button>
                  </div>
                  <span className="cart-item-subtotal">${(item.price * item.quantity).toFixed(2)}</span>
                  <button className="btn btn-danger icon-btn" onClick={() => handleRemove(item.id, item.menuItemName)}
                    disabled={removingId === item.id}>
                    <Trash2 size={15} />
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="glass-card cart-summary">
            <h3>Order Summary</h3>
            <div className="summary-line">
              <span>Subtotal ({itemCount} items)</span>
              <span>${totalPrice.toFixed(2)}</span>
            </div>
            <div className="summary-line">
              <span>Delivery fee</span>
              <span className="text-success">Free</span>
            </div>
            <div className="summary-divider" />
            <div className="summary-line total">
              <span>Total</span>
              <span>${totalPrice.toFixed(2)}</span>
            </div>
            <button className="btn btn-primary btn-full" onClick={() => navigate('/checkout')}>
              Proceed to Checkout
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
