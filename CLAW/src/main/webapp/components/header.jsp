<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>CLAW | Streetwear</title>
            <!-- Reference the global stylesheet -->
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
        </head>

        <body>

            <nav class="navbar">
                <!-- LOGO: Clicking returns to homepage -->
                <a href="${pageContext.request.contextPath}/" class="logo">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="CLAW Logo">
                    CLAW
                </a>

                <!-- [MOBILE ONLY] MENU TOGGLE (Hamburger) -->
                <!-- Only visible on screens < 768px via style.css -->
                <button class="mobile-menu-btn" id="mobileMenuBtn" aria-label="Toggle Menu">
                    <span></span>
                    <span></span>
                    <span></span>
                </button>

                <!-- --- ACTIVE LINK DETECTION --- -->
                <!-- We extract the current page path to determine which link should be highlighted red -->
                <c:set var="uri" value="${requestScope['javax.servlet.forward.servlet_path']}" />
                <c:if test="${empty uri}">
                    <c:set var="uri" value="${pageContext.request.servletPath}" />
                </c:if>

                <!-- Navigation links (Desktop: Horizontal | Mobile: Slide-out Sidebar) -->
                <div class="nav-links" id="navLinks">
                    <!-- Standard Navigation -->
                    <a href="${pageContext.request.contextPath}/"
                        class="${uri == '/' or uri == '/index.jsp' ? 'active' : ''}">Home</a>
                    <a href="${pageContext.request.contextPath}/shop" class="${uri == '/shop' ? 'active' : ''}">Shop</a>
                    <a href="${pageContext.request.contextPath}/about.jsp"
                        class="${uri == '/about.jsp' ? 'active' : ''}">About</a>

                    <!-- [LOGGED IN] ADMIN PANEL: Only visible to users with isAdmin = true -->
                    <c:if test="${not empty sessionScope.user and sessionScope.user.admin}">
                        <a href="${pageContext.request.contextPath}/admin" class="${uri == '/admin' ? 'active' : ''}"
                            style="color: #2ed573; font-weight: bold;">[Admin Panel]</a>
                    </c:if>

                    <!-- [LOGGED IN] PROFILE LINK: Shows current reward points -->
                    <c:if test="${not empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/profile"
                            class="${uri == '/profile' ? 'active' : ''}">
                            <span style="color: var(--accent); font-weight: bold;">&#9733;
                                ${sessionScope.user.points}</span>
                            Profile
                        </a>
                    </c:if>

                    <!-- CART LINK: Displays a red badge with the number of items -->
                    <a href="${pageContext.request.contextPath}/cart" class="${uri == '/cart' ? 'active' : ''}">
                        Cart

                        <c:if test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
                            <span class="cart-count">
                                <!-- Sum up all quantities in the cart -->
                                <c:set var="count" value="0" />
                                <c:forEach items="${sessionScope.cart.items}" var="item">
                                    <c:set var="count" value="${count + item.quantity}" />
                                </c:forEach>
                                ${count}
                            </span>
                        </c:if>
                    </a>

                    <!-- LOGOUT / LOGIN LINK -->
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
                        </c:when>
                        <c:otherwise>
                            <!-- [GUEST] Show Login link -->
                            <a href="${pageContext.request.contextPath}/login"
                                class="${uri == '/login' ? 'active' : ''}">Login</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </nav>

            <script>
                // [MOBILE ONLY] Menu Toggle Logic
                // This handles the sliding in/out of the sidebar on mobile devices
                const menuBtn = document.getElementById('mobileMenuBtn');
                const navLinks = document.getElementById('navLinks');

                menuBtn.addEventListener('click', () => {
                    navLinks.classList.toggle('show'); // Slide sidebar in/out
                    menuBtn.classList.toggle('active'); // Turn hamburger into X
                });
            </script>

            <!-- Main content starts here (closed in footer.jsp) -->
            <main>