<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="components/header.jsp" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
                <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                    <style>
                        /* Premium Admin Dashboard Layout */
                        .admin-container {
                            max-width: 1200px;
                            margin: 4rem auto;
                            padding: 0 2rem;
                        }

                        .admin-header {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            margin-bottom: 2.5rem;
                            border-bottom: 1px solid var(--border);
                            padding-bottom: 1.5rem;
                        }

                        .admin-header h1 {
                            font-size: 2.5rem;
                            font-weight: 800;
                            text-transform: uppercase;
                            letter-spacing: 2px;
                        }

                        /* Tabbed Menu Bar */
                        .admin-tabs {
                            display: flex;
                            gap: 0.5rem;
                            margin-bottom: 2.5rem;
                            border-bottom: 1px solid var(--border);
                            padding-bottom: 0.5rem;
                        }

                        .tab-btn {
                            background: transparent;
                            border: none;
                            color: var(--text-muted);
                            font-size: 1.05rem;
                            font-weight: 600;
                            cursor: pointer;
                            padding: 0.6rem 1.2rem;
                            border-radius: 6px;
                            transition: all 0.3s ease;
                            display: flex;
                            align-items: center;
                            gap: 0.5rem;
                        }

                        .tab-btn:hover {
                            color: var(--text);
                            background-color: rgba(255, 255, 255, 0.05);
                        }

                        .tab-btn.active {
                            color: var(--accent);
                            background-color: rgba(255, 71, 87, 0.1);
                            border: 1px solid rgba(255, 71, 87, 0.2);
                        }

                        .tab-content {
                            display: none;
                        }

                        .tab-content.active {
                            display: block;
                            animation: fadeIn 0.4s ease-out;
                        }

                        @keyframes fadeIn {
                            from {
                                opacity: 0;
                                transform: translateY(8px);
                            }

                            to {
                                opacity: 1;
                                transform: translateY(0);
                            }
                        }

                        /* Export Toolbar */
                        .export-toolbar {
                            display: flex;
                            gap: 0.75rem;
                            flex-wrap: wrap;
                        }

                        .btn-export {
                            padding: 0.6rem 1.2rem;
                            font-size: 0.85rem;
                            font-weight: bold;
                            display: inline-flex;
                            align-items: center;
                            gap: 0.5rem;
                            text-decoration: none;
                        }

                        /* Stats Grid */
                        .stats-grid {
                            display: grid;
                            grid-template-columns: repeat(4, 1fr);
                            gap: 1.5rem;
                            margin-bottom: 3.5rem;
                        }

                        @media (max-width: 992px) {
                            .stats-grid {
                                grid-template-columns: repeat(2, 1fr);
                            }
                        }

                        @media (max-width: 576px) {
                            .stats-grid {
                                grid-template-columns: 1fr;
                            }
                        }

                        .stat-card {
                            background-color: var(--card-bg);
                            padding: 1.8rem;
                            border-radius: 12px;
                            border: 1px solid var(--border);
                            text-align: left;
                            position: relative;
                            overflow: hidden;
                            transition: transform 0.3s ease, border-color 0.3s ease;
                        }

                        .stat-card:hover {
                            transform: translateY(-3px);
                            border-color: rgba(255, 71, 87, 0.3);
                        }

                        .stat-card::before {
                            content: '';
                            position: absolute;
                            top: 0;
                            left: 0;
                            width: 4px;
                            height: 100%;
                            background-color: var(--accent);
                            opacity: 0.7;
                        }

                        .stat-card.realized::before {
                            background-color: #2ed573;
                        }

                        .stat-card.pending::before {
                            background-color: #ffa502;
                        }

                        .stat-card.aov::before {
                            background-color: #4073ff;
                        }

                        .stat-title {
                            color: var(--text-muted);
                            text-transform: uppercase;
                            font-size: 0.8rem;
                            font-weight: 700;
                            letter-spacing: 1px;
                            margin-bottom: 0.5rem;
                        }

                        .stat-value {
                            font-size: 2.2rem;
                            font-weight: 800;
                            color: var(--text);
                        }

                        .stat-card.realized .stat-value {
                            color: #2ed573;
                        }

                        .stat-card.pending .stat-value {
                            color: #ffa502;
                        }

                        /* Layout Split for Analytics */
                        .analytics-split {
                            display: grid;
                            grid-template-columns: 1.4fr 1fr;
                            gap: 2rem;
                        }

                        @media (max-width: 992px) {
                            .analytics-split {
                                grid-template-columns: 1fr;
                            }
                        }

                        .analytics-card {
                            background-color: var(--card-bg);
                            border: 1px solid var(--border);
                            border-radius: 12px;
                            padding: 2rem;
                            margin-bottom: 2rem;
                        }

                        .analytics-card-title {
                            font-size: 1.3rem;
                            font-weight: 800;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                            margin-bottom: 1.5rem;
                            border-bottom: 1px solid var(--border);
                            padding-bottom: 0.8rem;
                            color: var(--text);
                        }

                        /* Profit & Loss Table Styling */
                        .pl-table {
                            width: 100%;
                            border-collapse: collapse;
                        }

                        .pl-table td {
                            padding: 1rem 0;
                            border-bottom: 1px solid var(--border);
                            font-size: 0.95rem;
                        }

                        .pl-table tr:last-child td {
                            border-bottom: none;
                        }

                        .pl-label {
                            font-weight: 600;
                        }

                        .pl-value {
                            text-align: right;
                            font-family: monospace;
                            font-weight: bold;
                            font-size: 1.05rem;
                        }

                        .pl-desc {
                            font-size: 0.8rem;
                            color: var(--text-muted);
                            margin-top: 0.2rem;
                            display: block;
                            font-weight: normal;
                        }

                        .pl-header-row td {
                            font-weight: 800;
                            text-transform: uppercase;
                            font-size: 0.85rem;
                            letter-spacing: 1px;
                            color: var(--text-muted);
                            border-bottom: 2px solid var(--border);
                        }

                        .pl-divider-row td {
                            border-bottom: 2px solid var(--border);
                        }

                        .pl-total-row td {
                            background-color: rgba(255, 255, 255, 0.02);
                            padding: 1.2rem 1rem;
                            border-top: 2px solid var(--border);
                            border-bottom: 2px solid var(--border);
                        }

                        .pl-total-row .pl-label {
                            font-size: 1.1rem;
                            color: var(--accent);
                        }

                        .pl-total-row .pl-value {
                            font-size: 1.25rem;
                            color: var(--accent);
                        }

                        /* Best Sellers Progress List */
                        .seller-item {
                            margin-bottom: 1.5rem;
                        }

                        .seller-meta {
                            display: flex;
                            justify-content: space-between;
                            margin-bottom: 0.4rem;
                            font-size: 0.95rem;
                        }

                        .progress-track {
                            height: 8px;
                            background-color: rgba(255, 255, 255, 0.05);
                            border-radius: 4px;
                            overflow: hidden;
                        }

                        .progress-bar {
                            height: 100%;
                            background-color: var(--accent);
                            border-radius: 4px;
                        }

                        /* Audit Logs Timeline */
                        .timeline {
                            position: relative;
                            padding: 1rem 0;
                        }

                        .timeline::before {
                            content: '';
                            position: absolute;
                            left: 20px;
                            top: 0;
                            bottom: 0;
                            width: 2px;
                            background-color: var(--border);
                        }

                        .timeline-item {
                            position: relative;
                            padding-left: 55px;
                            margin-bottom: 2.2rem;
                        }

                        .timeline-badge {
                            position: absolute;
                            left: 3px;
                            top: 2px;
                            width: 36px;
                            height: 36px;
                            border-radius: 50%;
                            background-color: #1a1a1a;
                            border: 2px solid var(--border);
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            font-size: 1rem;
                            z-index: 1;
                        }

                        /* Color Badges for Log Event Actions */
                        .timeline-item.login .timeline-badge {
                            border-color: #4073ff;
                            color: #4073ff;
                            background-color: rgba(64, 115, 255, 0.1);
                        }

                        .timeline-item.registration .timeline-badge {
                            border-color: #2ed573;
                            color: #2ed573;
                            background-color: rgba(46, 213, 115, 0.1);
                        }

                        .timeline-item.order .timeline-badge {
                            border-color: var(--accent);
                            color: var(--accent);
                            background-color: rgba(255, 71, 87, 0.1);
                        }

                        .timeline-item.delivered .timeline-badge {
                            border-color: #ffa502;
                            color: #ffa502;
                            background-color: rgba(255, 165, 2, 0.1);
                        }

                        .timeline-item.role .timeline-badge {
                            border-color: #eccc68;
                            color: #eccc68;
                            background-color: rgba(236, 204, 104, 0.1);
                        }

                        .timeline-item.export .timeline-badge {
                            border-color: #a55eea;
                            color: #a55eea;
                            background-color: rgba(165, 94, 234, 0.1);
                        }

                        .timeline-content {
                            background-color: var(--card-bg);
                            border: 1px solid var(--border);
                            border-radius: 10px;
                            padding: 1.2rem;
                        }

                        .timeline-meta {
                            display: flex;
                            justify-content: space-between;
                            font-size: 0.82rem;
                            color: var(--text-muted);
                            margin-bottom: 0.4rem;
                            flex-wrap: wrap;
                            gap: 0.5rem;
                        }

                        .timeline-actor {
                            font-weight: 700;
                            color: var(--text);
                        }

                        .timeline-action {
                            text-transform: uppercase;
                            font-size: 0.75rem;
                            font-weight: bold;
                            letter-spacing: 0.5px;
                            padding: 0.15rem 0.5rem;
                            border-radius: 4px;
                            background-color: rgba(255, 255, 255, 0.05);
                        }

                        .timeline-details {
                            font-size: 0.95rem;
                            color: var(--text);
                            line-height: 1.4;
                        }

                        /* Print media style stylesheet integration */
                        @media print {
                            body {
                                background-color: white !important;
                                color: black !important;
                                font-family: 'Inter', 'Segoe UI', Arial, sans-serif !important;
                            }

                            .header,
                            .footer,
                            .admin-tabs,
                            .btn,
                            .btn-export,
                            form,
                            .admin-table td:last-child,
                            .admin-table th:last-child {
                                display: none !important;
                            }

                            .admin-container {
                                margin: 0 !important;
                                padding: 0 !important;
                                max-width: 100% !important;
                            }

                            .tab-content {
                                display: block !important;
                                /* Force all tabs to display for audit records printing */
                                page-break-after: always;
                            }

                            .tab-content h2,
                            .tab-content h3,
                            .analytics-card-title {
                                color: black !important;
                                border-bottom: 2px solid #333 !important;
                                margin-top: 2rem !important;
                            }

                            .stat-card {
                                border: 1px solid #444 !important;
                                background: transparent !important;
                                box-shadow: none !important;
                                color: black !important;
                                float: left;
                                width: 22%;
                                margin-right: 2%;
                                margin-bottom: 2rem;
                            }

                            .stats-grid {
                                display: block !important;
                                content: "";
                                clear: both;
                            }

                            .stat-value {
                                color: black !important;
                                font-size: 1.8rem !important;
                            }

                            .analytics-split {
                                display: block !important;
                            }

                            .analytics-card {
                                background: transparent !important;
                                border: 1px solid #444 !important;
                                page-break-inside: avoid;
                            }

                            .admin-table {
                                background: transparent !important;
                                border: 1px solid #333 !important;
                                color: black !important;
                            }

                            .admin-table th {
                                background-color: #eaeaea !important;
                                color: black !important;
                                border-bottom: 2px solid #333 !important;
                            }

                            .admin-table td {
                                border-bottom: 1px solid #ccc !important;
                                color: black !important;
                            }

                            .pl-table td {
                                border-bottom: 1px solid #ccc !important;
                                color: black !important;
                            }

                            .pl-value {
                                color: black !important;
                            }

                            .pl-total-row td {
                                border-top: 2px solid #333 !important;
                                border-bottom: 2px solid #333 !important;
                                background-color: #f5f5f5 !important;
                            }

                            .pl-total-row .pl-label,
                            .pl-total-row .pl-value {
                                color: black !important;
                            }

                            .progress-track {
                                border: 1px solid #444 !important;
                                background: #eaeaea !important;
                            }

                            .progress-bar {
                                background: #333 !important;
                            }

                            .timeline::before {
                                background-color: #333 !important;
                            }

                            .timeline-badge {
                                background-color: white !important;
                                border-color: #333 !important;
                                color: black !important;
                            }

                            .timeline-content {
                                border: 1px solid #444 !important;
                                background: transparent !important;
                                color: black !important;
                            }

                            .timeline-actor,
                            .timeline-details {
                                color: black !important;
                            }
                        }
                    </style>

                    <div class="admin-container">

                        <!-- Top Dashboard Header with actions -->
                        <div class="admin-header">
                            <div>
                                <h1>Command Center</h1>
                                <p style="color: var(--text-muted); margin-top: 0.3rem;">Oversee transactions, track
                                    sales
                                    velocity, and review audit records.</p>
                            </div>

                            <!-- Dropdown / Exports Toolbar -->
                            <div class="export-toolbar">
                                <a href="${pageContext.request.contextPath}/admin?export=sales"
                                    class="btn btn-outline btn-export" title="Download entire orders database as CSV">
                                    <span>Export Sales</span>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin?export=pl"
                                    class="btn btn-outline btn-export" title="Download financial statement as CSV">
                                    <span>Export P&L</span>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin?export=logs"
                                    class="btn btn-outline btn-export"
                                    title="Download system logs audit history as CSV">
                                    <span>Export Logs</span>
                                </a>
                                <button onclick="window.print();" class="btn btn-export"
                                    style="background-color: var(--accent);"
                                    title="Print or save this complete dashboard as PDF">
                                    <span>Print / Save PDF</span>
                                </button>
                            </div>
                        </div>

                        <!-- Alert messages for success/error actions -->
                        <c:if test="${not empty success}">
                            <div class="success-msg"
                                style="background-color: rgba(46, 213, 115, 0.1); border: 1px solid rgba(46, 213, 115, 0.3); padding: 1rem; border-radius: 8px; color: #2ed573; margin-bottom: 1.5rem; text-align: center;">
                                ${success}
                            </div>
                        </c:if>
                        <c:if test="${not empty error}">
                            <div class="error-msg"
                                style="background-color: rgba(255, 71, 87, 0.1); border: 1px solid rgba(255, 71, 87, 0.3); padding: 1rem; border-radius: 8px; color: #ff4757; margin-bottom: 1.5rem; text-align: center;">
                                ${error}
                            </div>
                        </c:if>

                        <!-- Visual Tab bar -->
                        <div class="admin-tabs">
                            <button class="tab-btn active" onclick="openTab(event, 'command-tab')">
                                Order & User Management
                            </button>
                            <button class="tab-btn" onclick="openTab(event, 'analytics-tab')">
                                Business Analytics & P&L
                            </button>
                            <button class="tab-btn" onclick="openTab(event, 'logs-tab')">
                                System Audit Logs
                            </button>
                        </div>

                        <!-- TAB 1: ORDER & USER MANAGEMENT -->
                        <div id="command-tab" class="tab-content active">
                            <!-- STATS OVERVIEW FOR QUICK EYE-CATCHING COHERENCE -->
                            <div class="stats-grid">
                                <div class="stat-card">
                                    <div class="stat-title">Gross Retail Sales</div>
                                    <div class="stat-value">NPR
                                        <c:out value="${report.grossSales}" default="0" />
                                    </div>
                                </div>
                                <div class="stat-card realized">
                                    <div class="stat-title">Realized Revenue</div>
                                    <div class="stat-value">NPR
                                        <c:out value="${report.realizedRevenue}" default="0" />
                                    </div>
                                </div>
                                <div class="stat-card pending">
                                    <div class="stat-title">Pending Orders</div>
                                    <div class="stat-value">
                                        <c:out value="${report.pendingOrdersCount}" default="0" />
                                    </div>
                                </div>
                                <div class="stat-card aov">
                                    <div class="stat-title">Avg Order Value</div>
                                    <div class="stat-value">NPR
                                        <c:out value="${report.averageOrderValue}" default="0" />
                                    </div>
                                </div>
                            </div>

                            <!-- ORDERS MANAGEMENT TABLE -->
                            <h2>Recent Transactions</h2>
                            <div class="table-responsive" style="margin-bottom: 4rem;">
                                <table class="admin-table">
                                    <thead>
                                        <tr>
                                            <th>Order ID</th>
                                            <th>Customer</th>
                                            <th>Items Summary</th>
                                            <th>Total Amount</th>
                                            <th>Method</th>
                                            <th>Shipping & Delivery Address</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty orders}">
                                                <tr>
                                                    <td colspan="8"
                                                        style="text-align: center; padding: 3rem; color: var(--text-muted);">
                                                        No transaction records placed yet.
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${orders}" var="order">
                                                    <tr>
                                                        <td
                                                            style="font-family: monospace; font-weight: bold; color: var(--accent);">
                                                            #${order.id}</td>
                                                        <td><strong>${order.user.username}</strong></td>
                                                        <td style="max-width: 220px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"
                                                            title="${order.itemsSummary}">
                                                            ${order.itemsSummary}
                                                        </td>
                                                        <td><strong>NPR ${order.totalAmount}</strong></td>
                                                        <td><span
                                                                style="text-transform: uppercase; font-size: 0.85rem; font-weight: bold; background-color: rgba(255,255,255,0.05); padding: 0.2rem 0.5rem; border-radius: 4px;">${order.paymentMethod}</span>
                                                        </td>

                                                        <td style="font-size: 0.85rem; line-height: 1.45;">
                                                            <div style="font-weight: 700;">${order.streetAddress},
                                                                ${order.city}
                                                            </div>
                                                            <div style="color: var(--text-muted);">${order.postalCode} |
                                                                Phone:
                                                                ${order.phone}</div>
                                                            <div style="font-style: italic; color: var(--text-muted);">
                                                                ${order.email}</div>
                                                        </td>

                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${order.status == 'DELIVERED'}">
                                                                    <span
                                                                        class="status-badge status-delivered">DELIVERED</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span
                                                                        class="status-badge status-processing">PROCESSING</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>

                                                        <td>
                                                            <c:if test="${order.status != 'DELIVERED'}">
                                                                <form action="${pageContext.request.contextPath}/admin"
                                                                    method="post" style="margin: 0;">
                                                                    <input type="hidden" name="action"
                                                                        value="confirmDelivery">
                                                                    <input type="hidden" name="orderId"
                                                                        value="${order.id}">
                                                                    <button type="submit" class="btn btn-outline"
                                                                        style="padding: 0.5rem 1rem; font-size: 0.75rem; font-weight: bold; border-color: #ffa502; color: #ffa502;">
                                                                        Confirm Delivery
                                                                    </button>
                                                                </form>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>

                            <!-- USER ROLES MANAGEMENT -->
                            <h2>User Accounts Roles</h2>
                            <p style="color: var(--text-muted); margin-bottom: 1.5rem;">Audit registrations, manage
                                administrative permissions, and configure Co-Admins.</p>
                            <div class="table-responsive">
                                <table class="admin-table">
                                    <thead>
                                        <tr>
                                            <th>Account Holder</th>
                                            <th>Email Address</th>
                                            <th>System Permissions</th>
                                            <th>Management Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${usersList}" var="u">
                                            <tr>
                                                <td style="display: flex; align-items: center; gap: 1rem;">
                                                    <img src="${u.profilePictureUrl}"
                                                        style="width: 32px; height: 32px; border-radius: 50%; object-fit: cover; border: 1px solid var(--border);">
                                                    <strong>${u.username}</strong>
                                                </td>
                                                <td>${u.email}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${u.admin}">
                                                            <span class="status-badge"
                                                                style="background-color: rgba(255, 71, 87, 0.15); color: var(--accent); border: 1px solid rgba(255, 71, 87, 0.3);">ADMINISTRATOR</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="status-badge"
                                                                style="background-color: rgba(255,255,255,0.05); color: var(--text-muted); border: 1px solid var(--border);">STANDARD
                                                                USER</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <form action="${pageContext.request.contextPath}/admin"
                                                        method="post" style="margin: 0;">
                                                        <input type="hidden" name="action" value="toggleAdmin">
                                                        <input type="hidden" name="userEmail" value="${u.email}">
                                                        <c:choose>
                                                            <c:when test="${u.admin}">
                                                                <button type="submit" class="btn btn-outline"
                                                                    style="padding: 0.5rem 1rem; font-size: 0.75rem; border-color: #ff4757; color: #ff4757; font-weight: bold;">
                                                                    Revoke Admin
                                                                </button>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <button type="submit" class="btn btn-outline"
                                                                    style="padding: 0.5rem 1rem; font-size: 0.75rem; border-color: var(--accent); color: var(--accent); font-weight: bold;">
                                                                    Promote Co-Admin
                                                                </button>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- TAB 2: BUSINESS ANALYTICS & P&L -->
                        <div id="analytics-tab" class="tab-content">
                            <div class="analytics-split">

                                <!-- LEFT COLUMN: FINANCIAL PROFIT & LOSS STATEMENT -->
                                <div class="analytics-card">
                                    <h3 class="analytics-card-title">Profit & Loss Statement</h3>
                                    <table class="pl-table">
                                        <tbody>
                                            <tr class="pl-header-row">
                                                <td>Financial Statement Line</td>
                                                <td style="text-align: right;">Amount (NPR)</td>
                                            </tr>

                                            <tr>
                                                <td class="pl-label">
                                                    Gross Retail Sales
                                                    <span class="pl-desc">Aggregated face value of all placed order
                                                        transactions.</span>
                                                </td>
                                                <td class="pl-value">NPR ${report.grossSales}</td>
                                            </tr>

                                            <tr>
                                                <td class="pl-label" style="color: #2ed573;">
                                                    Realized Sourced Revenue
                                                    <span class="pl-desc">Actual captured revenue received from
                                                        delivered
                                                        packages.</span>
                                                </td>
                                                <td class="pl-value" style="color: #2ed573;">NPR
                                                    ${report.realizedRevenue}</td>
                                            </tr>

                                            <tr>
                                                <td class="pl-label">
                                                    Cost of Goods Sold (COGS)
                                                    <span class="pl-desc">Estimated manufacturing, printing, and
                                                        inventory cost
                                                        (50% of realized revenue).</span>
                                                </td>
                                                <td class="pl-value" style="color: #ff4757;">- NPR ${report.cogs}</td>
                                            </tr>

                                            <tr class="pl-divider-row">
                                                <td class="pl-label" style="font-size: 1.05rem;">Gross Profit Margin
                                                </td>
                                                <td class="pl-value" style="font-size: 1.1rem; color: #2ed573;">NPR
                                                    ${report.grossProfit}</td>
                                            </tr>

                                            <tr style="background-color: rgba(255,255,255,0.01);">
                                                <td colspan="2"
                                                    style="font-weight: 800; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.5px; padding-left: 0.5rem; color: var(--text-muted);">
                                                    Operating Overhead Expenses
                                                </td>
                                            </tr>

                                            <tr>
                                                <td class="pl-label">
                                                    Domain & Infrastructure Hosting
                                                    <span class="pl-desc">Fixed server fees and deployment scaling
                                                        configurations.</span>
                                                </td>
                                                <td class="pl-value">- NPR ${report.expenseHosting}</td>
                                            </tr>

                                            <tr>
                                                <td class="pl-label">
                                                    Courier Logistics & Delivery Fees
                                                    <span class="pl-desc">Simulated distribution shipping and delivery
                                                        fees (8%
                                                        of realized sales).</span>
                                                </td>
                                                <td class="pl-value">- NPR ${report.expenseLogistics}</td>
                                            </tr>

                                            <tr>
                                                <td class="pl-label">
                                                    Retail Packaging & Sourcing Box Labels
                                                    <span class="pl-desc">Streetwear stickers, boxes, and luxury
                                                        wrappers at NPR
                                                        150 per order.</span>
                                                </td>
                                                <td class="pl-value">- NPR ${report.expensePackaging}</td>
                                            </tr>

                                            <tr>
                                                <td class="pl-label">
                                                    Marketing Ads & Rewards Loyalty Credit
                                                    <span class="pl-desc">Promotional campaigns, acquisition costs, and
                                                        reward
                                                        point allocations (12% of sales).</span>
                                                </td>
                                                <td class="pl-value">- NPR ${report.expenseMarketing}</td>
                                            </tr>

                                            <tr>
                                                <td class="pl-label" style="font-weight: bold;">Total Operating Expenses
                                                </td>
                                                <td class="pl-value" style="color: #ff4757;">- NPR
                                                    ${report.totalExpenses}</td>
                                            </tr>

                                            <tr class="pl-total-row">
                                                <td class="pl-label">
                                                    Net Realized Profit
                                                    <span class="pl-desc" style="color: var(--text-muted);">Bottom-line
                                                        clean
                                                        earnings after all expenses.</span>
                                                </td>
                                                <c:choose>
                                                    <c:when test="${report.netProfit >= 0}">
                                                        <td class="pl-value" style="color: #2ed573;">NPR
                                                            ${report.netProfit}</td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="pl-value" style="color: #ff4757;">NPR
                                                            ${report.netProfit}</td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tr>

                                            <tr>
                                                <td class="pl-label">Net Profit Margin Percentage</td>
                                                <td class="pl-value" style="color: #2ed573;">
                                                    <fmt:formatNumber value="${report.netProfitMargin}"
                                                        maxFractionDigits="2" />%
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- RIGHT COLUMN: PRODUCT ANALYSIS & PAYMENT METHOD DATA -->
                                <div>
                                    <!-- BEST SELLERS LEADERBOARD -->
                                    <div class="analytics-card">
                                        <h3 class="analytics-card-title">Inventory Analytics (Best Sellers)</h3>
                                        <c:choose>
                                            <c:when test="${empty report.bestSellers}">
                                                <div
                                                    style="text-align: center; color: var(--text-muted); padding: 2rem 0;">
                                                    No items have been purchased yet to compile inventory analytics.
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Determine the highest sales value for sizing progress bars -->
                                                <c:set var="maxSold" value="1" />
                                                <c:forEach items="${report.bestSellers}" var="item" varStatus="vs">
                                                    <c:if test="${vs.first}">
                                                        <c:set var="maxSold" value="${item.value}" />
                                                    </c:if>
                                                </c:forEach>

                                                <c:forEach items="${report.bestSellers}" var="item">
                                                    <div class="seller-item">
                                                        <div class="seller-meta">
                                                            <span class="pl-label">${item.key}</span>
                                                            <strong style="color: var(--accent);">${item.value}
                                                                Sold</strong>
                                                        </div>
                                                        <div class="progress-track">
                                                            <jsp:element name="div">
                                                                <jsp:attribute name="class">progress-bar</jsp:attribute>
                                                                <jsp:attribute name="style">width: ${(item.value /
                                                                    maxSold) * 100}%; background-color: var(--accent);
                                                                </jsp:attribute>
                                                            </jsp:element>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <!-- PAYMENT GATEWAYS BREAKDOWN -->
                                    <div class="analytics-card">
                                        <h3 class="analytics-card-title">Payment Gateway Distribution</h3>
                                        <c:choose>
                                            <c:when test="${empty report.countByPaymentMethod}">
                                                <div
                                                    style="text-align: center; color: var(--text-muted); padding: 2rem 0;">
                                                    No transactions placed yet to compile payment gateway data.
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${report.countByPaymentMethod}" var="gateway">
                                                    <div class="seller-item">
                                                        <div class="seller-meta">
                                                            <span class="pl-label"
                                                                style="text-transform: uppercase;">${gateway.key}</span>
                                                            <span>
                                                                <strong>${gateway.value} Trx</strong>
                                                                <span
                                                                    style="color: var(--text-muted); font-size: 0.85rem; margin-left: 0.5rem;">
                                                                    (NPR
                                                                    <c:out
                                                                        value="${report.revenueByPaymentMethod[gateway.key]}"
                                                                        default="0" />)
                                                                </span>
                                                            </span>
                                                        </div>
                                                        <div class="progress-track">
                                                            <jsp:element name="div">
                                                                <jsp:attribute name="class">progress-bar</jsp:attribute>
                                                                <jsp:attribute name="style">width: ${(gateway.value /
                                                                    report.totalOrdersCount) * 100}%; background-color:
                                                                    #4073ff;</jsp:attribute>
                                                            </jsp:element>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                            </div>
                        </div>

                        <!-- TAB 3: SYSTEM AUDIT LOGS -->
                        <div id="logs-tab" class="tab-content">
                            <h2>System Audit Logs History</h2>
                            <p style="color: var(--text-muted); margin-bottom: 2rem;">Real-time logging audits tracking
                                account
                                activities, logs exports, updates, and order entries.</p>

                            <div class="timeline">
                                <c:choose>
                                    <c:when test="${empty auditLogs}">
                                        <div
                                            style="text-align: center; color: var(--text-muted); padding: 3rem 0; background-color: var(--card-bg); border-radius: 12px; border: 1px solid var(--border);">
                                            No system audit records found.
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${auditLogs}" var="log">
                                            <!-- Determine class by action category -->
                                            <c:set var="logClass" value="" />
                                            <c:set var="badgeChar" value="SY" />

                                            <c:choose>
                                                <c:when test="${fn:contains(fn:toLowerCase(log.action), 'login')}">
                                                    <c:set var="logClass" value="login" />
                                                    <c:set var="badgeChar" value="LG" />
                                                </c:when>
                                                <c:when test="${fn:contains(fn:toLowerCase(log.action), 'register')}">
                                                    <c:set var="logClass" value="registration" />
                                                    <c:set var="badgeChar" value="RG" />
                                                </c:when>
                                                <c:when test="${fn:contains(fn:toLowerCase(log.action), 'order')}">
                                                    <c:choose>
                                                        <c:when
                                                            test="${fn:contains(fn:toLowerCase(log.action), 'deliver')}">
                                                            <c:set var="logClass" value="delivered" />
                                                            <c:set var="badgeChar" value="DL" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="logClass" value="order" />
                                                            <c:set var="badgeChar" value="OR" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:when
                                                    test="${fn:contains(fn:toLowerCase(log.action), 'role') || fn:contains(fn:toLowerCase(log.action), 'admin')}">
                                                    <c:set var="logClass" value="role" />
                                                    <c:set var="badgeChar" value="AD" />
                                                </c:when>
                                                <c:when test="${fn:contains(fn:toLowerCase(log.action), 'export')}">
                                                    <c:set var="logClass" value="export" />
                                                    <c:set var="badgeChar" value="EX" />
                                                </c:when>
                                            </c:choose>

                                            <div class="timeline-item ${logClass}">
                                                <div class="timeline-badge">${badgeChar}</div>
                                                <div class="timeline-content">
                                                    <div class="timeline-meta">
                                                        <span>Actor: <span
                                                                class="timeline-actor">${log.username}</span></span>
                                                        <span class="timeline-action">${log.action}</span>
                                                        <span style="font-family: monospace;">${log.createdAt}</span>
                                                    </div>
                                                    <div class="timeline-details">${log.details}</div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                    </div>

                    <!-- JAVASCRIPT FOR TABS TOGGLING AND PRINT ADJUSTMENTS -->
                    <script>
                        function openTab(evt, tabId) {
                            // Declare all variables
                            var i, tabcontent, tablinks;

                            // Get all elements with class="tab-content" and hide them
                            tabcontent = document.getElementsByClassName("tab-content");
                            for (i = 0; i < tabcontent.length; i++) {
                                tabcontent[i].classList.remove("active");
                            }

                            // Get all elements with class="tab-btn" and remove the class "active"
                            tablinks = document.getElementsByClassName("tab-btn");
                            for (i = 0; i < tablinks.length; i++) {
                                tablinks[i].classList.remove("active");
                            }

                            // Show the current tab, and add an "active" class to the button that opened the tab
                            document.getElementById(tabId).classList.add("active");
                            evt.currentTarget.classList.add("active");

                            // Save the active tab in localStorage so it stays open on refresh!
                            localStorage.setItem("activeAdminTab", tabId);
                        }

                        // On page load, restore the previously active tab if it exists
                        document.addEventListener("DOMContentLoaded", function () {
                            var activeTab = localStorage.getItem("activeAdminTab");
                            if (activeTab) {
                                var tabBtn = document.querySelector('[onclick*="' + activeTab + '"]');
                                if (tabBtn) {
                                    // Trigger a simulated click on the saved tab button
                                    var event = new MouseEvent('click', {
                                        bubbles: true,
                                        cancelable: true,
                                        view: window
                                    });
                                    tabBtn.dispatchEvent(event);
                                }
                            }
                        });
                    </script>

                    <%@ include file="components/footer.jsp" %>