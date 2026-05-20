# Exhaustive Technical Report: CLAW Streetwear

## 1. Project Introduction
**CLAW Streetwear** is a comprehensive e-commerce platform built using the Java MVC (Model-View-Controller) pattern. It features a modern dark-themed UI, a persistent MySQL database, and advanced features like custom apparel design and a loyalty rewards system.

---

## 2. Directory & File Breakdown

### 📂 `src/main/java/com/claw/model` (Data Objects)
| File | Description | Key Logic/Functions |
| :--- | :--- | :--- |
| `User.java` | Represents a customer or administrator. | Manages credentials, profile picture URLs, and loyalty points. |
| `Product.java` | Core entity for shop items. | Stores price, category, and image paths. |
| `Order.java` | Represents a completed purchase. | Calculates `potentialPoints` based on item price and quantity. |
| `Cart.java` | Session-based container for items. | `addCustomItem()` ensures unique entries; `getTotalPrice()` aggregates costs. |
| `CartItem.java` | Link between Product and Cart. | Adds specific metadata like `size` and `frontDesignUrl`. |
| `MockDatabase.java` | Static data container. | Acts as a safety net if the database is disconnected. |

### 📂 `src/main/java/com/claw/dao` (Database Logic)
| File | Description | Key Queries |
| :--- | :--- | :--- |
| `UserDAO.java` | User data operations. | `findByEmail()`, `save()`, and `update()` for points/admin status. |
| `ProductDAO.java` | Product data operations. | `findAll()`, `findByCategory()`, and bulk `insertAll()` for migration. |
| `OrderDAO.java` | Order data operations. | `save()` uses subqueries to link users; `updateStatus()` for fulfillment. |

### 📂 `src/main/java/com/claw/controller` (Servlets)
| File | Purpose | Logic |
| :--- | :--- | :--- |
| `ShopServlet.java` | Product Gallery. | Filters database results based on URL `?category=` parameter. |
| `ProductServlet.java` | Item Details. | Fetches a single product by ID for the detail page. |
| `CartServlet.java` | Shopping Cart. | Handles item removal and **Multipart file uploads** for custom designs. |
| `LoginServlet.java` | Authentication. | Validates input against `UserDAO` and creates a session. |
| `RegisterServlet.java` | Signup. | Creates new DB records and initializes points to zero. |
| `AdminServlet.java` | Command Center. | Restricts access to admins; handles order delivery and role toggling. |
| `CheckoutServlet.java` | Order Processing. | Aggregates cart data into a single `Order` and routes to payment. |
| `PaymentServlet.java` | Simulation. | Mimics a payment gateway (eSewa/Card) before finalizing the order. |
| `ProfileServlet.java` | Dashboard. | Filters orders to show only those belonging to the logged-in user. |
| `LogoutServlet.java` | Security. | Clears the `HttpSession` and redirects to login. |

### 📂 `src/main/java/com/claw/util` (Infrastructure)
| File | Description | Key Feature |
| :--- | :--- | :--- |
| `DBUtil.java` | MySQL Connection. | Configures JDBC on `127.0.0.1:3306` with timeouts. |
| `DatabaseInitializer.java` | Auto-Migration. | Background thread that imports 25+ products into SQL on startup. |
| `DatabaseReset.java` | Emergency Tool. | Wipes and recreates the entire schema from `schema.sql`. |
| `PasswordUtils.java` | **Security**. | Implements **BCrypt hashing** and hash verification. |
| `SessionUtils.java` | Session Helper. | Centralizes `login()`, `logout()`, and `isAdmin()` checks. |
| `CookiesUtils.java` | Cookie Helper. | Simplifies cookie creation with `HttpOnly` security flags. |

---

### 📂 `src/main/webapp` (Frontend)
- **`index.jsp`**: Homepage featuring the "Redefine Your Streets" hero section.
- **`shop.jsp`**: Dynamic gallery using JSTL `<c:forEach>` to loop through SQL products.
- **`product.jsp`**: Interactive form allowing size selection and design file uploads.
- **`cart.jsp`**: Detailed list of items with price breakdown and "Remove" functionality.
- **`checkout.jsp`**: Multi-input form for shipping address and payment method.
- **`payment.jsp`**: Immersive dark-themed payment gateway simulation.
- **`profile.jsp`**: Displays order history and loyalty points balance.
- **`admin.jsp`**: Tabular view of all system orders and user permissions.
- **`rewards.jsp`**: Explanation of the points system and upcoming perks.

