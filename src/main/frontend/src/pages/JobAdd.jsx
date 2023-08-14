import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Button from "@mui/material/Button";

import { useState } from "react";

import JobForm from "../components/forms/JobForm";

import JobService from "../services/jobService";

const JobAdd = () => {
  const [job, setJob] = useState({
    name: "",
    minSalary: 0,
    maxSalary: 0,
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    JobService.createNewJob(job);
  };

  return (
    <>
      <Container fluid component="main">
        <CssBaseline />
        <Box component="form" onSubmit={handleSubmit} noValidate>
          <JobForm job={job} setJob={setJob} />
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

export default JobAdd;
