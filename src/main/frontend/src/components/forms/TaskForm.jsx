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

import PropTypes from "prop-types";
import EmployeeService from "../../services/employeeService";

import { extractEmployeeId } from "../../utils/jwtUtils";
import { ROLE_ADMIN, ROLE_MANAGER } from "../..//utils/constants";

const TaskForm = ({ task, setTask, currentUserRoles }) => {
  const employeeId = extractEmployeeId();

  const [allEmployees, setAllEmployees] = useState([]);

  useEffect(() => {
    const fetchAllEmployees = async (fetchFunc) => {
      await fetchFunc.then((response) => {
        if (response.data._embedded) {
          const parsedData = response.data._embedded.employeeList.map(
            (employee) => ({
              id: employee.id,
              name: employee.firstName + " " + employee.lastName,
            })
          );

          setAllEmployees(parsedData);
        }
      });
    };

    const fetchManager = async () => {
      await EmployeeService.getOneEmployee(employeeId).then((response) => {
        setAllEmployees((prev) => [
          {
            id: response.data.id,
            name: response.data.firstName + " " + response.data.lastName,
          },
          ...prev,
        ]);
      });
    };

    if (currentUserRoles.includes(ROLE_ADMIN)) {
      fetchAllEmployees(EmployeeService.getAllEmployees());
    } else if (currentUserRoles.includes(ROLE_MANAGER)) {
      fetchAllEmployees(
        EmployeeService.getAllEmployeesByManager(employeeId)
      ).then(() => fetchManager());
    } else {
      fetchAllEmployees(EmployeeService.getAllEmployees());
    }
  }, []);

  const handleTaskChange = (key, value) => {
    setTask((prevState) => ({
      ...prevState,
      [key]: value,
    }));
  };

  return (
    <>
      <Typography variant="h5" gutterBottom>
        Task
        <Divider />
      </Typography>
      <Grid container spacing={3} marginBottom={3}>
        <Grid item>
          <InputLabel htmlFor="title-textfield">Title</InputLabel>
          <TextField
            size="small"
            id="title-textfield"
            value={task.title}
            onChange={(e) => handleTaskChange("title", e.target.value)}
          />
        </Grid>
        <Grid item>
          <InputLabel htmlFor="description-textfield">Description</InputLabel>
          <TextField
            size="small"
            id="description-textfield"
            value={task.description}
            onChange={(e) => handleTaskChange("description", e.target.value)}
          />
        </Grid>

        <Grid item>
          <InputLabel>Due Date</InputLabel>
          <DatePicker
            slotProps={{ textField: { size: "small" } }}
            disablePast
            value={dayjs(task.dueDate)}
            onChange={(e) =>
              handleTaskChange("dueDate", e?.format("YYYY-MM-DD"))
            }
          />
        </Grid>

        <Grid item>
          <InputLabel id="employee-select-label">Employee</InputLabel>
          <Select
            size="small"
            labelId="employee-select-label"
            id="employee-select"
            defaultValue={task.employee.id}
            label="Employee"
            onChange={(e) =>
              handleTaskChange("employee", { id: e.target.value })
            }
          >
            {allEmployees.map((employee) => (
              <MenuItem key={employee.id} value={employee.id}>
                {employee.name}
              </MenuItem>
            ))}
          </Select>
        </Grid>
      </Grid>
    </>
  );
};

TaskForm.propTypes = {
  task: PropTypes.object.isRequired,
  setTask: PropTypes.func.isRequired,
  currentUserRoles: PropTypes.array.isRequired,
};

export default TaskForm;
