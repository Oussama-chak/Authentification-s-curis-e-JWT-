# TYBSync Secure Authentication (JWT)

This project implements a **fully secured JWT-based authentication system** for the TYBSync application, following all modern web security standards.

## Features

- **User Registration** (`/signup`)
- **User Login** (`/signin`)
- **JWT Token Management** via secure **HTTP-only cookies**
- **Backend Route Protection** based on JWT validity
- **SecurityContextHolder** integration for Spring Security authentication
- **Token Validation and Refresh** mechanisms

---

## How Authentication Works

### 1. User Sign-Up

- The user sends their registration data to `/api/v1/auth/signup`.
- The server:
  - Creates a new user.
  - Issues a JWT token immediately.
  - Stores the JWT securely in an **HTTP-only cookie**.
- The token is not accessible by client-side JavaScript, preventing XSS attacks.

---

### 2. User Sign-In

- The user sends login credentials to `/api/v1/auth/signin`.
- The server:
  - Authenticates the user.
  - Generates a **JWT token**.
  - Stores it in an **HTTP-only cookie** and sets necessary headers:
    - `Authorization: Bearer <token>`
    - `Access-Control-Expose-Headers: Authorization`
- The response body may contain additional useful data like user ID and role.

---

### 3. Token Extraction (Backend)

- Every incoming request goes through `JwtAuthenticationFilter.java`.
- The filter:
  - Retrieves the JWT token from the request's cookies.
  - Validates the token.
  - If valid, loads the user and injects the authentication details into `SecurityContextHolder`.

✅ If the token is invalid or expired, access is denied.

---

## Important Files and Their Roles

### `JwtAuthenticationFilter.java`

- **Intercepts all incoming HTTP requests**.
- **Extracts JWT** from cookies.
- **Validates token** and **sets authentication** for the user.
- Ensures backend routes are protected and only accessible if the token is valid.

### `AuthenticationController.java`

- Provides REST API endpoints:
  - `/signup` — register and get token.
  - `/signin` — login and receive token.
- After login/signup, **injects Authorization header** in the response for easier client use.

---

## Security Highlights

- **HTTP-only cookies** are used for storing JWT tokens securely (prevents JavaScript from stealing the token).
- **Token validation** is mandatory before accessing any protected resource.
- **Spring Security context** is populated correctly using `SecurityContextHolder`.
- **CORS headers** properly configured to expose Authorization header when needed.
- **Clean and layered code structure** (DTOs, Services, Controllers, Filters).

---

## How It Guarantees Secure Access

- Cookies are not readable via JavaScript (XSS protection).
- No sensitive token data is exposed in localStorage or sessionStorage (safer).
- Backend ensures that every request is authenticated by extracting and validating JWT.
- Only authenticated users can access protected endpoints.

---

## Quick Test Flow

1. POST to `/api/v1/auth/signup` → register a new user → receive token in cookie.
2. POST to `/api/v1/auth/signin` → login with credentials → receive token in cookie and Authorization header.
3. Access a protected route → token is extracted and verified automatically.

##  Demo
<div align="center">
  <img src="src/assets/Capture d'écran 2025-04-27 065613.png" alt="Image 1" />
</div>
<div align="center">
  <img src="src/assets/Capture d'écran 2025-04-27 065642.png" alt="Image 2" />
</div>
<div align="center">
  <img src="src/assets/Capture d'écran 2025-04-27 065711.png" alt="Image 3" />
</div>
<div align="center">
  <img src="src/assets/Capture d'écran 2025-04-27 065753.png" alt="Image 4" />
</div>
<div align="center">
  <img src="src/assets/Capture d'écran 2025-04-27 065812.png" alt="Image 5" />
</div>

#  Project Ready for Production with Secure Authentication!

