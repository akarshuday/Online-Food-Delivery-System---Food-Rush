import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { restaurantAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';
import { Store, MapPin, Plus, ChevronRight, Search, Utensils } from 'lucide-react';
import './HomePage.css';

export default function HomePage() {
  const { isAdmin } = useAuth();
  const navigate = useNavigate();
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [showAdd, setShowAdd] = useState(false);
  const [addForm, setAddForm] = useState({ name: '', address: '', cuisineType: '', imageUrl: '' });

  useEffect(() => {
    restaurantAPI.getAll()
      .then(r => setRestaurants(r.data))
      .catch(() => toast.error('Failed to load restaurants'))
      .finally(() => setLoading(false));
  }, []);

  const filtered = restaurants.filter(r =>
    r.name.toLowerCase().includes(search.toLowerCase()) ||
    (r.cuisineType || '').toLowerCase().includes(search.toLowerCase())
  );

  const handleAdd = async (e) => {
    e.preventDefault();
    try {
      const res = await restaurantAPI.create(addForm);
      setRestaurants(prev => [...prev, res.data]);
      setShowAdd(false);
      setAddForm({ name: '', address: '', cuisineType: '', imageUrl: '' });
      toast.success('Restaurant added!');
    } catch {
      toast.error('Failed to add restaurant');
    }
  };

  return (
    <div className="container">
      {/* Hero */}
      <div className="hero">
        <div className="hero-content">
          <div className="floating-foods">
            <span className="food-emoji" style={{ animationDelay: '0s' }}>🍕</span>
            <span className="food-emoji" style={{ animationDelay: '0.3s' }}>🍔</span>
            <span className="food-emoji" style={{ animationDelay: '0.6s' }}>🍣</span>
            <span className="food-emoji" style={{ animationDelay: '0.9s' }}>🍜</span>
            <span className="food-emoji" style={{ animationDelay: '1.2s' }}>🥗</span>
            <span className="food-emoji" style={{ animationDelay: '1.5s' }}>🍰</span>
          </div>
          <h1 className="hero-title">Delicious Food <br/>Delivered Fast</h1>
          <p className="hero-sub">Discover amazing restaurants near you and order your favorite meals</p>
        </div>
        <div className="search-bar">
          <Search size={22} className="search-icon" />
          <input type="text" placeholder="Search restaurants or cuisines..." value={search}
            onChange={e => setSearch(e.target.value)} className="search-input" />
        </div>
      </div>

      {/* Admin add button */}
      {isAdmin && (
        <div className="admin-bar">
          <span className="admin-tag">Admin</span>
          <button className="btn btn-primary" onClick={() => setShowAdd(!showAdd)}>
            <Plus size={18} /> Add Restaurant
          </button>
        </div>
      )}

      {/* Add Restaurant Form */}
      {showAdd && (
        <div className="glass-card add-form">
          <h3 style={{ marginBottom: '1.5rem', color: 'var(--text-primary)', fontWeight: 700, fontSize: '1.4rem' }}>New Restaurant</h3>
          <form onSubmit={handleAdd}>
            <div className="add-form-grid">
              <div className="form-group">
                <label className="form-label">Name *</label>
                <input className="form-input" required value={addForm.name}
                  onChange={e => setAddForm({...addForm, name: e.target.value})} placeholder="e.g. Pizza Palace" />
              </div>
              <div className="form-group">
                <label className="form-label">Cuisine Type</label>
                <input className="form-input" value={addForm.cuisineType}
                  onChange={e => setAddForm({...addForm, cuisineType: e.target.value})} placeholder="e.g. Italian" />
              </div>
              <div className="form-group" style={{gridColumn:'1/-1'}}>
                <label className="form-label">Address</label>
                <input className="form-input" value={addForm.address}
                  onChange={e => setAddForm({...addForm, address: e.target.value})} placeholder="123 Main St" />
              </div>
              <div className="form-group" style={{gridColumn:'1/-1'}}>
                <label className="form-label">Image URL</label>
                <input className="form-input" value={addForm.imageUrl}
                  onChange={e => setAddForm({...addForm, imageUrl: e.target.value})} placeholder="https://example.com/image.jpg" />
              </div>
            </div>
            <div style={{ display:'flex', gap:'0.75rem', justifyContent:'flex-end', marginTop:'0.5rem' }}>
              <button type="button" className="btn btn-outline" onClick={() => setShowAdd(false)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Add Restaurant</button>
            </div>
          </form>
        </div>
      )}

      {/* Restaurant Grid */}
      <h2 className="section-title">
        {search ? `Results for "${search}"` : 'Popular Restaurants'}
        <span className="count-pill">{filtered.length}</span>
      </h2>

      {loading ? (
        <div className="spinner-wrap"><div className="spinner" /></div>
      ) : filtered.length === 0 ? (
        <div className="empty-state">
          <Store size={64} />
          <h3>{search ? 'No restaurants found' : 'No restaurants yet'}</h3>
          <p>{isAdmin ? 'Add the first restaurant above!' : 'Check back soon'}</p>
        </div>
      ) : (
        <div className="grid-3">
          {filtered.map((r, index) => (
            <div 
              key={r.id} 
              className="restaurant-card" 
              onClick={() => navigate(`/restaurant/${r.id}/menu`)}
              style={{ animationDelay: `${index * 0.1}s` }}
            >
              <div className="restaurant-image">
                <img src={r.imageUrl || 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500&q=80'} alt={r.name} />
              </div>
              <div className="restaurant-info">
                <h3>{r.name}</h3>
                {r.cuisineType && <span className="cuisine-tag">{r.cuisineType}</span>}
                {r.address && (
                  <p className="restaurant-address">
                    <MapPin size={16} /> {r.address}
                  </p>
                )}
              </div>
              <div className="restaurant-arrow">
                <ChevronRight size={28} />
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
