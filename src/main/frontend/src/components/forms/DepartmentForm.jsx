import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import InputLabel from "@mui/material/InputLabel";
import TextField from "@mui/material/TextField";

import PropTypes from "prop-types";

const DepartmentForm = ({ department, setDepartment }) => {
  return (
    <>
      <Typography variant="h5" gutterBottom>
        Department
      </Typography>
      <Grid container spacing={3} marginBottom={3}>
        <Grid item>
          <InputLabel htmlFor="departmentname-textfield">
            Department Name
          </InputLabel>
          <TextField
            size="small"
            id="departmentname-textfield"
            value={department.name}
            onChange={(e) =>
              setDepartment((prevState) => ({
                ...prevState,
                name: e.target.value,
              }))
            }
          />
        </Grid>
      </Grid>
    </>
  );
};

DepartmentForm.propTypes = {
  department: PropTypes.object.isRequired,
  setDepartment: PropTypes.func.isRequired,
};

export default DepartmentForm;
