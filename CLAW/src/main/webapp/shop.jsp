<%@ include file="components/header.jsp" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <div style="text-align: center; margin-bottom: 2rem;">
            <h1>Shop All Collections</h1>
        </div>

         <!-- SEARCH BAR CONTAINER: Rendered with premium glassmorphic styling -->
        <div style="max-width: 600px; margin: 0 auto 2.5rem auto; padding: 0 1.5rem;">
            <!-- GET Form sends query strings directly to /shop servlet context -->
            <form action="${pageContext.request.contextPath}/shop" method="get" style="display: flex; gap: 0.8rem; background: rgba(255, 255, 255, 0.03); padding: 0.4rem; border-radius: 50px; border: 1px solid rgba(255, 255, 255, 0.08); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37); backdrop-filter: blur(4px);">
                
                <!-- JSTL CHECK: Preserves current category filter context during search operations -->
                <c:if test="${not empty currentCategory and currentCategory != 'all'}">
                    <input type="hidden" name="category" value="${currentCategory}">
                </c:if>

                <!-- INPUT ELEMENT: Features 'text-transform: capitalize' for real-time visual capitalization of typed characters -->
                <input type="text" name="search" id="searchInput" placeholder="Search Premium Streetwear..." value="${searchQuery}" 
                       style="flex-grow: 1; padding: 0.8rem 1.5rem; border: none; background: transparent; color: #fff; font-size: 1rem; outline: none; font-family: inherit; text-transform: capitalize;">
                
                <button type="submit" class="btn" style="border-radius: 50px; padding: 0.8rem 2rem; font-weight: bold; border: none; cursor: pointer; transition: all 0.3s ease;">
                    Search
                </button>
            </form>

            <!-- JAVASCRIPT BLOCK: Capitalizes input programmatically so form submits in correct capitalization format -->
            <script>
                document.addEventListener("DOMContentLoaded", function() {
                    const searchInput = document.getElementById("searchInput");
                    if (searchInput) {
                        // Triggers dynamically on every keystroke
                        searchInput.addEventListener("input", function() {
                            // Maintain active cursor position to prevent shifting when formatting text
                            let cursor = this.selectionStart;
                            // Regex matches word boundaries and capitalizes letters
                            let capitalized = this.value.replace(/\b\w/g, c => c.toUpperCase());
                            
                            if (this.value !== capitalized) {
                                this.value = capitalized;
                                this.setSelectionRange(cursor, cursor);
                            }
                        });
                    }
                });
            </script>

            <!-- SEARCH FEEDBACK JSTL CONTAINER: Displays active query metadata and clear-search link -->
            <c:if test="${not empty searchQuery}">
                <div style="text-align: center; margin-top: 1rem; color: var(--text-muted); font-size: 0.95rem;">
                    Showing results for "<span style="color: var(--accent); font-weight: bold;">${searchQuery}</span>"
                    <a href="${pageContext.request.contextPath}/shop?category=${currentCategory}" style="margin-left: 0.5rem; color: #ff4757; text-decoration: none; font-size: 0.85rem; border-bottom: 1px dashed #ff4757;">[Clear Search]</a>
                </div>
            </c:if>
        </div>

        <!-- CATEGORY FILTERS: Allows users to filter the product list -->
        <div class="filters">
            <!-- These links reload the page with category and keep any active search filters -->
            <a href="?category=all${not empty searchQuery ? '&search='.concat(searchQuery) : ''}" class="btn ${currentCategory eq 'all' ? '' : 'btn-outline'}">All</a>
            <a href="?category=shirts${not empty searchQuery ? '&search='.concat(searchQuery) : ''}" class="btn ${currentCategory eq 'shirts' ? '' : 'btn-outline'}">Shirts</a>
            <a href="?category=pants${not empty searchQuery ? '&search='.concat(searchQuery) : ''}" class="btn ${currentCategory eq 'pants' ? '' : 'btn-outline'}">Bottoms</a>
            <a href="?category=hoodies${not empty searchQuery ? '&search='.concat(searchQuery) : ''}" class="btn ${currentCategory eq 'hoodies' ? '' : 'btn-outline'}">Hoodies</a>
            <a href="?category=jackets${not empty searchQuery ? '&search='.concat(searchQuery) : ''}" class="btn ${currentCategory eq 'jackets' ? '' : 'btn-outline'}">Jackets</a>
            <a href="?category=accessories${not empty searchQuery ? '&search='.concat(searchQuery) : ''}"
                class="btn ${currentCategory eq 'accessories' ? '' : 'btn-outline'}">Accessories</a>
        </div>

        <!-- PRODUCT GRID: Displays the results of the filter -->
        <div class="product-grid">

            <!-- Loop through each product passed from the ShopServlet -->
            <c:forEach items="${products}" var="product">
                <a href="${pageContext.request.contextPath}/product?id=${product.id}" class="product-card">
                    <div class="product-image">
                        <!-- Construct image path using context path + database image URL -->
                        <img src="${pageContext.request.contextPath}/${product.imageUrl}" alt="${product.name}">
                    </div>
                    <div class="product-info">
                        <div class="product-title">${product.name}</div>
                        <div class="product-price">NPR ${product.price}</div>
                    </div>
                </a>
            </c:forEach>

            <!-- EMPTY STATE: Show a structured, stylish message if no products match the filter -->
            <c:if test="${empty products || products.size() == 0}">
                <div style="text-align:center; grid-column: 1/-1; padding: 4rem 1rem; color: var(--text-muted);">
                    <c:choose>
                        <c:when test="${not empty searchQuery}">
                            <p style="font-size: 1.25rem; margin-bottom: 0.5rem; color: #fff; font-weight: bold;">No premium items match "${searchQuery}"</p>
                            <p style="font-size: 0.95rem; margin-bottom: 1.5rem;">Try adjusting your spelling or category filters to find what you want.</p>
                            <a href="${pageContext.request.contextPath}/shop?category=${currentCategory}" class="btn" style="border-radius: 30px; padding: 0.8rem 1.8rem; text-decoration: none; font-size: 0.9rem; font-weight: bold;">Clear Search Filter</a>
                        </c:when>
                        <c:otherwise>
                            <p style="font-size: 1.1rem; color: #fff;">No products found in this category.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>

        <%@ include file="components/footer.jsp" %>