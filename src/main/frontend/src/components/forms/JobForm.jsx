import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import InputLabel from "@mui/material/InputLabel";
import TextField from "@mui/material/TextField";

import PropTypes from "prop-types";

const JobForm = ({ job, setJob }) => {
  const handleJobChange = (key, value) => {
    setJob((prevState) => ({
      ...prevState,
      [key]: value,
    }));
  };

  return (
    <>
      <Typography variant="h5" gutterBottom>
        Job
      </Typography>
      <Grid container spacing={3} marginBottom={3}>
        <Grid item>
          <InputLabel htmlFor="jobname-textfield">Job Name</InputLabel>
          <TextField
            size="small"
            id="jobname-textfield"
            value={job.name}
            onChange={(e) => handleJobChange("name", e.target.value)}
          />
        </Grid>
        <Grid item>
          <InputLabel htmlFor="minsalary-textfield">Minimum Salary</InputLabel>
          <TextField
            size="small"
            id="minsalary-textfield"
            type="number"
            inputProps={{
              type: "number",
              pattern: "[0-9]*",
            }}
            value={job.minSalary}
            defaultChecked
            onChange={(e) => handleJobChange("minSalary", e.target.value)}
          />
        </Grid>
        <Grid item>
          <InputLabel htmlFor="maxsalary-textfield">Maximum Salary</InputLabel>
          <TextField
            size="small"
            id="maxsalary-textfield"
            type="number"
            inputProps={{
              type: "number",
              pattern: "[0-9]*",
            }}
            value={job.maxSalary}
            defaultChecked
            onChange={(e) => handleJobChange("maxSalary", e.target.value)}
          />
        </Grid>
      </Grid>
    </>
  );
};

JobForm.propTypes = {
  job: PropTypes.object.isRequired,
  setJob: PropTypes.func.isRequired,
};

export default JobForm;
