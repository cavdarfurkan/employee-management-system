import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Button from "@mui/material/Button";

import { useState } from "react";

import DepartmentForm from "../components/forms/DepartmentForm";

import DepartmentService from "../services/departmentService";

const DepartmentAdd = () => {
  const [department, setDepartment] = useState({
    name: "",
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    DepartmentService.createNewDepartment(department);
  };

  return (
    <>
      <Container fluid component="main">
        <CssBaseline />
        <Box component="form" onSubmit={handleSubmit} noValidate>
          <DepartmentForm
            department={department}
            setDepartment={setDepartment}
          />
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

export default DepartmentAdd;
