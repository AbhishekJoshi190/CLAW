<%@ include file="components/header.jsp" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <style>
            /* Interactive removal button for deleting items from cart */
            .btn-remove {
                padding: 0.5rem 1rem;
                font-size: 0.85rem;
                font-weight: 600;
                border: 1px solid #ff4757;
                color: #ff4757;
                background: transparent;
                border-radius: 6px;
                cursor: pointer;
                transition: all 0.3s ease;
            }

            .btn-remove:hover {
                background-color: #ff4757;
                color: white;
                box-shadow: 0 4px 12px rgba(255, 71, 87, 0.3);
                transform: translateY(-2px);
            }

            .btn-remove:active {
                transform: translateY(0);
            }
        </style>

        <div style="text-align: center; margin-bottom: 2rem;">
            <h1>Your Cart</h1>
        </div>

        <!-- CONDITIONAL DISPLAY: Check if cart has items -->
        <c:choose>
            <c:when test="${empty sessionScope.cart || empty sessionScope.cart.items}">
                <!-- CASE 1: CART IS EMPTY -->
                <div class="empty-cart" style="text-align: center; padding: 4rem 2rem;">
                    <div class="empty-cart" style="text-align: center; padding: 4rem 2rem;">
                        <h2>Cart is empty</h2>
                        <p style="margin: 1rem 0 2rem 0; color: var(--text-muted);">Looks like you haven't added
                            anything to your cart yet.</p>
                        <a href="${pageContext.request.contextPath}/shop" class="btn">Continue Shopping</a>
                    </div>
            </c:when>

            <c:otherwise>
                <div class="table-responsive">
                    <table class="cart-table">
                        <thead>
                            <tr style="border-bottom: 1px solid var(--border-color); text-align: left;">
                                <th style="padding: 1rem;">Product</th>
                                <th style="padding: 1rem;">Price</th>
                                <th style="padding: 1rem;">Quantity</th>
                                <th style="padding: 1rem;">Total</th>
                                <th style="padding: 1rem;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Loop through each item in the cart session -->
                            <c:forEach items="${sessionScope.cart.items}" var="item" varStatus="status">
                                <tr style="border-bottom: 1px solid var(--border-color);">
                                    <td style="padding: 1rem; display: flex; align-items: center; gap: 1rem;">
                                        <img src="${item.product.imageUrl}" alt="${item.product.name}"
                                            style="width: 80px; height: 80px; object-fit: cover; border-radius: 4px;">
                                        <div>
                                            <div style="font-weight: bold;">${item.product.name}</div>

                                            <!-- DISPLAY CUSTOMIZATIONS -->
                                            <div style="color: var(--text-muted); font-size: 0.9rem;">
                                                <c:if test="${not empty item.size}">Size: ${item.size}<br></c:if>
                                                <c:if test="${not empty item.hipsSize}">Hips Size: ${item.hipsSize}"<br>
                                                </c:if>
                                            </div>

                                            <!-- PREVIEW CUSTOM PRINTS (thumbnails) -->
                                            <div style="display: flex; gap: 0.5rem; margin-top: 0.5rem;">
                                                <c:if test="${not empty item.frontDesignUrl}">
                                                    <img src="${item.frontDesignUrl}"
                                                        style="width: 40px; height: 40px; object-fit: cover; border: 1px dashed #555; border-radius: 4px;"
                                                        title="Front Print">
                                                </c:if>
                                                <c:if test="${not empty item.backDesignUrl}">
                                                    <img src="${item.backDesignUrl}"
                                                        style="width: 40px; height: 40px; object-fit: cover; border: 1px dashed #555; border-radius: 4px;"
                                                        title="Back Print">
                                                </c:if>
                                            </div>
                                        </div>
                                    </td>
                                    <td style="padding: 1rem;">NPR ${item.product.price}</td>
                                    <td style="padding: 1rem;">${item.quantity}</td>
                                    <td style="padding: 1rem;">NPR ${item.totalPrice}</td>

                                    <!-- REMOVE BUTTON FORM -->
                                    <td style="padding: 1rem;">
                                        <form action="${pageContext.request.contextPath}/cart" method="post"
                                            style="margin: 0;">
                                            <input type="hidden" name="action" value="remove">
                                            <!-- Pass the index of the item to the servlet for removal -->
                                            <input type="hidden" name="index" value="${status.index}">
                                            <button type="submit" class="btn-remove">Remove</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="cart-total"
                    style="text-align: right; font-size: 1.5rem; font-weight: bold; margin-bottom: 2rem;">
                    Total: NPR ${sessionScope.cart.totalPrice}
                </div>

                <div style="text-align: right;">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <a href="${pageContext.request.contextPath}/checkout" class="btn"
                                style="text-decoration: none;">Proceed to Checkout</a>
                        </c:when>
                        <c:otherwise>
                            <a href="#" onclick="showLoginModal(event)" class="btn"
                                style="text-decoration: none;">Proceed to Checkout</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>

        </c:choose>

        <!-- Premium Login Requirement Modal -->
        <div id="loginModal" style="display:none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.8); z-index: 2000; align-items: center; justify-content: center; opacity: 0; transition: opacity 0.3s ease; backdrop-filter: blur(4px);">
            <div style="background-color: var(--card-bg); padding: 2.5rem; border-radius: 12px; max-width: 400px; width: 90%; text-align: center; border: 1px solid var(--border); box-shadow: 0 10px 40px rgba(0,0,0,0.6); transform: translateY(-20px); transition: transform 0.3s ease;" id="loginModalContent">
                <div style="font-size: 3.5rem; color: #ff4757; margin-bottom: 0.5rem; line-height: 1;">&#9888;</div>
                <h2 style="margin-bottom: 1rem; font-size: 1.6rem;">Authentication Required</h2>
                <p style="color: var(--text-muted); margin-bottom: 2rem; font-size: 0.95rem; line-height: 1.5;">You need to be logged in to your account to proceed to checkout and secure your gear.</p>
                <div style="display: flex; gap: 1rem; justify-content: center;">
                    <button onclick="closeLoginModal()" class="btn btn-outline" style="flex: 1;">Cancel</button>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn" style="flex: 1; text-decoration: none;">Login Now</a>
                </div>
            </div>
        </div>

        <script>
            function showLoginModal(e) {
                e.preventDefault();
                const modal = document.getElementById('loginModal');
                const content = document.getElementById('loginModalContent');
                modal.style.display = 'flex';
                // Trigger reflow for animation
                void modal.offsetWidth;
                modal.style.opacity = '1';
                content.style.transform = 'translateY(0)';
            }

            function closeLoginModal() {
                const modal = document.getElementById('loginModal');
                const content = document.getElementById('loginModalContent');
                modal.style.opacity = '0';
                content.style.transform = 'translateY(-20px)';
                setTimeout(() => {
                    modal.style.display = 'none';
                }, 300);
            }
        </script>

        <%@ include file="components/footer.jsp" %>