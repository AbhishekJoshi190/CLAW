# CLAW Streetwear: QA Test Case Master Specification

This document contains the official structured test cases for the CLAW Streetwear Web Application, optimized as high-density master matrix tables for each category.

---

## 🔐 1. Authentication & Session Management (AUTH)

| Test ID | Objective | Preconditions | Action (Test Steps) | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-AUTH-01** | Verify that a guest visitor can successfully register a new account when providing valid, unique credentials. | 1. User is anonymous.<br>2. Email is not already registered. | 1. Navigate to `/register.jsp`<br>2. Enter unique username, valid email, and secure password.<br>3. Click "Create Account". | 1. User row added to database.<br>2. Password saved hashed via BCrypt.<br>3. Redirected to `/login.jsp` with a success alert. | `[  ] PENDING` |
| **TC-AUTH-02** | Verify that registration is blocked if the email address provided is already registered in the system. | 1. An account with email `hype@claw.com` already exists. | 1. Navigate to `/register.jsp`<br>2. Enter username and password.<br>3. Enter email: `hype@claw.com`.<br>4. Click "Create Account". | 1. Registration is blocked.<br>2. No new database records created.<br>3. Displays warning: "Email already registered." | `[  ] PENDING` |
| **TC-AUTH-03** | Verify that registration fails when mandatory fields are left blank. | 1. User is on `/register.jsp`. | 1. Leave username, email, and password fields blank.<br>2. Click "Create Account". | 1. HTML5 validation blocks or Servlet rejects.<br>2. Displays error: "All fields are required." | `[  ] PENDING` |
| **TC-AUTH-04** | Verify that the system validates email formats and blocks invalid email strings. | 1. User is on `/register.jsp`. | 1. Fill in valid username and password.<br>2. Enter invalid email (e.g. `streetwearmail.com`).<br>3. Click "Create Account". | 1. System validates format error.<br>2. Submission blocked; requests valid email syntax. | `[  ] PENDING` |
| **TC-AUTH-05** | Verify that a registered user can successfully authenticate and log in with correct credentials. | 1. User exists with email `hype@claw.com` and password `Streetwear@2026`. | 1. Navigate to `/login.jsp`<br>2. Enter email: `hype@claw.com`.<br>3. Enter password: `Streetwear@2026`.<br>4. Click "Log In". | 1. BCrypt matches secure hash in DB.<br>2. Active session is created for user.<br>3. Redirected to home/shop catalog. | `[  ] PENDING` |
| **TC-AUTH-06** | Verify that the system denies access when a correct email is combined with an incorrect password. | 1. User exists with email `hype@claw.com`. | 1. Navigate to `/login.jsp`<br>2. Enter email: `hype@claw.com`.<br>3. Enter password: `WrongPassword123`.<br>4. Click "Log In". | 1. Authentication fails; session not created.<br>2. Displays error: "Invalid email or password." | `[  ] PENDING` |
| **TC-AUTH-07** | Verify that access is denied when trying to log in using an email that does not exist in the database. | 1. Email `phantom@ghost.com` is unregistered. | 1. Navigate to `/login.jsp`<br>2. Enter email: `phantom@ghost.com`<br>3. Enter password: `AnyPassword123`<br>4. Click "Log In". | 1. Authentication rejected.<br>2. Displays generic error: "Invalid email or password" (to protect email existence state). | `[  ] PENDING` |
| **TC-AUTH-08** | Verify that guest users are blocked from accessing protected pages and are redirected to the login screen. | 1. Visitor is anonymous. | 1. Attempt to access `/profile.jsp` or `/checkout.jsp` directly via address bar URL. | 1. Servlet interceptor blocks request.<br>2. User redirected to `/login.jsp` with authorization warning. | `[  ] PENDING` |
| **TC-AUTH-09** | Verify that a logged-in user can log out, thereby terminating their active session securely. | 1. User is logged in with active session. | 1. Click "Logout" button/link in the navigation header. | 1. Active session invalidated on server.<br>2. Cookies deleted from browser.<br>3. Redirected to `/login.jsp`. | `[  ] PENDING` |

