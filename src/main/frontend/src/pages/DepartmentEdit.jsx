import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Button from "@mui/material/Button";

import DepartmentForm from "../components/forms/DepartmentForm";

import DepartmentService from "../services/departmentService";

const DepartmentEdit = () => {
  const params = useParams();

  const [department, setDepartment] = useState({});

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      await DepartmentService.getOneDepartment(params.id).then((response) => {
        setDepartment(response.data);
      });
    };

    fetchData().then(() => setLoading(false));
  }, [params.id]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    await DepartmentService.updateDepartment(
      department.id.toString(),
      department
    );
  };

  if (loading) {
    return "Loading...";
  }

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
export default DepartmentEdit;
