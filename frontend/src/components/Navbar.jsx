import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { ShoppingCart, UtensilsCrossed, LogOut, ClipboardList, Home } from 'lucide-react';
import './Navbar.css';

export default function Navbar() {
  const { isAuthenticated, user, logout } = useAuth();
  const { itemCount } = useCart();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <Link to="/home" className="navbar-brand">
        <UtensilsCrossed size={28} />
        <span>FoodRush</span>
      </Link>

      {isAuthenticated && (
        <div className="navbar-links">
          <Link to="/home" className="nav-link">
            <Home size={18} /> Home
          </Link>
          <Link to="/orders" className="nav-link">
            <ClipboardList size={18} /> Orders
          </Link>
          <Link to="/cart" className="nav-link cart-link">
            <ShoppingCart size={18} />
            {itemCount > 0 && <span className="cart-badge">{itemCount}</span>}
            Cart
          </Link>
          <span className="nav-user">Hi, {user?.name}</span>
          <button onClick={handleLogout} className="btn-logout">
            <LogOut size={16} /> Logout
          </button>
        </div>
      )}
    </nav>
  );
}