---

## 👕 2. Shop Catalog & Browsing (SHOP)

| Test ID | Objective | Preconditions | Action (Test Steps) | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-SHOP-01** | Verify that the shop page displays all products stored in the database. | 1. Product table has active items. | 1. Navigate to `/shop.jsp` or `/shop` URL. | 1. Products successfully loaded from DB.<br>2. Product cards render image, price, title, and View Details link. | `[  ] PENDING` |
| **TC-SHOP-02** | Verify that basic keyword search filters products by exact title matches. | 1. Catalog has items containing "Hoodie" in the name. | 1. Navigate to `/shop`.<br>2. Enter "Hoodie" in search query.<br>3. Submit the search form. | 1. Displays only products containing the term "Hoodie".<br>2. Hides all non-matching products. | `[  ] PENDING` |
| **TC-SHOP-03** | Verify that product searching is case-insensitive (and queries are not automatically capitalized). | 1. Catalog contains item named "Essential Black Shirt". | 1. Navigate to `/shop`.<br>2. Search for "essential black".<br>3. Submit the search form. | 1. "Essential Black Shirt" is returned successfully.<br>2. Search ignores casing rules.<br>3. Search retains the raw search query input. | `[  ] PENDING` |
| **TC-SHOP-04** | Verify that sending an empty query or whitespace query resets the filter. | 1. User is on shop catalog. | 1. Leave the search field empty or enter spaces.<br>2. Click the Search button. | 1. System displays all products.<br>2. No filter is applied. | `[  ] PENDING` |
| **TC-SHOP-05** | Verify that searching for special characters or SQL patterns handles input safely. | 1. User is on shop catalog. | 1. Enter `' OR '1'='1` or `%` in the search field.<br>2. Submit the query. | 1. Input is safely sanitized/parameterized.<br>2. Database remains secure.<br>3. Displays a friendly empty state if no matches exist. | `[  ] PENDING` |
|
---

## 🛒 3. Shopping Cart Management (CART)

| Test ID | Objective | Preconditions | Action (Test Steps) | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-CART-01** | Verify that adding a standard product correctly inserts it into the user's shopping cart. | 1. User is on "CLAW Tee" detail page.<br>2. Cart is empty. | 1. Select size "M".<br>2. Click "Add to Cart". | 1. Cart session registers "CLAW Tee" (size: M, qty: 1).<br>2. Cart badge in navbar updates to `1`. | `[  ] PENDING` |
| **TC-CART-02** | Verify that adding the exact same product multiple times increments the quantity rather than creating duplicate rows. | 1. Cart already has 1 "CLAW Tee" (size: M). | 1. Go to same product details page.<br>2. Select size "M".<br>3. Click "Add to Cart" again. | 1. Single line item maintained in cart.<br>2. Quantity updates from `1` to `2`. | `[  ] PENDING` |
| **TC-CART-03** | Verify that users can add custom sized garments (such as specifying hips and length) as unique line items. | 1. User is on Cargo Pants detail page. | 1. Select "Custom Fit".<br>2. Enter "Hips: 36 in, Inseam: 31 in" in custom details text.<br>3. Click "Add to Cart". | 1. Custom fit item added to session cart.<br>2. Cart page highlights these measurements for the product. | `[  ] PENDING` |
| **TC-CART-04** | Verify that the cart accurately sums the total prices of mixed standard and custom items. | 1. Cart has 2x "CLAW Tee" (NPR 1,500 each) and 1x "Cargo Pants" (NPR 3,500). | 1. Navigate to `/cart.jsp`.<br>2. View total order cost. | 1. Math resolves: `(1500*2) + 3500`.<br>2. Total cost renders exactly as `NPR 6,500`. | `[  ] PENDING` |
| **TC-CART-05** | Verify that users can delete items from their cart, updating totals instantly. | 1. Cart has "CLAW Hoodie" (NPR 4,000) and "CLAW Tee" (NPR 1,500). | 1. Navigate to `/cart.jsp`<br>2. Click "Remove" button next to "CLAW Tee". | 1. Tee is evicted from session cart.<br>2. Total updates instantly to `NPR 4,000`. | `[  ] PENDING` |

