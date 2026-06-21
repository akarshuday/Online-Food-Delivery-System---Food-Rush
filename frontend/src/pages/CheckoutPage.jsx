import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { orderAPI } from '../services/api';
import toast from 'react-hot-toast';
import { MapPin, CreditCard, CheckCircle } from 'lucide-react';
import './CheckoutPage.css';

export default function CheckoutPage() {
  const { cart, totalPrice, clearCart, itemCount } = useCart();
  const navigate = useNavigate();
  const [form, setForm] = useState({ deliveryAddress: '', paymentMethod: 'CASH_ON_DELIVERY' });
  const [placing, setPlacing] = useState(false);
  const [placed, setPlaced] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (itemCount === 0) { toast.error('Your cart is empty'); return; }
    setPlacing(true);
    try {
      await orderAPI.place(form);
      setPlaced(true);
      await clearCart();
    } catch (err) {
      toast.error(err?.response?.data?.message || 'Failed to place order');
    } finally {
      setPlacing(false);
    }
  };

  if (placed) {
    return (
      <div className="container">
        <div className="success-screen">
          <div className="success-icon"><CheckCircle size={56} /></div>
          <h1>Order Placed! 🎉</h1>
          <p>Your food is on its way. Track it in your orders.</p>
          <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', flexWrap: 'wrap' }}>
            <button className="btn btn-primary" onClick={() => navigate('/orders')}>View My Orders</button>
            <button className="btn btn-outline" onClick={() => navigate('/home')}>Order More</button>
          </div>
        </div>
      </div>
    );
  }

  const items = cart?.items || [];

  return (
    <div className="container">
      <h1 className="page-title">Checkout</h1>
      <p className="page-sub">Almost there! Confirm your order details.</p>

      <div className="checkout-layout">
        {/* Form */}
        <form onSubmit={handleSubmit} className="glass-card checkout-form">
          <h3><MapPin size={18} /> Delivery Details</h3>
          <div className="form-group" style={{ marginTop: '1rem' }}>
            <label className="form-label">Delivery Address *</label>
            <textarea
              className="form-input"
              rows={3}
              required
              placeholder="Enter your full delivery address..."
              value={form.deliveryAddress}
              onChange={e => setForm({ ...form, deliveryAddress: e.target.value })}
              style={{ resize: 'vertical' }}
            />
          </div>

          <h3 style={{ marginTop: '1.5rem' }}><CreditCard size={18} /> Payment Method</h3>
          <div className="payment-options">
            {[
              { value: 'CASH_ON_DELIVERY', label: 'Cash on Delivery', emoji: '💵' },
              { value: 'ONLINE', label: 'Online Payment', emoji: '💳' },
              { value: 'UPI', label: 'UPI', emoji: '📱' },
            ].map(opt => (
              <label key={opt.value} className={`payment-option ${form.paymentMethod === opt.value ? 'selected' : ''}`}>
                <input type="radio" name="paymentMethod" value={opt.value}
                  checked={form.paymentMethod === opt.value}
                  onChange={e => setForm({ ...form, paymentMethod: e.target.value })}
                />
                <span className="pay-emoji">{opt.emoji}</span>
                <span>{opt.label}</span>
              </label>
            ))}
          </div>

          <button type="submit" className="btn btn-primary btn-full" style={{ marginTop: '1.5rem' }} disabled={placing}>
            {placing ? 'Placing order...' : `Place Order · $${totalPrice.toFixed(2)}`}
          </button>
        </form>

        {/* Order summary */}
        <div className="glass-card checkout-summary">
          <h3>Order Summary</h3>
          <div className="summary-items">
            {items.map(item => (
              <div key={item.menuItemId} className="summary-item">
                <span>{item.menuItemName || item.name} × {item.quantity}</span>
                <span>${(item.price * item.quantity).toFixed(2)}</span>
              </div>
            ))}
          </div>
          <div className="summary-divider" />
          <div className="summary-line total">
            <span>Total</span>
            <span>${totalPrice.toFixed(2)}</span>
          </div>
        </div>
      </div>
    </div>
  );
}
