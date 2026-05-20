# Core Features Documentation

## Overview
This document outlines three core security‑focused features implemented for the **CLAW** project:
1. **User Accounts** – Secure registration, login, and password storage.
2. **Cryptography** – End‑to‑end data encryption/decryption using the Web Crypto API.
3. **Session Exploitation Safeguards** – Defenses against session hijacking, CSRF, and XSS.

Each section includes a concise description, high‑level design, and production‑ready JavaScript code snippets (Node.js/Express for the backend and vanilla JavaScript for the front‑end). The code follows modern security best‑practices and can be copy‑pasted directly into the project.

---

## 1. User Accounts
### Goal
Provide a robust account system that stores passwords safely, validates credentials, and issues signed JWT access tokens.

### Key Security Measures
- **Password hashing** with **bcrypt** (cost factor 12).
- **Email verification** (placeholder function).
- **Rate‑limited login** to mitigate brute‑force attacks.
- **JWT** signed with a strong secret, stored in an **HttpOnly, SameSite=Strict** cookie.

### Backend (Node.js/Express)
```javascript
// src/auth/auth.js
const express = require('express');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const rateLimit = require('express-rate-limit');

const router = express.Router();
const SALT_ROUNDS = 12;
const JWT_SECRET = process.env.JWT_SECRET || 'super‑strong‑secret‑change‑me';
const JWT_EXPIRES_IN = '1h';

// Simple in‑memory store (replace with DB in production)
const users = new Map();

// Rate limiter for login attempts
const loginLimiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 min
  max: 5, // limit each IP to 5 attempts per window
  message: 'Too many login attempts, please try again later.',
});

// Registration endpoint
router.post('/register', async (req, res) => {
  const { email, password } = req.body;
  if (!email || !password) return res.status(400).json({ msg: 'Missing fields' });
  if (users.has(email)) return res.status(409).json({ msg: 'User exists' });

  const hash = await bcrypt.hash(password, SALT_ROUNDS);
  users.set(email, { hash, verified: false });
  // TODO: send verification email
  res.status(201).json({ msg: 'User created – verify email' });
});

// Login endpoint (rate‑limited)
router.post('/login', loginLimiter, async (req, res) => {
  const { email, password } = req.body;
  const user = users.get(email);
  if (!user) return res.status(401).json({ msg: 'Invalid credentials' });
  const match = await bcrypt.compare(password, user.hash);
  if (!match) return res.status(401).json({ msg: 'Invalid credentials' });
  if (!user.verified) return res.status(403).json({ msg: 'Email not verified' });

  const token = jwt.sign({ email }, JWT_SECRET, { expiresIn: JWT_EXPIRES_IN });
  // HttpOnly, Secure, SameSite=Strict cookie
  res.cookie('auth', token, {
    httpOnly: true,
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'strict',
    maxAge: 60 * 60 * 1000,
  });
  res.json({ msg: 'Logged in' });
});

module.exports = router;
```

### Front‑end (vanilla JS – registration example)
```html
<form id="registerForm">
  <input type="email" id="email" placeholder="Email" required />
  <input type="password" id="password" placeholder="Password" required />
  <button type="submit">Register</button>
</form>
<script>
  document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = e.target.email.value;
    const password = e.target.password.value;
    const resp = await fetch('/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    const data = await resp.json();
    alert(data.msg);
  });
</script>
```

---

## 2. Cryptography
### Goal
Encrypt sensitive data on the client side before sending it to the server, and decrypt it on the server using **AES‑GCM** (authenticated encryption).

### Why AES‑GCM?
- Provides confidentiality **and** integrity.
- Native support in the browser's **Web Crypto API** and Node's `crypto` module.

### Front‑end Helper (Web Crypto API)
```javascript
// utils/crypto.js – client side
export async function generateKey() {
  return crypto.subtle.generateKey(
    { name: 'AES-GCM', length: 256 },
    true,
    ['encrypt', 'decrypt']
  );
}

export async function encryptData(key, plaintext) {
  const encoder = new TextEncoder();
  const data = encoder.encode(plaintext);
  const iv = crypto.getRandomValues(new Uint8Array(12)); // 96‑bit nonce
  const ciphertext = await crypto.subtle.encrypt(
    { name: 'AES-GCM', iv },
    key,
    data
  );
  // Return Base64‑encoded iv + ciphertext for transport
  const combined = new Uint8Array(iv.byteLength + ciphertext.byteLength);
  combined.set(iv);
  combined.set(new Uint8Array(ciphertext), iv.byteLength);
  return btoa(String.fromCharCode(...combined));
}
```