---

## 📦 4. Checkout & Order Placement (CHK)

| Test ID | Objective | Preconditions | Action (Test Steps) | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-CHK-01** | Verify that users can transition to the checkout process when their cart contains products. | 1. User is logged in.<br>2. Cart contains active items. | 1. Navigate to `/cart.jsp`<br>2. Click "Proceed to Checkout" button. | 1. Access permitted.<br>2. Navigates to `/checkout.jsp` showcasing checkout form and summary. | `[  ] PENDING` |
| **TC-CHK-02** | Verify that checkout access is blocked if the cart contains no items. | 1. User is logged in.<br>2. Cart is empty. | 1. Attempt to navigate directly to `/checkout.jsp` via URL address bar. | 1. Redirected to `/cart.jsp` or `/shop.jsp`.<br>2. Displays alert: "Your cart must have items to checkout." | `[  ] PENDING` |
| **TC-CHK-03** | Verify that checkout cannot proceed with incomplete or empty shipping details. | 1. User is on `/checkout.jsp`. | 1. Leave Address and Phone empty.<br>2. Click "Place Order". | 1. Form validation triggers.<br>2. Order submission blocked; fields highlighted. | `[  ] PENDING` |
| **TC-CHK-04** | Verify that submitting complete details creates an order successfully in the database. | 1. Cart is not empty; on `/checkout.jsp`. | 1. Enter valid name, address, phone.<br>2. Select COD.<br>3. Click "Place Order". | 1. New row added in `orders` database table.<br>2. Order status set as `PENDING`.<br>3. Active cart is cleared; redirected to success view. | `[  ] PENDING` |

---

## 💳 5. Payments Processing (PAY)

| Test ID | Objective | Preconditions | Action (Test Steps) | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-PAY-01** | Verify that choosing Cash on Delivery initializes the order as PENDING until cash collection is completed. | 1. User placing order on checkout. | 1. Select "Cash on Delivery" method.<br>2. Submit order. | 1. Order stored in DB with status: `PENDING`.<br>2. Order success receipt generated. | `[  ] PENDING` |
| **TC-PAY-02** | Verify card/digital payment integration behaves correctly when a valid card is entered. | 1. User is on checkout payment page. | 1. Select "Card Payment".<br>2. Input test card: `4111222233334444`.<br>3. Click "Pay Now". | 1. Mock transaction succeeds.<br>2. Order saved in DB with payment confirmation metrics. | `[  ] PENDING` |

---

## 🏆 6. User Profile & Loyalty Rewards (PROF)

| Test ID | Objective | Preconditions | Action (Test Steps) | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-PROF-01** | Verify that completing orders increments a user's loyalty points in the database. | 1. User has `150` points. | 1. Log in; buy hoodie of `NPR 5,000`.<br>2. Complete checkout.<br>3. Visit profile page. | 1. Earns points ratio (e.g. 1 point / NPR 100 spent -> `+50` points).<br>2. Displayed points updates to `200`. | `[  ] PENDING` |
| **TC-PROF-02** | Verify that earning points elevates the user's tier category (Bronze/Silver/Gold). | 1. Tier Rules: Bronze (0-199), Silver (200-499), Gold (500+).<br>2. User has `190` points (Bronze). | 1. Earn 20 additional points via a purchase.<br>2. View `/profile.jsp` or `/rewards.jsp`. | 1. Points update to `210` in DB.<br>2. Loyalty tier dynamically updates and renders as `Silver`. | `[  ] PENDING` |

---

## 📊 7. Admin Dashboard & Reporting (ADM)

