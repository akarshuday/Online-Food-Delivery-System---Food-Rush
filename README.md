# FoodRush (Full Stack Food Delivery App)

## Overview

This repository contains a full-stack food delivery application with:
- `backend/`: Spring Boot + Spring Security + JPA + MySQL
- `frontend/`: React + Vite + React Router + Axios

The app provides user authentication, restaurant browsing, menu selection, cart management, checkout, and order tracking.

## Architecture

### Backend (`backend/`)

Implemented with Spring Boot and uses:
- `spring-boot-starter-web` for REST APIs
- `spring-boot-starter-data-jpa` for JPA/Hibernate persistence
- `spring-boot-starter-security` for JWT-based auth
- `jjwt` for JSON Web Token creation and validation
- `mysql-connector-j` to connect to MySQL

Key backend modules:
- `controller/`: REST API endpoints for auth, restaurants, cart, and orders
- `service/`: business logic for auth, restaurants, cart operations, and order placement
- `entity/`: domain model for `User`, `Restaurant`, `MenuItem`, `Cart`, `CartItem`, `Order`, `OrderItem`
- `config/`: security, CORS, JWT utilities, and database initialization
- `dto/`: request/response payloads used by the frontend

Important backend behavior:
- Creates a sample admin user and example restaurants/menu items on startup
- Uses JWT bearer tokens for authenticated requests
- Permits restaurant listing endpoints publicly and protects cart/order APIs
- Supports admin-only operations for creating restaurants, creating menu items, and updating order status

### Frontend (`frontend/`)

Implemented with React using Vite and includes:
- `react-router-dom` for routes
- `axios` for API calls
- `react-hot-toast` for notifications
- custom auth and cart context providers

Main UI sections:
- `LoginPage` / `RegisterPage`: authentication flows
- `HomePage`: restaurant discovery and admin restaurant creation
- `MenuPage`: restaurant menu viewing, adding to cart, admin menu management
- `CartPage`: cart review and quantity management
- `CheckoutPage`: order checkout with delivery address
- `OrdersPage`: order history and admin status updates

## Current runtime status

I verified the project by running both sides:
- Backend is running successfully on: `http://localhost:8080`
- Frontend is running successfully on: `http://127.0.0.1:5173`

## Project start guide

### Prerequisites

- Java 17+ (backend uses Spring Boot 3.5)
- Maven or rely on the included Maven wrapper `mvnw`
- Node.js & npm
- MySQL server available at `localhost:3306`

### Backend startup

1. Open a terminal in `backend/`
2. Build the backend:
   - Windows: `.\mvnw -DskipTests package`
   - macOS/Linux: `./mvnw -DskipTests package`
3. Run the backend:
   - Windows: `.\mvnw spring-boot:run`
   - macOS/Linux: `./mvnw spring-boot:run`

The backend uses `backend/src/main/resources/application.properties` and will automatically create the database `fooddeliverydb` if your MySQL server accepts the configured credentials.

### Frontend startup

1. Open a terminal in `frontend/`
2. Install dependencies:
   - `npm install`
3. Start the development server:
   - `npm run dev -- --host 127.0.0.1 --port 5173`

Then open:
- `http://127.0.0.1:5173`

### Recommended order

1. Start the backend first
2. Start the frontend next
3. Open the frontend URL in the browser
4. Log in or register to start ordering

## Default seeded admin account

The backend seeds an admin user at startup if not already present:
- Email: `admin@foodapp.com`
- Password: `admin123`

Admin users can:
- add restaurants from the home screen
- add menu items on the restaurant menu page
- update order statuses from the orders page

## Important backend configuration

File: `backend/src/main/resources/application.properties`

Key settings:
- MySQL URL: `jdbc:mysql://localhost:3306/fooddeliverydb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
- Username: `root`
- Password: `akarshuday@06`
- JWT secret and expiration are also configured here

If your MySQL credentials differ, update this file before starting the backend.

## API base paths

- Auth: `POST /api/auth/register`, `POST /api/auth/login`
- Restaurants: `GET /api/restaurants`, `GET /api/restaurants/{id}`, `GET /api/restaurants/{id}/menu`
- Cart: `GET /api/cart`, `POST /api/cart/add`, `PUT /api/cart/update/{cartItemId}`, `DELETE /api/cart/remove/{cartItemId}`, `DELETE /api/cart/clear`
- Orders: `POST /api/orders/place`, `GET /api/orders`, `PUT /api/orders/{id}/status`, `PUT /api/orders/{id}/cancel`

## Notes

- The frontend stores JWT in `localStorage` and attaches it as a Bearer token for authenticated requests.
- The backend currently allows public restaurant browsing; cart and order operations require login.
- The app is configured for local development, not production.

---

Enjoy exploring the FoodRush application!
