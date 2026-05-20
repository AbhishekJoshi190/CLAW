<%@ include file="components/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="auth-container">
    <h1 class="auth-title">Create Account</h1>

    <!-- ERROR HANDLING: Display error if user already exists or validation fails -->
    <c:if test="${not empty error}">
        <div class="error-msg">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <!-- USERNAME INPUT -->
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required placeholder="Choose a username">
        </div>

        <!-- EMAIL INPUT (Primary identifier) -->
        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" required placeholder="Enter your email">
        </div>

        <!-- PASSWORD INPUT (Hidden characters) -->
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required minlength="6"
                   placeholder="Create a password">
        </div>

        <button type="submit" class="btn auth-btn">Register</button>
    </form>

    <div class="auth-footer">
        Already have an account? <a href="${pageContext.request.contextPath}/login">Log in here</a>
    </div>
</div>

<%@ include file="components/footer.jsp" %>
