<!-- Include the global site header (navigation, logo, etc.) -->
<%@ include file="components/header.jsp" %>

    <!-- HERO SECTION: The big banner at the top of the homepage -->
    <section class="hero">
        <h1>Redefine Your Streets</h1>
        <p>Premium streetwear for the modern urbanite. No compromises.</p>
        <a href="${pageContext.request.contextPath}/shop" class="btn">Shop Now</a>
    </section>

    <!-- SECTION TITLE -->
    <div style="text-align: center; margin-bottom: 2rem;">
        <h2>Trending Categories</h2>
    </div>

    <!-- CATEGORY GRID: Quick links to filtered shop views -->
    <div class="product-grid">

        <!-- Category: T-Shirts -->
        <a href="${pageContext.request.contextPath}/shop?category=shirts" class="product-card">
            <div class="product-image">
                <img src="${pageContext.request.contextPath}/images/tee-black.jpg" alt="T-Shirts">
            </div>
            <div class="product-info">
                <div class="product-title">T-Shirts</div>
            </div>
        </a>

        <!-- Category: Bottoms & Cargos -->
        <a href="${pageContext.request.contextPath}/shop?category=pants" class="product-card">
            <div class="product-image">
                <img src="${pageContext.request.contextPath}/images/cargo-black.jpg" alt="Bottoms">
            </div>
            <div class="product-info">
                <div class="product-title">Bottoms & Cargos</div>
            </div>
        </a>

        <!-- Category: Outerwear -->
        <a href="${pageContext.request.contextPath}/shop?category=jackets" class="product-card">
            <div class="product-image">
                <img src="${pageContext.request.contextPath}/images/jacket-leather.jpg" alt="Jackets">
            </div>
            <div class="product-info">
                <div class="product-title">Outerwear</div>
            </div>
        </a>

    </div>

    <!-- Include the global site footer (copyright, social links, etc.) -->
    <%@ include file="components/footer.jsp" %>