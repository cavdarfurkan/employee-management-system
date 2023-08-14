import React from "react";
import { createRoot } from "react-dom/client";
import { RouterProvider, createBrowserRouter } from "react-router-dom";

import "./index.css";

import MenuDrawer from "./components/MenuDrawer";
import ErrorPage from "./pages/ErrorPage";
import LoginPage from "./pages/LoginPage";
import LogoutPage from "./pages/LogoutPage";
import Dashboard from "./pages/Dashboard";
import SettingsPage from "./pages/SettingsPage";
import Employees from "./pages/Employees";
import EmployeeEdit from "./pages/EmployeeEdit";

import ProtectedRoute from "./utils/ProtectedRoute";
import { ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_MANAGER } from "./utils/constants";

import { AppContextProvider } from "./AppContext";

import AuthService from "./services/authService";

import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import EmployeeAdd from "./pages/EmployeeAdd";
import Departments from "./pages/Departments";
import Jobs from "./pages/Jobs";
import DepartmentEdit from "./pages/DepartmentEdit";
import DepartmentAdd from "./pages/DepartmentAdd";
import JobEdit from "./pages/JobEdit";
import JobAdd from "./pages/JobAdd";
import WorkTimes from "./pages/WorkTimes";
import Task from "./pages/Task";
import TaskEdit from "./pages/TaskEdit";
import TaskAdd from "./pages/TaskAdd";

const rolesLoader = async () => {
  const currentUser = AuthService.getCurrentUser();

  if (currentUser) {
    const currentUserRoles = currentUser.user.authorities.map(
      (authority) => authority.authority
    );

    return currentUserRoles;
  }

  return [];
};

const router = createBrowserRouter([
  {
    element: <MenuDrawer />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "/",
        loader: rolesLoader,
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE]}>
            <Dashboard />
          </ProtectedRoute>
        ),
      },
      {
        path: "/employees",
        loader: rolesLoader,
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN, ROLE_MANAGER]}>
            <Employees />
          </ProtectedRoute>
        ),
      },
      {
        path: "/employees/:id",
        loader: rolesLoader,
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE]}>
            <EmployeeEdit />
          </ProtectedRoute>
        ),
      },
      {
        path: "/employees/add",
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN]}>
            <EmployeeAdd />
          </ProtectedRoute>
        ),
      },
      {
        path: "/departments",
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN]}>
            <Departments />
          </ProtectedRoute>
        ),
      },
      {
        path: "/departments/:id",
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN]}>
            <DepartmentEdit />
          </ProtectedRoute>
        ),
      },
      {
        path: "/departments/add",
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN]}>
            <DepartmentAdd />
          </ProtectedRoute>
        ),
      },
      {
        path: "/jobs",
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN]}>
            <Jobs />
          </ProtectedRoute>
        ),
      },
      {
        path: "/jobs/:id",
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN]}>
            <JobEdit />
          </ProtectedRoute>
        ),
      },
      {
        path: "/jobs/add",
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN]}>
            <JobAdd />
          </ProtectedRoute>
        ),
      },
      {
        path: "/work-times",
        element: (
          <ProtectedRoute roles={[ROLE_MANAGER, ROLE_EMPLOYEE]}>
            <WorkTimes />
          </ProtectedRoute>
        ),
      },
      {
        path: "/tasks",
        loader: rolesLoader,
        element: (
          <ProtectedRoute roles={[ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE]}>
            <Task />
          </ProtectedRoute>
        ),
      },
      {
        path: "/tasks/:id",
        loader: rolesLoader,
        element: (
          <ProtectedRoute roles={[ROLE_MANAGER, ROLE_ADMIN]}>
            <TaskEdit />
          </ProtectedRoute>
        ),
      },
      {
        path: "/tasks/add",
        loader: rolesLoader,
        element: (
          <ProtectedRoute roles={[ROLE_MANAGER, ROLE_ADMIN]}>
            <TaskAdd />
          </ProtectedRoute>
        ),
      },
    ],
  },
  { path: "/login", element: <LoginPage /> },
  { path: "/logout", element: <LogoutPage /> },
  {
    path: "/settings",
    element: (
      <ProtectedRoute roles={[ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE]}>
        <SettingsPage />
      </ProtectedRoute>
    ),
  },
]);

const rootElement = document.getElementById("root");
const root = createRoot(rootElement);
root.render(
  <React.StrictMode>
    <AppContextProvider>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <RouterProvider router={router} />
      </LocalizationProvider>
    </AppContextProvider>
  </React.StrictMode>
);
