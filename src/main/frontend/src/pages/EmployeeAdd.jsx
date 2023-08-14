import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Button from "@mui/material/Button";

import EmployeeForm from "../components/forms/EmployeeForm";
// import UserForm from "../components/forms/UserForm";
import EmployeeJobForm from "../components/forms/EmployeeJobForm";

import EmployeeService from "../services/employeeService";

import { useState } from "react";
import dayjs from "dayjs";

const EmployeeAdd = () => {
  const [employee, setEmployee] = useState({
    firstName: "",
    lastName: "",
    gender: "MALE",
    dateOfBirth: dayjs(),
    address: "",
    hireDate: dayjs(),
    manager: { id: -1 },
    job: { id: -1 },
    department: { id: -1 },
  });

  const handleSubmit = (e) => {
    e.preventDefault();

    const copyEmployee = Object.assign({}, employee);

    if (employee.manager.id === -1) {
      copyEmployee.manager = null;
    }
    if (employee.job.id === -1) {
      copyEmployee.job = null;
    }
    if (employee.department.id === -1) {
      copyEmployee.department = null;
    }

    EmployeeService.createNewEmployee(copyEmployee);
  };

  return (
    <>
      <Container fluid maxWidth="false" component="main">
        <CssBaseline />
        <Box component="form" onSubmit={handleSubmit} noValidate>
          <EmployeeForm employee={employee} setEmployee={setEmployee} />
          {/* <EditUser employee={employee} setEmployee={setEmployee} /> */}
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

export default EmployeeAdd;
