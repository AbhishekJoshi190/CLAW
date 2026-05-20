<%@ include file="components/header.jsp" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <div style="max-width: 1000px; margin: 4rem auto; padding: 0 2rem;">
            <div style="text-align: center; margin-bottom: 3rem;">
                <h1 style="font-size: 2.5rem; text-transform: uppercase; letter-spacing: 2px;">Rewards Center</h1>
                <p style="color: var(--text-muted); font-size: 1.1rem; margin-top: 1rem;">Spend your hard-earned points
                    on exclusive gear and discounts.</p>
                <div class="points-badge"
                    style="display: inline-block; background-color: var(--accent); color: white; padding: 0.5rem 1rem; border-radius: 20px; font-weight: bold; margin-top: 1rem; font-size: 1.2rem;">
                    Your Balance: &#9733; ${sessionScope.user.points} Points
                </div>
            </div>

            <c:if test="${not empty success}">
                <div class="success-msg" style="max-width: 600px; margin: 0 auto 2rem auto;">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="error-msg" style="max-width: 600px; margin: 0 auto 2rem auto;">${error}</div>
            </c:if>

            <div class="product-grid" style="grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));">

                <!-- Reward 1 -->
                <div class="product-card" style="padding: 1.5rem; text-align: center;">
                    <img src="${pageContext.request.contextPath}/images/reward_sticker.png" alt="Sticker Pack"
                        style="width: 100%; height: 200px; object-fit: cover; border-radius: 8px; margin-bottom: 1rem; border: 1px solid var(--border);">
                    <h3 style="margin-bottom: 0.5rem;">CLAW Sticker Pack</h3>
                    <div style="color: var(--accent); font-weight: bold; margin-bottom: 1.5rem;">&#9733; 50 Points</div>
                    <form action="${pageContext.request.contextPath}/rewards" method="post">
                        <input type="hidden" name="rewardName" value="CLAW Sticker Pack">
                        <input type="hidden" name="cost" value="50">
                        <button type="submit" class="btn btn-outline" style="width: 100%;" ${sessionScope.user.points <
                            50 ? 'disabled' : '' }>Redeem</button>
                    </form>
                </div>

                <!-- Reward 2 -->
                <div class="product-card" style="padding: 1.5rem; text-align: center;">
                    <img src="${pageContext.request.contextPath}/images/reward_pen.png" alt="Signature Pen"
                        style="width: 100%; height: 200px; object-fit: cover; border-radius: 8px; margin-bottom: 1rem; border: 1px solid var(--border);">
                    <h3 style="margin-bottom: 0.5rem;">CLAW Signature Pen</h3>
                    <div style="color: var(--accent); font-weight: bold; margin-bottom: 1.5rem;">&#9733; 100 Points
                    </div>
                    <form action="${pageContext.request.contextPath}/rewards" method="post">
                        <input type="hidden" name="rewardName" value="CLAW Signature Pen">
                        <input type="hidden" name="cost" value="100">
                        <button type="submit" class="btn btn-outline" style="width: 100%;" ${sessionScope.user.points <
                            100 ? 'disabled' : '' }>Redeem</button>
                    </form>
                </div>

                <!-- Reward 3 -->
                <div class="product-card" style="padding: 1.5rem; text-align: center;">
                    <img src="${pageContext.request.contextPath}/images/reward_cap.png" alt="Classic Cap"
                        style="width: 100%; height: 200px; object-fit: cover; border-radius: 8px; margin-bottom: 1rem; border: 1px solid var(--border);">
                    <h3 style="margin-bottom: 0.5rem;">Classic Logo Cap</h3>
                    <div style="color: var(--accent); font-weight: bold; margin-bottom: 1.5rem;">&#9733; 300 Points
                    </div>
                    <form action="${pageContext.request.contextPath}/rewards" method="post">
                        <input type="hidden" name="rewardName" value="Classic Logo Cap">
                        <input type="hidden" name="cost" value="300">
                        <button type="submit" class="btn btn-outline" style="width: 100%;" ${sessionScope.user.points <
                            300 ? 'disabled' : '' }>Redeem</button>
                    </form>
                </div>

                <!-- Reward 4 -->
                <div class="product-card" style="padding: 1.5rem; text-align: center;">
                    <img src="${pageContext.request.contextPath}/images/reward_discount.png" alt="Discount Code"
                        style="width: 100%; height: 200px; object-fit: cover; border-radius: 8px; margin-bottom: 1rem; border: 1px solid var(--border);">
                    <h3 style="margin-bottom: 0.5rem;">20% Discount Code</h3>
                    <div style="color: var(--accent); font-weight: bold; margin-bottom: 1.5rem;">&#9733; 500 Points
                    </div>
                    <form action="${pageContext.request.contextPath}/rewards" method="post">
                        <input type="hidden" name="rewardName" value="20% Discount Code">
                        <input type="hidden" name="cost" value="500">
                        <button type="submit" class="btn btn-outline" style="width: 100%;" ${sessionScope.user.points <
                            500 ? 'disabled' : '' }>Redeem</button>
                    </form>
                </div>

            </div>
        </div>

        <%@ include file="components/footer.jsp" %>