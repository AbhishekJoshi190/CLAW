

<%@ include file="components/header.jsp" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <style>
            /* Layout for the checkout page (split between form and summary) */
            .checkout-container {
                max-width: 800px;
                margin: 4rem auto;
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 2rem;
            }

            @media (max-width: 768px) {
                .checkout-container {
                    grid-template-columns: 1fr;
                }
            }

            .checkout-card {
                background-color: var(--card-bg);
                padding: 2rem;
                border-radius: 12px;
                border: 1px solid var(--border);
            }

            .form-group {
                margin-bottom: 1.2rem;
            }

            .form-group label {
                display: block;
                margin-bottom: 0.5rem;
                font-weight: 600;
                color: var(--text-muted);
                font-size: 0.85rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .form-group input {
                width: 100%;
                padding: 0.8rem 1rem;
                background-color: rgba(255, 255, 255, 0.05);
                border: 1px solid var(--border);
                border-radius: 8px;
                color: var(--text);
                font-family: inherit;
                transition: 0.3s;
            }

            .form-group input:focus {
                outline: none;
                border-color: var(--accent);
                background-color: rgba(255, 255, 255, 0.1);
            }

            /* Payment method selection boxes */
            .payment-method {
                display: flex;
                align-items: center;
                gap: 1.5rem;
                padding: 1rem 1.5rem;
                border: 1px solid var(--border);
                border-radius: 12px;
                margin-bottom: 1rem;
                cursor: pointer;
                transition: 0.3s;
                background-color: rgba(255, 255, 255, 0.02);
            }

            .payment-method:hover {
                border-color: var(--accent);
                background-color: rgba(255, 255, 255, 0.05);
            }

            .payment-method img {
                height: 35px;
                width: 60px;
                object-fit: contain;
                border-radius: 4px;
            }
        </style>

        <div class="checkout-container">
            <!-- LEFT COLUMN: Shipping and Payment Form -->
            <div class="checkout-card">
                <h2 style="margin-bottom: 1.5rem;">Billing & Shipping</h2>
                <form action="${pageContext.request.contextPath}/checkout" method="post" id="checkoutForm">

                    <!-- ADDRESS FIELDS -->
                    <div class="form-group">
                        <label for="streetAddress">Street Address</label>
                        <input type="text" id="streetAddress" name="streetAddress" required
                            placeholder="House No, Street Name">
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label for="city">City / Location</label>
                            <input type="text" id="city" name="city" required placeholder="Kathmandu">
                        </div>
                        <div class="form-group">
                            <label for="postalCode">Postal Code</label>
                            <input type="text" id="postalCode" name="postalCode" required placeholder="44600">
                        </div>
                    </div>

                    <!-- CONTACT FIELDS -->
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label for="phone">Phone Number</label>
                            <input type="tel" id="phone" name="phone" required placeholder="98XXXXXXXX">
                        </div>
                        <div class="form-group">
                            <label for="email">Gmail / Email</label>
                            <!-- Defaults to the user's registered email -->
                            <input type="email" id="email" name="email" required value="${sessionScope.user.email}"
                                placeholder="example@gmail.com">
                        </div>
                    </div>

                    <h3 style="margin: 2rem 0 1rem 0;">Payment Method</h3>

                    <!-- PAYMENT METHOD SELECTION (with local image and online fallback) -->
                    <label class="payment-method">
                        <input type="radio" name="paymentMethod" value="esewa" required>
                        <img src="images/esewa.png" alt="eSewa"
                            onerror="this.src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_H_iZJ9iZ8q-P5M_ZfC9e_o-h-f1_YI2lSg&s'">
                        <span style="font-weight:bold;">eSewa</span>
                    </label>

                    <label class="payment-method">
                        <input type="radio" name="paymentMethod" value="khalti" required>
                        <img src="images/khalti.png" alt="Khalti"
                            onerror="this.src='https://web.khalti.com/static/img/logo1.png'">
                        <span style="font-weight:bold;">Khalti</span>
                    </label>

                    <label class="payment-method">
                        <input type="radio" name="paymentMethod" value="card" required>
                        <img src="images/card.png" alt="Card"
                            onerror="this.src='https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Mastercard-logo.svg/1200px-Mastercard-logo.svg.png'">
                        <span style="font-weight:bold;">Debit / Credit Card</span>
                    </label>

                    <label class="payment-method">
                        <input type="radio" name="paymentMethod" value="COD" required>
                        <img src="images/payment_cod.png" alt="COD"
                            onerror="this.src='https://cdn-icons-png.flaticon.com/512/2331/2331941.png'">
                        <span style="font-weight:bold;">Cash on Delivery (COD)</span>
                    </label>

                    <button type="submit" class="btn" style="width: 100%; margin-top: 1.5rem;">Place Order</button>
                </form>
            </div>

            <!-- RIGHT COLUMN: Detailed Order Summary -->
            <div class="checkout-card">
                <h2 style="margin-bottom: 1.5rem;">Order Summary</h2>

                <div style="border-bottom: 1px solid var(--border); padding-bottom: 1rem; margin-bottom: 1rem;">
                    <!-- List every item currently in the cart -->
                    <c:forEach items="${sessionScope.cart.items}" var="item">
                        <div
                            style="display: flex; justify-content: space-between; margin-bottom: 0.5rem; color: var(--text-muted);">
                            <span>
                                ${item.quantity}x ${item.product.name}
                                <!-- Show size/hips if applicable -->
                                <c:if test="${not empty item.size}"><span style="font-size:0.9rem;">(Size:
                                        ${item.size})</span></c:if>
                                <c:if test="${not empty item.hipsSize}"><span style="font-size:0.9rem;">(Hips:
                                        ${item.hipsSize}")</span></c:if>
                                <!-- Mark if customized -->
                                <c:if test="${not empty item.frontDesignUrl or not empty item.backDesignUrl}">
                                    <span
                                        style="color: var(--accent); font-size: 0.8rem; font-weight: bold; margin-left: 0.5rem;">[CUSTOM
                                        PRINT]</span>
                                </c:if>
                            </span>
                            <span>NPR ${item.totalPrice}</span>
                        </div>
                    </c:forEach>
                </div>

                <!-- Grand Total -->
                <div
                    style="display: flex; justify-content: space-between; font-size: 1.2rem; font-weight: bold; margin-bottom: 1rem;">
                    <span>Total:</span>
                    <span style="color: var(--accent);">NPR ${sessionScope.cart.totalPrice}</span>
                </div>

                <!-- REWARD POINTS INFO: Informs user that points are granted upon delivery -->
                <div
                    style="background-color: rgba(46, 213, 115, 0.1); border: 1px solid rgba(46, 213, 115, 0.3); padding: 1rem; border-radius: 8px; color: #2ed573; text-align: center;">
                    <p style="font-weight: bold;">&#9733; Reward Points Pending</p>
                    <small>Points will be credited to your account once your order is confirmed as delivered!</small>
                </div>
            </div>
        </div>

        <%@ include file="components/footer.jsp" %>
