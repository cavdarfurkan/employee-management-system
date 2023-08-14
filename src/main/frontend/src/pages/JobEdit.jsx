import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Button from "@mui/material/Button";

import JobForm from "../components/forms/JobForm";

import JobService from "../services/jobService";

const JobEdit = () => {
  const params = useParams();

  const [job, setJob] = useState({});

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      await JobService.getOneJob(params.id).then((response) => {
        setJob(response.data);
      });
    };

    fetchData().then(() => setLoading(false));
  }, [params.id]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    await JobService.updateJob(job.id.toString(), job);
  };

  if (loading) {
    return "Loading...";
  }

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

export default JobEdit;
