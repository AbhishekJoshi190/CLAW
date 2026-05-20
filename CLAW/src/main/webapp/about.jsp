<%@ include file="components/header.jsp" %>

    <div style="max-width: 800px; margin: 0 auto; text-align: center; padding: 4rem 1.5rem;">
        <img src="${pageContext.request.contextPath}/images/logo.png" alt="CLAW Logo"
            style="height: 120px; width: auto; margin-bottom: 2rem; filter: invert(1); mix-blend-mode: screen;">

        <h1 style="font-size: 3rem; margin-bottom: 2rem; letter-spacing: 4px; text-transform: uppercase;">ABOUT CLAW
        </h1>

        <p style="font-size: 1.2rem; color: var(--text-muted); margin-bottom: 2rem; line-height: 1.8;">
            CLAW was born in the neon-lit streets, forged by the desire to create uncompromising apparel for those who
            dictate their own path. We blend avant-garde silhouettes with functional utility, delivering streetwear that
            doesn't just look the part, but lives it.
        </p>

        <p style="font-size: 1.2rem; color: var(--text-muted); line-height: 1.8; margin-bottom: 4rem;">
            Every piece is a statement. Join the movement.
        </p>

        <div
            style="background-color: var(--card-bg); padding: 3rem; border-radius: 12px; border: 1px solid var(--border); margin-top: 2rem;">
            <h2 style="font-size: 2rem; margin-bottom: 1.5rem; letter-spacing: 2px;">CONTACT US</h2>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; text-align: left;">
                <div>
                    <h3
                        style="color: var(--accent); margin-bottom: 0.5rem; font-size: 0.9rem; text-transform: uppercase;">
                        Phone</h3>
                    <p style="font-size: 1.1rem; font-weight: 600;">+977 980666666</p>
                </div>
                <div>
                    <h3
                        style="color: var(--accent); margin-bottom: 0.5rem; font-size: 0.9rem; text-transform: uppercase;">
                        Email</h3>
                    <p style="font-size: 1.1rem; font-weight: 600;">claw.inp.np</p>
                </div>
            </div>

            <div style="margin-top: 2rem; padding-top: 2rem; border-top: 1px solid var(--border);">
                <h3 style="color: var(--accent); margin-bottom: 0.5rem; font-size: 0.9rem; text-transform: uppercase;">
                    Location</h3>
                <p style="font-size: 1.1rem; font-weight: 600;">Streetwear District, Pokhara, Nepal</p>
            </div>
        </div>

        <div style="margin-top: 4rem;">
            <img src="https://images.unsplash.com/photo-1523381210434-271e8be1f52b?q=80&w=2070&auto=format&fit=crop"
                alt="CLAW Culture" style="width: 100%; border-radius: 8px; filter: grayscale(100%) brightness(0.7);">
        </div>
    </div>

    <%@ include file="components/footer.jsp" %>
