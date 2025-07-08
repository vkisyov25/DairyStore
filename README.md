![image](https://github.com/user-attachments/assets/276dbefd-85e8-4bc4-900a-94d7fc47c85e)

# 🧀 DairyStore – Online Marketplace for Dairy Products

A modern web platform that connects dairy producers with customers, ensuring transparency, direct sales, and secure payments.

## Tech Stack

**Backend:**
- Java 17 + Spring Boot
- Hibernate + PostgreSQL
- Stripe API, JWT, JavaMailSender
- Selenium (automatic verification of company data through the Commercial Register)

**Frontend:**
- HTML5, CSS3, Vanilla JavaScript
- Fetch API for server communication

## ✅ Key Features

- Role-based user registration with email confirmation  
- Stripe integration for secure payments  
- Dynamic shopping cart and order system  
- Secure access via JWT authentication  
- Automated company data verification using Selenium

## User roles

- **Admin** – manage users, manage product, add delivery companies
- **Seller** – add products, edit products, receive payments
- **Buyer** – browse products, add to shopping cart, make order, pay securely

## ER diagram

![image](https://github.com/user-attachments/assets/b040c364-6d2c-479e-9b80-b1f141463cbe)

### Relationships:

- `users` (1) ↔ (1) `carts`  
  → Each user has one cart

- `users` (1) ↔ (M) `products`  
  → A seller can create multiple products

- `users` (1) ↔ (M) `orders`  
  → A buyer can place multiple orders

- `carts` (1) ↔ (M) `cart_items`  
  → A cart can contain multiple products

- `products` (1) ↔ (M) `cart_items`  
  → A product can be present in multiple cart items

- `orders` (1) ↔ (M) `order_items`  
  → An order includes multiple items

- `delivery_companies` (1) ↔ (M) `orders`  
  → A delivery company can handle many orders


## Project Structure

src/main/java/com/dairystore/
├── Configurations/ # JWT, Security, Stripe configuration
├── Controllers/ # REST controllers (Product, Order, Cart, User...)
│ └── Analyses/ # Controllers for buyer/seller analytics
├── Models/
│ ├── dtos/ # Data Transfer Objects (CreateUserDto, OrderDto, etc.)
│ └── enums/ # Enum types (User roles, Product types...)
├── Repository/ # Spring Data JPA interfaces for DB access
├── Security/ # Custom user details and JWT auth logic
├── Services/
│ ├── analyses/ # Buyer/Seller strategy pattern logic
│ └── ... # Service & Impl classes for each domain
├── Utils/ # Utility classes (e.g. JwtUtil)
├── Validations/ # Custom validators (EIK, seller fields)
└── DairyStoreApplication # Main Spring Boot entry point

src/main/resources/
├── static/
│ ├── css/ # Stylesheets for pages
│ ├── html/ # Public-facing HTML templates
│ ├── js/ # Frontend logic using Vanilla JS
│ └── images/ # UI images and backgrounds
├── templates/ # Server-side rendered HTML templates
└── application.properties # Spring Boot configuration

The backend follows a layered architecture with clear separation of concerns:
- Controllers → Services → Repositories → DB
- Business logic is encapsulated in services (with Impl classes)
- DTOs and Validators handle data transfer and input correctness
- Strategy Pattern is used for analytics logic (buyers/sellers)

The frontend is structured using static HTML, JS, and CSS, organized per user role.


## Video
https://youtu.be/Ffe5uwPVsUI
