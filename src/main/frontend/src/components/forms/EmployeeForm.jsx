import { useEffect, useState } from "react";

import Divider from "@mui/material/Divider";
import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";

import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import dayjs from "dayjs";
import { MuiTelInput } from "mui-tel-input";

import EmployeeService from "../../services/employeeService";

import PropTypes from "prop-types";

const EmployeeForm = ({ employee, setEmployee }) => {
  const [managers, setManagers] = useState([]);

  useEffect(() => {
    const fetchManagersExcept = async () => {
      await EmployeeService.getAllManagersExcept(employee.id).then(
        (response) => {
          if (response.data._embedded) {
            setManagers(response.data._embedded.employeeList);
          }
        }
      );
    };

    const fetchManagers = async () => {
      await EmployeeService.getAllManagers().then((response) => {
        if (response.data._embedded) {
          setManagers(response.data._embedded.employeeList);
        }
      });
    };

    if (employee.id) {
      fetchManagersExcept();
    } else {
      fetchManagers();
    }
  }, []);

  const handleEmployeeChange = (key, value) => {
    setEmployee((prevState) => ({
      ...prevState,
      [key]: value,
    }));
  };

  return (
    <>
      <Typography variant="h5" gutterBottom>
        Employee
        <Divider />
      </Typography>
      <Grid container spacing={3} marginBottom={3}>
        <Grid item>
          <InputLabel htmlFor="firstname-textfield">First Name</InputLabel>
          <TextField
            size="small"
            id="firstname-textfield"
            value={employee.firstName}
            onChange={(e) => handleEmployeeChange("firstName", e.target.value)}
          />
        </Grid>
        <Grid item>
          <InputLabel htmlFor="lastname-textfield">Last Name</InputLabel>
          <TextField
            size="small"
            id="lastname-textfield"
            value={employee.lastName}
            onChange={(e) => handleEmployeeChange("lastName", e.target.value)}
          />
        </Grid>
        <Grid item>
          <InputLabel id="gender-select-label">Gender</InputLabel>
          <Select
            size="small"
            labelId="gender-select-label"
            id="gender-select"
            value={employee.gender}
            label="Gender"
            onChange={(e) => handleEmployeeChange("gender", e.target.value)}
          >
            <MenuItem value={"MALE"}>Male</MenuItem>
            <MenuItem value={"FEMALE"}>Female</MenuItem>
          </Select>
        </Grid>
        <Grid item>
          <InputLabel>Date of Birth</InputLabel>
          <DatePicker
            slotProps={{ textField: { size: "small" } }}
            disableFuture
            value={dayjs(employee.dateOfBirth)}
            onChange={(e) =>
              handleEmployeeChange("dateOfBirth", e?.format("YYYY-MM-DD"))
            }
          />
        </Grid>
        <Grid item>
          <InputLabel>Phone Number</InputLabel>
          <MuiTelInput
            size="small"
            value={employee.phoneNumber}
            onChange={(e) => handleEmployeeChange("phoneNumber", e)}
          />
        </Grid>
        <Grid item>
          <InputLabel htmlFor="address-textfield">Address</InputLabel>
          <TextField
            size="small"
            id="address-textfield"
            multiline
            maxRows={4}
            value={employee.address}
            onChange={(e) => handleEmployeeChange("address", e.target.value)}
          />
        </Grid>
        <Grid item>
          <InputLabel>Hire Date</InputLabel>
          <DatePicker
            slotProps={{ textField: { size: "small" } }}
            disableFuture
            value={dayjs(employee.hireDate)}
            onChange={(e) =>
              handleEmployeeChange("hireDate", e?.format("YYYY-MM-DD"))
            }
          />
        </Grid>
        <Grid item>
          <InputLabel id="manager-select-label">Manager</InputLabel>
          <Select
            size="small"
            labelId="manager-select-label"
            id="manager-select"
            defaultValue={employee.manager === null ? -1 : employee.manager.id}
            label="Manager"
            onChange={(e) =>
              handleEmployeeChange(
                "manager",
                e.target.value === -1
                  ? null
                  : managers.find((manager) => manager.id === e.target.value)
              )
            }
          >
            <MenuItem value={-1}>No manager</MenuItem>
            {managers.map((manager) => (
              <MenuItem key={manager.id} value={manager.id}>
                {manager.firstName + " " + manager.lastName}
              </MenuItem>
            ))}
          </Select>
        </Grid>
      </Grid>
    </>
  );
};

EmployeeForm.propTypes = {
  employee: PropTypes.object.isRequired,
  setEmployee: PropTypes.func.isRequired,
};

export default EmployeeForm;
