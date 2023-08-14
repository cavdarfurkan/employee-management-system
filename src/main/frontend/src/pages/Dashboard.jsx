import Grid from "@mui/material/Grid";

import { useState } from "react";
import { useLoaderData } from "react-router-dom";

import EmployeeBoard from "../components/boards/EmployeeBoard";
import ManagerBoard from "../components/boards/ManagerBoard";
import AdminBoard from "../components/boards/AdminBoard";

import { ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_MANAGER } from "../utils/constants";

const Dashboard = () => {
  const currentUserRoles = useLoaderData();
  // const navigate = useNavigate();

  // const [user, setUser] = useState({});
  // const [userRoles, setUserRoles] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  // useEffect(() => {
  //   const currentUser = AuthService.getCurrentUser();

  //     setIsLoading(false);
  //   }
  // }, []);

  if (isLoading) {
    return (
      <>
        <h1>Loading...</h1>
      </>
    );
  }

  return (
    <>
      <Grid container spacing={5}>
        {currentUserRoles.includes(ROLE_ADMIN) && (
          <Grid item>
            <AdminBoard />
          </Grid>
        )}
        {currentUserRoles.includes(ROLE_MANAGER) && (
          <Grid item>
            <ManagerBoard />
          </Grid>
        )}
        {currentUserRoles.includes(ROLE_EMPLOYEE) && (
          <Grid item>
            <EmployeeBoard />
          </Grid>
        )}
      </Grid>
    </>
  );
};

export default Dashboard;
