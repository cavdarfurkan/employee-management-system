import { useEffect, useState } from "react";

import Divider from "@mui/material/Divider";
import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";

import JobService from "../../services/jobService";
import DepartmentService from "../../services/departmentService";

import PropTypes from "prop-types";

const EmployeeJobForm = ({ employee, setEmployee }) => {
  const [jobs, setJobs] = useState([]);
  const [departments, setDepartments] = useState([]);

  useEffect(() => {
    const fetchJobs = async () => {
      await JobService.getAllJobs().then((response) => {
        if (response.data._embedded) {
          setJobs(response.data._embedded.jobList);
        }
      });
    };

    const fetchDepartments = async () => {
      await DepartmentService.getAllDepartments().then((response) => {
        if (response.data._embedded) {
          setDepartments(response.data._embedded.departmentList);
        }
      });
    };

    fetchJobs();
    fetchDepartments();
  }, []);

  const handleEmployeeJobChange = (key, value) => {
    setEmployee((prevState) => ({
      ...prevState,
      [key]: value,
    }));
  };

  return (
    <>
      <Typography variant="h5" gutterBottom>
        Job
        <Divider />
      </Typography>
      <Grid container spacing={3} marginBottom={3}>
        <Grid item>
          <InputLabel id="department-select-label">Department</InputLabel>
          <Select
            size="small"
            labelId="department-select-label"
            id="department-select"
            defaultValue={
              employee.department === null ? -1 : employee.department.id
            }
            label="Department"
            onChange={(e) =>
              handleEmployeeJobChange(
                "department",
                e.target.value === -1
                  ? null
                  : departments.find(
                      (department) => department.id === e.target.value
                    )
              )
            }
          >
            <MenuItem value={-1}>No department</MenuItem>
            {departments.map((department) => (
              <MenuItem key={department.id} value={department.id}>
                {department.name}
              </MenuItem>
            ))}
          </Select>
        </Grid>
        <Grid item>
          <InputLabel id="job-select-label">Job</InputLabel>
          <Select
            size="small"
            labelId="job-select-label"
            id="job-select"
            defaultValue={employee.job === null ? -1 : employee.job.id}
            label="Job"
            onChange={(e) =>
              handleEmployeeJobChange(
                "job",
                e.target.value === -1
                  ? null
                  : jobs.find((job) => job.id === e.target.value)
              )
            }
          >
            <MenuItem value={-1}>No job</MenuItem>
            {jobs.map((job) => (
              <MenuItem key={job.id} value={job.id}>
                {job.name}
              </MenuItem>
            ))}
          </Select>
        </Grid>
        <Grid item>
          <InputLabel htmlFor="salary-textfield">Salary</InputLabel>
          <TextField
            size="small"
            id="salary-textfield"
            type="number"
            inputProps={{
              type: "number",
              min: employee.job ? employee.job.minSalary : 0,
              max: employee.job ? employee.job.maxSalary : 0,
              pattern: "[0-9]*",
            }}
            value={employee.salary}
            defaultChecked
            onChange={(e) => handleEmployeeJobChange("salary", e.target.value)}
          />
        </Grid>
      </Grid>
    </>
  );
};

EmployeeJobForm.propTypes = {
  employee: PropTypes.object.isRequired,
  setEmployee: PropTypes.func.isRequired,
};

export default EmployeeJobForm;
