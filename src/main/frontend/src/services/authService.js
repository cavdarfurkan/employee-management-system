import axios from "axios";
import api from "./api";

const BASE = "/api/auth";

const register = async (username, email, password) => {
  return await axios.post(
    "register",
    { username: username, email: email, password: password },
    { baseURL: BASE, headers: { "Content-Type": "application/json" } }
  );
};

const login = async (username, password) => {
  try {
    const response = await axios.post(
      "login",
      { username: username, password: password },
      { baseURL: BASE, headers: { "Content-Type": "application/json" } }
    );

    if (response.data.token) {
      localStorage.setItem("user", JSON.stringify(response.data));
    }

    return Promise.resolve(response.data);
  } catch (error) {
    return Promise.reject(error.response.data.message);
  }
};

const logout = async () => {
  try {
    const response = await axios.post("logout", null, {
      baseURL: BASE,
      params: { id: getCurrentUser().user.id },
    });

    localStorage.removeItem("user");
    return Promise.resolve(response.data);
  } catch (error) {
    return Promise.reject(error);
  }
};

const refreshToken = async (refreshToken) => {
  try {
    const response = await axios.post(
      "refresh-token",
      { refreshToken: refreshToken },
      { baseURL: BASE }
    );

    if (response.data.token) {
      const currentUser = getCurrentUser();
      currentUser.token = response.data.token;
      localStorage.setItem("user", JSON.stringify(currentUser));
    }

    return Promise.resolve(response);
  } catch (error) {
    return Promise.reject(error);
  }
};

const getAllRoles = async () => {
  return api.get("/auth/all-roles");
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
};

const AuthService = {
  register,
  login,
  logout,
  refreshToken,
  getAllRoles,
  getCurrentUser,
};

export default AuthService;
