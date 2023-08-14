import jwtDecode from "jwt-decode";
import AuthService from "../services/authService";

/**
 * The function checks if a JSON Web Token (JWT) has expired by comparing its expiration time with the
 * current time.
 * @returns a boolean value. It returns `false` if the JWT (JSON Web Token) has expired, and `true` if
 * it is still valid.
 */
export function validateJwtExpiration() {
  const token = AuthService.getCurrentUser().token;
  const decodedToken = jwtDecode(token);

  const now = new Date();

  if (decodedToken.exp * 1000 < now.getTime()) {
    return false;
  }

  return true;
}

export function extractEmployeeId() {
  const token = AuthService.getCurrentUser().token;
  const decodedToken = jwtDecode(token);

  if (decodedToken.employee_id) {
    return decodedToken.employee_id;
  } else {
    return null;
  }
}
