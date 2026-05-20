<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>CLAW | Secure Checkout</title>
            <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;800&display=swap" rel="stylesheet">
            <style>
                body {
                    font-family: 'Inter', sans-serif;
                    background-color: #121212;
                    color: #fff;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 100vh;
                    margin: 0;
                }

                .payment-box {
                    background-color: #1e1e1e;
                    padding: 2.5rem;
                    border-radius: 12px;
                    width: 100%;
                    max-width: 400px;
                    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
                    text-align: center;
                }

                .payment-box.esewa {
                    border-top: 4px solid #60B732;
                }

                .payment-box.khalti {
                    border-top: 4px solid #5C2D91;
                }

                .payment-box.card {
                    border-top: 4px solid #0070ba;
                }

                .logo-img {
                    max-width: 200px;
                    height: 60px;
                    object-fit: contain;
                    margin-bottom: 1.5rem;
                    border-radius: 8px;
                }

                .form-group {
                    margin-bottom: 1.5rem;
                    text-align: left;
                }

                .form-group label {
                    display: block;
                    margin-bottom: 0.5rem;
                    color: #aaa;
                    font-size: 0.9rem;
                }

                .form-group input {
                    width: 100%;
                    padding: 0.8rem;
                    background-color: rgba(0, 0, 0, 0.3);
                    border: 1px solid #333;
                    border-radius: 6px;
                    color: #fff;
                    font-size: 1rem;
                    box-sizing: border-box;
                }

                .form-group input:focus {
                    outline: none;
                    border-color: #fff;
                }

                .btn {
                    width: 100%;
                    padding: 1rem;
                    border: none;
                    border-radius: 6px;
                    font-weight: bold;
                    font-size: 1.1rem;
                    cursor: pointer;
                    color: white;
                    transition: 0.3s;
                }

                .btn-esewa {
                    background-color: #60B732;
                }

                .btn-esewa:hover {
                    background-color: #4A9C21;
                }

                .btn-khalti {
                    background-color: #5C2D91;
                }

                .btn-khalti:hover {
                    background-color: #481F78;
                }

                .btn-card {
                    background-color: #0070ba;
                }

                .btn-card:hover {
                    background-color: #005a96;
                }

                .cancel-link {
                    display: block;
                    margin-top: 1.5rem;
                    color: #888;
                    text-decoration: none;
                    font-size: 0.9rem;
                }

                .cancel-link:hover {
                    color: #ff4757;
                    text-decoration: underline;
                }
            </style>
        </head>

        <body>

            <div class="payment-box ${method}">
                <c:choose>
                    <c:when test="${method == 'esewa'}">
                        <img src="images/esewa.png" alt="eSewa" class="logo-img"
                            onerror="this.src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_H_iZJ9iZ8q-P5M_ZfC9e_o-h-f1_YI2lSg&s'">
                        <h3 style="margin-top: 0;">Pay NPR ${sessionScope.pendingOrder.totalAmount}</h3>
                        <form action="${pageContext.request.contextPath}/payment" method="post">
                            <div class="form-group">
                                <label>eSewa ID</label>
                                <input type="text" required placeholder="98XXXXXXXX">
                            </div>
                            <div class="form-group">
                                <label>MPIN</label>
                                <input type="password" required placeholder="••••">
                            </div>
                            <button type="submit" class="btn btn-esewa">Login & Pay</button>
                        </form>
                    </c:when>

                    <c:when test="${method == 'khalti'}">
                        <img src="images/khalti.png" alt="Khalti" class="logo-img"
                            onerror="this.src='https://web.khalti.com/static/img/logo1.png'">
                        <h3 style="margin-top: 0;">Pay NPR ${sessionScope.pendingOrder.totalAmount}</h3>
                        <form action="${pageContext.request.contextPath}/payment" method="post">
                            <div class="form-group">
                                <label>Khalti Mobile Number</label>
                                <input type="text" required placeholder="98XXXXXXXX">
                            </div>
                            <div class="form-group">
                                <label>Khalti PIN</label>
                                <input type="password" required placeholder="••••">
                            </div>
                            <button type="submit" class="btn btn-khalti">Pay Now</button>
                        </form>
                    </c:when>

                    <c:otherwise>
                        <!-- Card fallback -->
                        <img src="images/card.png" alt="Card" class="logo-img"
                            onerror="this.src='https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Mastercard-logo.svg/1200px-Mastercard-logo.svg.png'">
                        <h3 style="margin-top: 0;">Pay NPR ${sessionScope.pendingOrder.totalAmount}</h3>
                        <form action="${pageContext.request.contextPath}/payment" method="post">
                            <div class="form-group">
                                <label>Card Number</label>
                                <input type="text" required placeholder="0000 0000 0000 0000">
                            </div>
                            <div style="display: flex; gap: 1rem;">
                                <div class="form-group" style="flex: 1;">
                                    <label>Expiry Date</label>
                                    <input type="text" required placeholder="MM/YY">
                                </div>
                                <div class="form-group" style="flex: 1;">
                                    <label>CVV</label>
                                    <input type="password" required placeholder="123">
                                </div>
                            </div>
                            <button type="submit" class="btn btn-card">Submit Payment</button>
                        </form>
                    </c:otherwise>
                </c:choose>

                <a href="${pageContext.request.contextPath}/checkout" class="cancel-link">Cancel and return to
                    checkout</a>
            </div>

        </body>

        </html>