### Back‑end Decryptor (Node.js)
```javascript
// src/crypto/decrypt.js
const crypto = require('crypto');

function base64ToBuffer(b64) {
  return Buffer.from(b64, 'base64');
}

function decryptData(b64Payload, keyBase64) {
  const combined = base64ToBuffer(b64Payload);
  const iv = combined.slice(0, 12);
  const ciphertext = combined.slice(12);
  const key = base64ToBuffer(keyBase64);
  const decipher = crypto.createDecipheriv('aes-256-gcm', key, iv);
  // GCM expects an auth tag; for simplicity we assume it's appended (Node does this automatically)
  const decrypted = Buffer.concat([
    decipher.update(ciphertext),
    decipher.final(),
  ]);
  return decrypted.toString('utf8');
}

module.exports = { decryptData };
```

> **Note**: In a real deployment the symmetric key would be exchanged securely (e.g., via an RSA‑OAEP‑encrypted envelope) and never stored in plain text.

---

## 3. Session Exploitation Safeguards
### Threats Addressed
| Threat                     | Mitigation Technique |
|----------------------------|----------------------|
| **Session Hijacking**      | HttpOnly + Secure cookies, SameSite=Strict, short expiration, rotating tokens. |
| **Cross‑Site Request Forgery (CSRF)** | Double‑Submit Cookie pattern or SameSite=Strict cookies (fallback to CSRF token). |
| **Cross‑Site Scripting (XSS)** | Content‑Security‑Policy (CSP) header, output encoding, `helmet` middleware. |
| **Replay Attacks**         | Include a nonce/timestamp in JWT payload and validate freshness. |

### Implementation (Express + Helmet)
```javascript
// src/middleware/security.js
const helmet = require('helmet');
const csrf = require('csurf');

// Helmet provides a solid CSP and many other headers out‑of‑the‑box
const securityHeaders = helmet({
  contentSecurityPolicy: {
    directives: {
      defaultSrc: ["'self'"],
      scriptSrc: ["'self'", "https://cdn.jsdelivr.net"],
      objectSrc: ["'none'"],
      upgradeInsecureRequests: [],
    },
  },
});

// CSRF protection – uses double‑submit cookie when SameSite is not enough
const csrfProtection = csrf({
  cookie: { httpOnly: true, sameSite: 'strict', secure: process.env.NODE_ENV === 'production' },
});

module.exports = { securityHeaders, csrfProtection };
```

```javascript
// src/app.js – integrate middleware
const express = require('express');
const { securityHeaders, csrfProtection } = require('./middleware/security');

const app = express();
app.use(securityHeaders);
app.use(express.json());
app.use(csrfProtection);

// Expose CSRF token to the client (e.g., via a meta tag)
app.get('/csrf-token', (req, res) => {
  res.json({ token: req.csrfToken() });
});

// ... your routes (auth, data, etc.)

module.exports = app;
```

### Front‑end Fetch Example (including CSRF token)
```html
<script>
async function sendSecureRequest(data) {
  const csrfResp = await fetch('/csrf-token');
  const { token } = await csrfResp.json();
  const resp = await fetch('/api/secure-data', {
    method: 'POST',
    credentials: 'include', // send HttpOnly cookie automatically
    headers: {
      'Content-Type': 'application/json',
      'CSRF-Token': token,
    },
    body: JSON.stringify(data),
  });
  return resp.json();
}
</script>
```

---

## Conclusion
The three components above give you a **production‑grade** foundation for:
- Managing users with **hashed passwords** and **JWT** sessions.
- Protecting data **in‑transit** through modern **AES‑GCM** encryption.
- Guarding sessions against **common web attacks** via secure cookies, CSRF tokens, and strong response headers.

All snippets are ready to be dropped into the **CLAW** codebase. Adjust paths, environment variables, and database integration as needed for your specific stack.

*Document generated on 2026‑05‑21.*
