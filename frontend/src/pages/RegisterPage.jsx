import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';
import { UtensilsCrossed, User, Mail, Phone, Lock, UserPlus } from 'lucide-react';
import './AuthPage.css';

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();
  // Backend RegisterRequest: name, email, password, phone
  const [form, setForm] = useState({ name: '', email: '', password: '', phone: '' });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await register(form);
      toast.success('Account created! Please log in.');
      navigate('/login');
    } catch (err) {
      toast.error(err?.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-header">
          <div className="auth-logo"><UtensilsCrossed size={32} /></div>
          <h1>Create Account</h1>
          <p>Join FoodRush and start ordering</p>
        </div>
        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label className="form-label">Full Name</label>
            <div className="input-icon-wrap">
              <User size={16} className="input-icon" />
              <input name="name" type="text" className="form-input icon-input" placeholder="John Doe"
                value={form.name} onChange={handleChange} required />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Email</label>
            <div className="input-icon-wrap">
              <Mail size={16} className="input-icon" />
              <input name="email" type="email" className="form-input icon-input" placeholder="you@example.com"
                value={form.email} onChange={handleChange} required />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Phone (optional)</label>
            <div className="input-icon-wrap">
              <Phone size={16} className="input-icon" />
              <input name="phone" type="tel" className="form-input icon-input" placeholder="+91 98765 43210"
                value={form.phone} onChange={handleChange} />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Password</label>
            <div className="input-icon-wrap">
              <Lock size={16} className="input-icon" />
              <input name="password" type="password" className="form-input icon-input" placeholder="Min. 6 characters"
                value={form.password} onChange={handleChange} required minLength={6} />
            </div>
          </div>
          <button type="submit" className="btn btn-primary btn-full" disabled={loading}>
            <UserPlus size={16} />
            {loading ? 'Creating account...' : 'Create Account'}
          </button>
        </form>
        <p className="auth-footer">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
