import axios from "axios";
import { validateJwtExpiration } from "../utils/jwtUtils";
import AuthService from "./authService";

const instance = axios.create({
  baseURL: "/api",
  headers: { "Content-Type": "application/json" },
});

instance.interceptors.request.use(
  (config) => {
    const token = AuthService.getCurrentUser().token;
    if (validateJwtExpiration()) {
      config.headers["Authorization"] = "Bearer " + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

instance.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalConfig = error.config;

    if (error.response.status === 401 && !originalConfig._retry) {
      originalConfig._retry = true;

      try {
        const res = await AuthService.refreshToken(
          AuthService.getCurrentUser().refreshToken
        );

        if (res.status === 200) {
          return instance(originalConfig);
        }
      } catch (err) {
        const event = new CustomEvent("logout", { detail: "/logout" });
        window.dispatchEvent(event);
      }
    }

    return Promise.reject(error);
  }
);

export default instance;
