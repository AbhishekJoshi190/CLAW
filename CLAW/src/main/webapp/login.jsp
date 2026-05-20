<%@ include file="components/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="auth-container">
    <h1 class="auth-title">Welcome Back</h1>

    <!-- ERROR HANDLING: Display error if login fails -->
    <c:if test="${not empty error}">
        <div class="error-msg">${error}</div>
    </c:if>

    <!-- SUCCESS MESSAGE: Display after a successful registration redirect -->
    <c:if test="${param.registered == 'true'}">
        <div class="success-msg">Registration successful! Please log in.</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <!-- EMAIL INPUT -->
        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" required placeholder="Enter your email">
        </div>

        <!-- PASSWORD INPUT -->
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required placeholder="Enter your password">
        </div>

        <button type="submit" class="btn auth-btn">Log In</button>
    </form>

    <div class="auth-footer">
        Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a>
    </div>
</div>

<%@ include file="components/footer.jsp" %>

