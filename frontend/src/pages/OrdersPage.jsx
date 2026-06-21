import { useEffect, useState } from 'react';
import { orderAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';
import { ClipboardList, Package, Bike } from 'lucide-react';
import './OrdersPage.css';

const STATUS_FLOW = ['PENDING', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED'];

function badgeClass(status) {
  return `badge badge-${status?.toLowerCase().replace('_for_', '_')}`;
}

export default function OrdersPage() {
  const { isAdmin } = useAuth();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [timers, setTimers] = useState({});

  const fetchOrders = () => {
    orderAPI.getMyOrders()
      .then(r => setOrders(r.data.sort((a, b) => b.id - a.id)))
      .catch(() => toast.error('Failed to load orders'))
      .finally(() => setLoading(false));
  };

  useEffect(() => { 
    fetchOrders(); 
  }, []);

  useEffect(() => {
    // Auto-advance orders with random timers
    const intervals = orders.map(order => {
      if (order.status === 'DELIVERED' || order.status === 'CANCELLED') return null;
      
      const currentIdx = STATUS_FLOW.indexOf(order.status);
      if (currentIdx >= STATUS_FLOW.length - 1) return null;

      const timerKey = order.id;
      if (timers[timerKey]) return null;

      const randomTime = Math.floor(Math.random() * (15000 - 5000 + 1)) + 5000; // 5-15 seconds

      const interval = setInterval(() => {
        const nextIdx = currentIdx + 1;
        if (nextIdx < STATUS_FLOW.length) {
          const nextStatus = STATUS_FLOW[nextIdx];
          handleStatusChange(order.id, nextStatus, false);
          clearInterval(interval);
        }
      }, randomTime);

      setTimers(prev => ({ ...prev, [timerKey]: interval }));
      return interval;
    }).filter(Boolean);

    return () => intervals.forEach(interval => clearInterval(interval));
  }, [orders]);

  const handleStatusChange = async (orderId, status, showToast = true) => {
    try {
      await orderAPI.updateStatus(orderId, status);
      setOrders(prev => prev.map(o => o.id === orderId ? { ...o, status } : o));
      if (showToast) toast.success('Status updated');
    } catch {
      if (showToast) toast.error('Failed to update status');
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    return new Date(dateStr).toLocaleString('en-US', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
  };

  if (loading) return <div className="spinner-wrap"><div className="spinner" /></div>;

  return (
    <div className="container">
      <h1 className="page-title">My Orders</h1>
      <p className="page-sub">{orders.length} order{orders.length !== 1 ? 's' : ''} placed</p>

      {orders.length === 0 ? (
        <div className="empty-state">
          <ClipboardList size={56} />
          <h3>No orders yet</h3>
          <p>Place your first order from our restaurants!</p>
        </div>
      ) : (
        <div className="orders-list">
          {orders.map(order => (
            <div key={order.id} className="glass-card order-card">
              <div className="order-header">
                <div>
                  <span className="order-id">Order #{order.id}</span>
                  <span className="order-date">{formatDate(order.createdAt)}</span>
                </div>
                <span className={badgeClass(order.status)}>{order.status?.replace(/_/g, ' ')}</span>
              </div>

              {/* Status stepper */}
              <div className="status-stepper">
                {STATUS_FLOW.map((s, i) => {
                  const currentIdx = STATUS_FLOW.indexOf(order.status);
                  const done = i <= currentIdx;
                  const active = i === currentIdx;
                  return (
                    <div key={s} className={`step ${done ? 'done' : ''} ${active ? 'active' : ''}`}>
                      <div className="step-dot">
                        {s === 'OUT_FOR_DELIVERY' && active && <Bike size={14} className="delivery-icon" />}
                      </div>
                      {i < STATUS_FLOW.length - 1 && <div className="step-line" />}
                      <span className="step-label">{s.replace(/_/g, ' ')}</span>
                    </div>
                  );
                })}
              </div>

              {/* Items */}
              <div className="order-items">
                {(order.items || order.orderItems || []).map((item, idx) => (
                  <div key={idx} className="order-item-row">
                    <span><Package size={13} /> {item.menuItemName || item.name} × {item.quantity}</span>
                    <span>${(item.price * item.quantity).toFixed(2)}</span>
                  </div>
                ))}
              </div>

              <div className="order-footer">
                <div>
                  <span className="order-address-label">Delivery: </span>
                  <span className="order-address">{order.deliveryAddress}</span>
                </div>
                <span className="order-total">Total: ${Number(order.totalAmount).toFixed(2)}</span>
              </div>

              {/* Admin status controls */}
              {isAdmin && order.status !== 'DELIVERED' && order.status !== 'CANCELLED' && (
                <div className="admin-actions">
                  <span className="admin-tag">Update:</span>
                  {STATUS_FLOW.filter(s => STATUS_FLOW.indexOf(s) > STATUS_FLOW.indexOf(order.status)).map(s => (
                    <button key={s} className="btn btn-outline status-btn"
                      onClick={() => handleStatusChange(order.id, s)}>
                      {s.replace(/_/g, ' ')}
                    </button>
                  ))}
                  <button className="btn btn-danger status-btn" onClick={() => handleStatusChange(order.id, 'CANCELLED')}>
                    Cancel
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