| Test ID | Objective | Preconditions | Action (Test Steps) | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-ADM-01** | Verify that users without administrator roles cannot access the administrative dashboard. | 1. Logged in as standard client (`isAdmin = false`). | 1. Enter `/admin` or `/admin.jsp` directly into browser URL address bar. | 1. Servlet filter halts execution.<br>2. Denies access; redirects to homepage with a "403 Forbidden" error. | `[  ] PENDING` |
| **TC-ADM-02** | Verify that financial totals differentiate between all orders (Gross) and completed/delivered orders (Realized). | 1. DB has 1x Pending order (NPR 3,000) and 1x Delivered order (NPR 5,000). | 1. Log in as Admin.<br>2. Navigate to admin dashboard. | 1. **Gross Sales** card: `NPR 8,000`.<br>2. **Realized Revenue** card: `NPR 5,000` (delivered only). | `[  ] PENDING` |
| **TC-ADM-03** | Verify that changing order status to DELIVERED recalculates financial metrics immediately. | 1. Order #15 value NPR 4,000 has status `PENDING`.<br>2. Dashboard realized revenue is NPR 10,000. | 1. In admin order management, locate #15.<br>2. Click "Mark as Delivered".<br>3. Check dashboard metrics. | 1. Order status changes to `DELIVERED` in DB.<br>2. Realized Revenue card updates to `NPR 14,000`. | `[  ] PENDING` |
| **TC-ADM-04** | Verify that simulated operating costs are calculated correctly based on delivered orders. | 1. Realized Revenue is `NPR 10,000`.<br>2. Delivered count is `2` orders. | 1. Open admin P&L card. | 1. Computations verify:<br>- **COGS**: NPR 5,000 (50%)<br>- **Logistics**: NPR 800 (8%)<br>- **Packaging**: NPR 300 (2 * 150)<br>- **Marketing**: NPR 1,200 (12%)<br>- **Fixed Hosting**: NPR 1,200<br>- **Total Expenses**: NPR 8,500<br>- **Net Profit**: NPR 1,500. | `[  ] PENDING` |
| **TC-ADM-05** | Verify that the Best Sellers algorithm successfully extracts products from text summaries and sorts them correctly. | 1. DB has delivered orders: "2x CLAW Tee" and "1x CLAW Hoodie, 1x CLAW Tee". | 1. Open Admin dashboard best sellers section. | 1. Summaries parsed.<br>2. **CLAW Tee** is #1 (3 units sold).<br>3. **CLAW Hoodie** is #2 (1 unit sold). | `[  ] PENDING` |
| **TC-ADM-06** | Verify that clicking the export button triggers a download of a correctly formatted CSV transaction report. | 1. Orders exist in database. | 1. Click "Export CSV Report" button. | 1. Servlet streams CSV data.<br>2. Browser triggers download of `orders_report.csv` containing all transactions. | `[  ] PENDING` |

---

## 🛡️ 8. Security & Data Integrity (SEC)

| Test ID | Objective | Preconditions | Action (Test Steps) | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-SEC-01** | Verify that passwords are never saved in plaintext in the database. | 1. User registered with password "Streetwear@2026". | 1. Log in to MySQL server terminal.<br>2. Query: `SELECT password FROM users WHERE email='hype@claw.com';` | 1. Scrambled BCrypt hash shown (starting with `$2a$`).<br>2. Plaintext password is not present. | `[  ] PENDING` |
| **TC-SEC-02** | Verify that session cookie identifiers cannot be read or stolen via JavaScript injection (protecting against XSS). | 1. User is authenticated. | 1. Open browser developer tools (F12).<br>2. Go to Console.<br>3. Run `console.log(document.cookie);`. | 1. JS console does not display `JSESSIONID` token cookie (protected by `HttpOnly` flag). | `[  ] PENDING` |
| **TC-SEC-03** | Verify that entering malicious SQL syntax in text fields does not compromise the database or bypass authentication. | 1. User is on the login page. | 1. Enter `' OR '1'='1` or `admin' --` in email/password fields.<br>2. Click "Log In". | 1. Inputs parameterized safely.<br>2. Authentication rejected; no DB/SQL syntax errors exposed. | `[  ] PENDING` |
