import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { restaurantAPI, menuAPI } from '../services/api';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';
import { ArrowLeft, Plus, ShoppingCart, UtensilsCrossed } from 'lucide-react';
import './MenuPage.css';

export default function MenuPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { addToCart } = useCart();
  const { isAdmin } = useAuth();
  const [restaurant, setRestaurant] = useState(null);
  const [menuItems, setMenuItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [addingId, setAddingId] = useState(null);
  const [showAddMenu, setShowAddMenu] = useState(false);
  const [menuForm, setMenuForm] = useState({ name: '', description: '', price: '', category: '', imageUrl: '' });

  useEffect(() => {
    Promise.all([
      restaurantAPI.getById(id),
      menuAPI.getByRestaurant(id),
    ])
      .then(([rRes, mRes]) => {
        setRestaurant(rRes.data);
        setMenuItems(mRes.data);
      })
      .catch(() => toast.error('Failed to load menu'))
      .finally(() => setLoading(false));
  }, [id]);

  const handleAddToCart = async (item) => {
    setAddingId(item.id);
    try {
      await addToCart(item.id, 1);
      toast.success(`${item.name} added to cart!`);
    } catch {
      toast.error('Failed to add item');
    } finally {
      setAddingId(null);
    }
  };

  const handleAddMenuItem = async (e) => {
    e.preventDefault();
    try {
      const res = await menuAPI.addItem(id, { ...menuForm, price: parseFloat(menuForm.price) });
      setMenuItems(prev => [...prev, res.data]);
      setShowAddMenu(false);
      setMenuForm({ name: '', description: '', price: '', category: '', imageUrl: '' });
      toast.success('Menu item added!');
    } catch {
      toast.error('Failed to add item');
    }
  };

  // Group by category
  const grouped = menuItems.reduce((acc, item) => {
    const cat = item.category || 'Other';
    if (!acc[cat]) acc[cat] = [];
    acc[cat].push(item);
    return acc;
  }, {});

  if (loading) return <div className="spinner-wrap"><div className="spinner" /></div>;

  return (
    <div className="container">
      {/* Header */}
      <div className="menu-header">
        <button className="btn btn-outline back-btn" onClick={() => navigate('/home')}>
          <ArrowLeft size={18} /> Back
        </button>
        <div className="restaurant-header-info">
          <div className="restaurant-header-image">
            <img src={restaurant?.imageUrl || 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500&q=80'} alt={restaurant?.name} />
          </div>
          <div>
            <h1 className="page-title">{restaurant?.name}</h1>
            <p className="page-sub">{restaurant?.cuisineType} · {restaurant?.address}</p>
          </div>
        </div>
        {isAdmin && (
          <button className="btn btn-primary" onClick={() => setShowAddMenu(!showAddMenu)}>
            <Plus size={16} /> Add Item
          </button>
        )}
      </div>

      {/* Add menu item form */}
      {showAddMenu && (
        <div className="glass-card" style={{ marginBottom: '2.5rem' }}>
          <h3 style={{ marginBottom: '1.25rem', color: 'var(--text-primary)', fontWeight: 600 }}>New Menu Item</h3>
          <form onSubmit={handleAddMenuItem}>
            <div className="add-form-grid2">
              <div className="form-group">
                <label className="form-label">Name *</label>
                <input className="form-input" required value={menuForm.name}
                  onChange={e => setMenuForm({...menuForm, name: e.target.value})} placeholder="e.g. Margherita Pizza" />
              </div>
              <div className="form-group">
                <label className="form-label">Price ($) *</label>
                <input className="form-input" required type="number" min="0" step="0.01" value={menuForm.price}
                  onChange={e => setMenuForm({...menuForm, price: e.target.value})} placeholder="19.99" />
              </div>
              <div className="form-group">
                <label className="form-label">Category</label>
                <input className="form-input" value={menuForm.category}
                  onChange={e => setMenuForm({...menuForm, category: e.target.value})} placeholder="e.g. Starters" />
              </div>
              <div className="form-group">
                <label className="form-label">Image URL</label>
                <input className="form-input" value={menuForm.imageUrl}
                  onChange={e => setMenuForm({...menuForm, imageUrl: e.target.value})} placeholder="https://example.com/image.jpg" />
              </div>
              <div className="form-group" style={{gridColumn:'1/-1'}}>
                <label className="form-label">Description</label>
                <input className="form-input" value={menuForm.description}
                  onChange={e => setMenuForm({...menuForm, description: e.target.value})} placeholder="Short description" />
              </div>
            </div>
            <div style={{ display:'flex', gap:'0.75rem', justifyContent:'flex-end' }}>
              <button type="button" className="btn btn-outline" onClick={() => setShowAddMenu(false)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Add Item</button>
            </div>
          </form>
        </div>
      )}

      {/* Menu */}
      {menuItems.length === 0 ? (
        <div className="empty-state">
          <UtensilsCrossed size={56} />
          <h3>No items yet</h3>
          <p>{isAdmin ? 'Add some menu items above.' : 'The restaurant hasn\'t added items yet.'}</p>
        </div>
      ) : (
        Object.entries(grouped).map(([category, items]) => (
          <div key={category} className="menu-category">
            <h2 className="category-title">{category}</h2>
            <div className="grid-2">
              {items.map(item => (
                <div key={item.id} className="menu-item-card">
                  <div className="menu-item-image">
                    <img src={item.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80'} alt={item.name} />
                  </div>
                  <div className="menu-item-info">
                    <h4>{item.name}</h4>
                    {item.description && <p className="menu-desc">{item.description}</p>}
                    <span className="menu-price">${Number(item.price).toFixed(2)}</span>
                  </div>
                  <button
                    className="btn btn-primary add-cart-btn"
                    onClick={() => handleAddToCart(item)}
                    disabled={addingId === item.id}
                  >
                    {addingId === item.id ? 'Adding...' : <><Plus size={16} /><ShoppingCart size={16} /></>}
                  </button>
                </div>
              ))}
            </div>
          </div>
        ))
      )}
    </div>
  );
}
