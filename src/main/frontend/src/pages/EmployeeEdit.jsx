import { useEffect, useState } from "react";
import { useLoaderData, useParams } from "react-router-dom";

import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Button from "@mui/material/Button";

import EmployeeService from "../services/employeeService";

import EmployeeForm from "../components/forms/EmployeeForm";
import UserForm from "../components/forms/UserForm";
import EmployeeJobForm from "../components/forms/EmployeeJobForm";
import { ROLE_ADMIN } from "../utils/constants";

const EmployeeEdit = () => {
  const currentUserRoles = useLoaderData();
  const params = useParams();

  const [employee, setEmployee] = useState({});

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      await EmployeeService.getOneEmployee(params.id).then((response) =>
        setEmployee(response.data)
      );
    };

    fetchData().then(() => setLoading(false));
  }, [params.id]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    await EmployeeService.updateEmployee(employee.id.toString(), employee);
  };

  // eslint-disable-next-line no-unused-vars
  // const printEmployeeCallback = useMemo(() => {
  //   if (Object.keys(employee).length !== 0) {
  //     console.log(employee);
  //   }
  // }, [employee]);

  if (loading) {
    return "Loading...";
  }

  return (
    <>
      <Container fluid maxWidth="false" component="main">
        <CssBaseline />
        <Box component="form" onSubmit={handleSubmit} noValidate>
          <EmployeeForm employee={employee} setEmployee={setEmployee} />
          {currentUserRoles.includes(ROLE_ADMIN) && (
            <UserForm employee={employee} setEmployee={setEmployee} />
          )}
          <EmployeeJobForm employee={employee} setEmployee={setEmployee} />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Save
          </Button>
        </Box>
      </Container>
    </>
  );
};

export default EmployeeEdit;
