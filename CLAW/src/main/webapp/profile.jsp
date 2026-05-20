<%@ include file="components/header.jsp" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <style>
            /* Profile layout: Sidebar for pic/points and Main for forms */
            .profile-container {
                display: flex;
                max-width: 800px;
                margin: 4rem auto;
                background-color: var(--card-bg);
                border-radius: 12px;
                overflow: hidden;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
                border: 1px solid var(--border);
            }

            .profile-sidebar {
                padding: 2.5rem;
                background-color: rgba(0, 0, 0, 0.2);
                text-align: center;
                width: 300px;
                border-right: 1px solid var(--border);
            }

            .profile-pic {
                width: 150px;
                height: 150px;
                border-radius: 50%;
                object-fit: cover;
                border: 4px solid var(--accent);
                margin-bottom: 1.5rem;
            }

            /* The shiny badge showing current loyalty points */
            .points-badge {
                display: inline-block;
                background-color: var(--accent);
                color: white;
                padding: 0.5rem 1rem;
                border-radius: 20px;
                font-weight: bold;
                margin-top: 1rem;
                font-size: 1.1rem;
            }

            .profile-content {
                padding: 2.5rem;
                flex: 1;
            }

            /* [MOBILE ONLY] Stack profile sidebar and content */
            @media (max-width: 768px) {
                .profile-container {
                    flex-direction: column;
                    margin: 2rem 1rem;
                }

                .profile-sidebar {
                    width: 100%;
                    border-right: none;
                    border-bottom: 1px solid var(--border);
                }
            }
        </style>

        <div class="profile-container">
            <!-- SIDEBAR: User stats and quick links -->
            <div class="profile-sidebar">
                <!-- Profile Image from session -->
                <img src="${sessionScope.user.profilePictureUrl}" alt="Profile Picture" class="profile-pic">
                <h2>${sessionScope.user.username}</h2>
                <p style="color: var(--text-muted);">${sessionScope.user.email}</p>

                <!-- Display reward points -->
                <div class="points-badge">
                    &#9733; ${sessionScope.user.points} Points
                </div>

                <!-- Navigation to Rewards store -->
                <div style="margin-top: 2rem;">
                    <a href="${pageContext.request.contextPath}/rewards" class="btn btn-outline"
                        style="width: 100%;">Redeem Rewards</a>
                </div>
            </div>

            <!-- MAIN CONTENT: Settings and Edit form -->
            <div class="profile-content">
                <h1 style="margin-bottom: 2rem;">Edit Profile</h1>

                <!-- Feedback message after update -->
                <c:if test="${not empty success}">
                    <div class="success-msg">${success}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data">
                    <!-- CHANGE USERNAME -->
                    <div class="form-group">
                        <label for="username">Display Name</label>
                        <input type="text" id="username" name="username" value="${sessionScope.user.username}" required>
                    </div>

                    <!-- UPLOAD NEW PIC -->
                    <div class="form-group">
                        <label for="profilePicture">Profile Picture</label>
                        <input type="file" id="profilePicture" name="profilePicture" accept="image/*">
                        <small style="color: var(--text-muted); margin-top: 0.5rem; display: block;">Choose an image
                            from your device.</small>
                    </div>

                    <button type="submit" class="btn" style="margin-top: 1rem;">Save Changes</button>
                </form>
            </div>
        </div>

        <%@ include file="components/footer.jsp" %>

