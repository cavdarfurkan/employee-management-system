import { useEffect } from "react";
import AuthService from "../services/authService";

import { useNavigate } from "react-router-dom";

const LogoutPage = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const currentUser = AuthService.getCurrentUser();

    const logout = async () => {
      if (currentUser) {
        return await AuthService.logout();
      }
    };

    logout().then(() => {
      navigate("/login");
    });
  }, [navigate]);
};

export default LogoutPage;
