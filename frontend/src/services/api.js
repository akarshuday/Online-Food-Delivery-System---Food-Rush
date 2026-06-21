import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

// Attach JWT token to every request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle 401 globally — redirect to login
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// ── Auth ──────────────────────────────────────────────
export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
};

// ── Restaurants ───────────────────────────────────────
export const restaurantAPI = {
  getAll: () => api.get('/restaurants'),
  getById: (id) => api.get(`/restaurants/${id}`),
  create: (data) => api.post('/restaurants', data),
};

// ── Menu ──────────────────────────────────────────────
export const menuAPI = {
  getByRestaurant: (restaurantId) => api.get(`/restaurants/${restaurantId}/menu`),
  addItem: (restaurantId, data) => api.post(`/restaurants/${restaurantId}/menu`, data),
};

// ── Cart ──────────────────────────────────────────────
export const cartAPI = {
  get: () => api.get('/cart'),
  addItem: (data) => api.post('/cart/add', data),
  // Backend DELETE /cart/remove/{cartItemId} — uses the cart-item's own ID
  removeItem: (cartItemId) => api.delete(`/cart/remove/${cartItemId}`),
  updateItem: (cartItemId, quantity) => api.put(`/cart/update/${cartItemId}?quantity=${quantity}`),
  clear: () => api.delete('/cart/clear'),
};

// ── Orders ────────────────────────────────────────────
export const orderAPI = {
  place: (data) => api.post('/orders/place', data),
  // Backend GET /orders returns the authenticated user's orders
  getMyOrders: () => api.get('/orders'),
  updateStatus: (orderId, status) => api.put(`/orders/${orderId}/status?status=${status}`),
};

export default api;