### 📂 `src/main/webapp/components` (Reusable UI)
- **`header.jsp`**: Contains the sticky navigation, logo, and conditional Login/Logout links.
- **`footer.jsp`**: Contains the brand copyright, social icons, and newsletter placeholder.

### 📂 `src/main/webapp/css`
- **`style.css`**: The master stylesheet. Includes CSS variables for colors (Dark Grey: `#121212`, Accent: `#e63946`), glassmorphism effects, and responsive mobile layouts.

---

## 3. Implementation History & Bug Fixes

| Phase | Milestone | Solution / Fix |
| :--- | :--- | :--- |
| **Phase 1** | Database Connectivity | Added `connectTimeout=3000` to `DBUtil` to prevent `SocketTimeoutException`. |
| **Phase 2** | Data Persistence | Integrated `DatabaseInitializer` background thread to ensure products exist in SQL. |
| **Phase 3** | File Management | Used `UUID.randomUUID()` in `CartServlet` to prevent design file overwrites. |
| **Phase 4** | **Security (Today)** | Integrated **jBCrypt** to hash all user passwords for data protection. |
| **Phase 5** | **Portability (Today)**| Added **Mock Fallback** in DAOs to allow access without MySQL installed. |
| **Phase 6** | **Documentation** | Added step-by-step numbering to all complex logic blocks for clarity. |

---

## 4. Deep Dive: Core System Logic

### 🔐 Security & Authentication Logic
| Feature | Implementation | Benefit |
| :--- | :--- | :--- |
| **Password Hashing** | `BCrypt.hashpw()` | Passwords are stored as unreadable hashes, impossible to reverse. |
| **Salt Generation** | `BCrypt.gensalt()` | Adds random data to hashes to block "Rainbow Table" attacks. |
| **Session Tracking** | `SessionUtils.login()` | Securely stores User objects in `HttpSession` across the site. |
| **Admin Protection**| `isAdmin()` Filter | Restricts dashboard access to users with the `is_admin` flag. |

### 🔄 Multi-Layered Fallback Architecture
| Layer | Name | Condition | Action |
| :--- | :--- | :--- | :--- |
| **Layer 1** | **SQL Mode** | MySQL is active. | Read/Write to the real `claw_db`. |
| **Layer 2** | **Detection** | `SQLException` caught. | Console prints: *"SQL failed. Falling back..."* |
| **Layer 3** | **Mock Mode** | MySQL is missing. | Read/Write to the in-memory `MockDatabase`. |

### 🛠️ Key Algorithms
| Logic | File | Formula / Process |
| :--- | :--- | :--- |
| **Points Calculation** | `CheckoutServlet` | `(5 * Quantity) + (Total Spend / 100)` |
| **Custom Uploads** | `CartServlet` | `UUID + originalName` stored in `images/` folder. |
| **Product Migration** | `DatabaseInitializer`| Background loop that checks if `products` table is empty. |

---

## 5. Educational Design & Standards
| Standard | Description |
| :--- | :--- |
| **Numbered Steps** | Critical code paths are marked (Step 1, Step 2, etc.) for easier reading. |
| **MVC Separation** | Clear division: Models (Data), DAOs (Storage), Controllers (Logic). |
| **Zero-Config** | The project runs out-of-the-box on any PC with no setup required. |

---

## 6. Full Folder Structure
```text
CLAW/
├── pom.xml (Maven Config)
├── src/main/java/com/claw/
│   ├── controller/ (10 Servlets)
│   ├── dao/ (3 SQL Access Objects)
│   ├── model/ (6 Data Models)
│   └── util/ (3 Utilities)
├── src/main/resources/
│   └── schema.sql (Database Structure)
└── src/main/webapp/
    ├── components/ (Header/Footer)
    ├── css/ (style.css)
    ├── images/ (Product & Custom Images)
    └── WEB-INF/ (Web Config)
```

*Report Last Updated: 2026-05-16*
