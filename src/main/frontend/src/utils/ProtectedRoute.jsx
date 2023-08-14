import { Navigate, useNavigate } from "react-router-dom";
import AuthService from "../services/authService";

import PropTypes from "prop-types";
import { useEffect } from "react";

function ProtectedRoute({ roles, children }) {
  const currentUser = AuthService.getCurrentUser();
  const navigate = useNavigate();

  useEffect(() => {
    const logoutListener = (event) => {
      navigate(event.detail);
    };

    window.addEventListener("logout", logoutListener);

    return () => {
      window.removeEventListener("logout", logoutListener);
    };
  }, [navigate]);

  if (!currentUser) {
    return <Navigate to={"/login"} />;
  }

  const currentUserRoles = currentUser.user.authorities.map(
    (authority) => authority.authority
  );

  if (!roles.some((role) => currentUserRoles.includes(role))) {
    throw new Error("Unauthorized access!");
  }

  return children;
}

ProtectedRoute.propTypes = {
  roles: PropTypes.array.isRequired,
  children: PropTypes.element.isRequired,
};

export default ProtectedRoute;
