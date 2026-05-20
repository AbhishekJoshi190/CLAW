<%@ include file="components/header.jsp" %>

    <style>
        /* Container for the product image and the custom print overlay */
        .preview-container {
            position: relative;
            width: 100%;
            max-width: 500px;
            margin: 0 auto;
            overflow: hidden;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            background-color: #f5f5f5;
        }
         /* Styles the main product image */
        .main-product-img {
            width: 100%;
            display: block;
            user-select: none;
            -webkit-user-drag: none;
        }

        /* The dashed box showing where the graphic can be placed */
        .custom-overlay {
            position: absolute;
            top: 25%;
            left: 20%;
            width: 60%;
            height: 50%;
            border: 2px dashed rgba(255, 255, 255, 0.3);
            pointer-events: none;
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 5;
        }

        /* The uploaded graphic that can be dragged */
        .draggable-graphic {
            position: absolute;
            width: 100px;
            cursor: move;
            z-index: 10;
            pointer-events: auto;
            touch-action: none;
            border: 1px solid transparent;
            transition: border-color 0.2s;
        }

        .draggable-graphic:hover {
            border-color: var(--accent);
        }

        /* UI controls for zooming and resetting */
        .control-btn {
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid var(--border);
            color: white;
            padding: 0.5rem;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>

    <div class="single-product">
        <!-- LEFT COLUMN: Image & Customization Preview -->
        <div class="single-product-image">
            <div class="preview-container" id="previewArea">
                <img src="${pageContext.request.contextPath}/${product.imageUrl}" alt="${product.name}"
                    class="main-product-img" id="productImg">

                <div class="custom-overlay" id="overlayBox">
                    <span
                        style="color: rgba(255,255,255,0.5); font-size: 0.8rem; position: absolute; bottom: -20px;">PRINT
                        AREA</span>
                </div>

                <!-- This <img> will hold the user's uploaded design -->
                <img id="printPreview" class="draggable-graphic" style="display: none; top: 40%; left: 40%;">
            </div>

            <!-- Scaling and Removal controls (shown only after upload) -->
            <div id="controls" style="display: none; margin-top: 1rem; text-align: center;">
                <p style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 0.5rem;">Drag to position, or use
                    controls to scale:</p>
                <div style="display: flex; justify-content: center; gap: 1rem;">
                    <button type="button" class="control-btn" onclick="adjustScale(1.1)">Zoom In +</button>
                    <button type="button" class="control-btn" onclick="adjustScale(0.9)">Zoom Out -</button>
                    <button type="button" class="control-btn" style="color: #ff4757;"
                        onclick="resetCustom()">Remove</button>
                </div>
            </div>
        </div>

        <!-- RIGHT COLUMN: Product details and Add to Cart form -->
        <div class="single-product-details">
            <h1>${product.name}</h1>
            <div class="price">NPR ${product.price}0</div>

            <p style="color: var(--text-muted); margin-bottom: 2rem;">
                Premium streetwear item crafted for maximum comfort and style. Perfect for everyday urban wear.
            </p>

            <form action="${pageContext.request.contextPath}/cart" method="post" enctype="multipart/form-data">
                <input type="hidden" name="productId" value="${product.id}">

                <!-- SIZE SELECTION -->
                <div class="form-group" style="margin-bottom: 1rem;">
                    <label for="size" style="display:block; margin-bottom:0.5rem; color:var(--text-muted);">Size</label>
                    <select name="size" id="size" required
                        style="width: 100%; padding: 0.8rem; background-color: rgba(0,0,0,0.3); border: 1px solid #333; border-radius: 6px; color: #fff;">
                        <option value="S">Small (S)</option>
                        <option value="M" selected>Medium (M)</option>
                        <option value="L">Large (L)</option>
                        <option value="XL">Extra Large (XL)</option>
                        <option value="XXL">XXL</option>
                    </select>
                </div>

                <!-- CATEGORY-SPECIFIC FIELDS: Hips size for bottoms -->
                <c:if test="${product.category == 'Bottoms'}">
                    <div class="form-group" style="margin-bottom: 1rem;">
                        <label for="hipsSize" style="display:block; margin-bottom:0.5rem; color:var(--text-muted);">Hips
                            Size (inches)</label>
                        <input type="number" name="hipsSize" id="hipsSize" required placeholder="e.g. 32"
                            style="width: 100%; padding: 0.8rem; background-color: rgba(0,0,0,0.3); border: 1px solid #333; border-radius: 6px; color: #fff;">
                    </div>
                </c:if>

                <!-- CUSTOM DESIGN UPLOAD -->
                <div
                    style="border-top: 1px solid var(--border); padding-top: 1rem; margin-top: 1rem; margin-bottom: 1.5rem;">
                    <h3 style="margin-bottom: 1rem; font-size: 1.1rem;">Custom Printing (Optional)</h3>

                    <div class="form-group" style="margin-bottom: 1rem;">
                        <label for="frontDesign"
                            style="display:block; margin-bottom:0.5rem; color:var(--text-muted);">Upload Front
                            Design</label>
                        <input type="file" name="frontDesign" id="frontDesign" accept="image/*"
                            style="width: 100%; color: #aaa;" onchange="handleFile(this)">
                    </div>

                    <!-- HIDDEN FIELDS: Store the coordinates and scale to send to server -->
                    <input type="hidden" name="printX" id="printX" value="40">
                    <input type="hidden" name="printY" id="printY" value="40">
                    <input type="hidden" name="printScale" id="printScale" value="100">

                </div>

                <button type="submit" class="btn" style="width: 100%; font-size: 1.1rem; padding: 1rem;">Add to
                    Cart</button>
            </form>
        </div>
    </div>

    <script>
        // --- JAVASCRIPT: DRAG & DROP LOGIC ---
        const preview = document.getElementById('printPreview');
        const controls = document.getElementById('controls');
        const container = document.getElementById('previewArea');
        const xInput = document.getElementById('printX');
        const yInput = document.getElementById('printY');
        const scaleInput = document.getElementById('printScale');

        let isDragging = false;
        let startX, startY;
        let currentScale = 100;

        /**
         * Reads the uploaded file and displays it in the preview area.
         */
        function handleFile(input) {
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                    controls.style.display = 'block';
                }
                reader.readAsDataURL(input.files[0]);
            }
        }

        /**
         * Resizes the graphic when user clicks Zoom buttons.
         */
        function adjustScale(factor) {
            currentScale *= factor;
            preview.style.width = currentScale + 'px';
            scaleInput.value = Math.round(currentScale);
        }

        /**
         * Clears the customization if user changes their mind.
         */
        function resetCustom() {
            preview.style.display = 'none';
            controls.style.display = 'none';
            document.getElementById('frontDesign').value = '';
        }

        // --- MOUSE EVENTS for Desktop Dragging ---
        preview.addEventListener('mousedown', (e) => {
            isDragging = true;
            startX = e.clientX - preview.offsetLeft;
            startY = e.clientY - preview.offsetTop;
            preview.style.transition = 'none';
        });

        document.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            e.preventDefault();

            const rect = container.getBoundingClientRect();
            let x = e.clientX - rect.left - startX;
            let y = e.clientY - rect.top - startY;

            // Stay within image boundaries
            x = Math.max(0, Math.min(x, rect.width - preview.offsetWidth));
            y = Math.max(0, Math.min(y, rect.height - preview.offsetHeight));

            preview.style.left = x + 'px';
            preview.style.top = y + 'px';

            // Convert to percentage for server-side processing
            xInput.value = Math.round((x / rect.width) * 100);
            yInput.value = Math.round((y / rect.height) * 100);
        });

        document.addEventListener('mouseup', () => {
            isDragging = false;
            preview.style.transition = 'border-color 0.2s';
        });

        // --- [MOBILE ONLY] TOUCH EVENTS for Mobile Dragging ---
        // Allows users to position their design using finger dragging on mobile devices
        preview.addEventListener('touchstart', (e) => {
            isDragging = true;
            const touch = e.touches[0];
            startX = touch.clientX - preview.offsetLeft;
            startY = touch.clientY - preview.offsetTop;
        });

        document.addEventListener('touchmove', (e) => {
            if (!isDragging) return;
            // Prevent scrolling the page while dragging the graphic
            const touch = e.touches[0];
            const rect = container.getBoundingClientRect();
            let x = touch.clientX - rect.left - startX;
            let y = touch.clientY - rect.top - startY;

            x = Math.max(0, Math.min(x, rect.width - preview.offsetWidth));
            y = Math.max(0, Math.min(y, rect.height - preview.offsetHeight));

            preview.style.left = x + 'px';
            preview.style.top = y + 'px';

            // Sync with hidden fields for server submission
            xInput.value = Math.round((x / rect.width) * 100);
            yInput.value = Math.round((y / rect.height) * 100);
        });

        document.addEventListener('touchend', () => isDragging = false);
    </script>

    <%@ include file="components/footer.jsp" %>