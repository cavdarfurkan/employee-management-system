import EditIcon from "@mui/icons-material/Edit";
import AddIcon from "@mui/icons-material/Add";
import CancelIcon from "@mui/icons-material/CancelOutlined";
import StartIcon from "@mui/icons-material/PlayCircleOutlineOutlined";
import CompleteIcon from "@mui/icons-material/CheckCircleOutlined";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useLoaderData, useNavigate } from "react-router-dom";

import {
  Container,
  CssBaseline,
  Button,
  Select,
  MenuItem,
  InputLabel,
  Checkbox,
  IconButton,
  ButtonGroup,
  Typography,
  Fab,
} from "@mui/material";

import DataTable from "react-data-table-component";
import EmployeeService from "../services/employeeService";

import { extractEmployeeId } from "../utils/jwtUtils";
import { ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_MANAGER } from "../utils/constants";

import TaskService from "../services/taskService";

const Task = () => {
  const navigate = useNavigate();
  const currentUserRoles = useLoaderData();
  const employeeId = extractEmployeeId();

  const [selectedEmployeeId, setSelectedEmployeeId] = useState(employeeId || 1);

  const [selectedRows, setSelectedRows] = useState([]);
  const [toggleClearedRows, setToggleClearedRows] = useState(false);
  const [statusChanged, setStatusChanged] = useState(false);

  const [allEmployees, setAllEmployees] = useState([]);
  const [tasks, setTasks] = useState([]);

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
  }, [currentUserRoles, employeeId]);

  const handleRowSelected = useCallback((state) => {
    setSelectedRows(state.selectedRows);
  }, []);

  const tasksMemo = useMemo(() => {
    const fetchTasksOfEmployee = async () => {
      await TaskService.getAllTasksOfEmployee(selectedEmployeeId).then(
        (response) => {
          if (response.data._embedded) {
            setTasks(response.data._embedded.taskList);
          } else {
            setTasks([]);
          }
        }
      );
    };

    fetchTasksOfEmployee();
  }, [selectedEmployeeId, toggleClearedRows, statusChanged]);

  const contextAction = useMemo(() => {
    const handleDelete = async () => {
      if (window.confirm("Are you sure you want to delete?")) {
        await Promise.all(
          selectedRows.map((row) => TaskService.deleteTask(row.id))
        );
        setToggleClearedRows(!toggleClearedRows);
      }
    };

    return (
      <>
        <Button
          key="delete"
          onClick={handleDelete}
          style={{ backgroundColor: "red", color: "white" }}
        >
          Delete
        </Button>
      </>
    );
  }, [selectedRows, toggleClearedRows]);

  const columns = [
    { name: "ID", selector: (row) => row.id, sortable: true },
    { name: "Title", selector: (row) => row.title, wrap: true },
    {
      name: "Assignment Date",
      selector: (row) => new Date(row.assignmentDate).toLocaleDateString(),
      center: true,
    },
    {
      name: "Due Date",
      selector: (row) =>
        row.dueDate && new Date(row.dueDate).toLocaleDateString(),
      center: true,
    },
    {
      name: "Status",
      selector: (row) => row.status.name,
      sortable: true,
      center: true,
    },
    (currentUserRoles.includes(ROLE_ADMIN) ||
      currentUserRoles.includes(ROLE_MANAGER)) && {
      cell: (row) => (
        <Button
          color="primary"
          onClick={() => navigate(`/tasks/${row.id}`)}
          startIcon={<EditIcon />}
        >
          Edit
        </Button>
      ),
      center: true,
    },
    (currentUserRoles.includes(ROLE_EMPLOYEE) ||
      currentUserRoles.includes(ROLE_MANAGER)) &&
      employeeId === selectedEmployeeId && {
        cell: (row) => (
          <ButtonGroup variant="outlined">
            {(row.status.name === "PENDING" ||
              row.status.name === "IN_PROGRESS") && (
              <IconButton
                color="error"
                onClick={async () =>
                  await TaskService.cancelTask(row.id).then(() =>
                    setStatusChanged(!statusChanged)
                  )
                }
              >
                <CancelIcon />
              </IconButton>
            )}
            {row.status.name === "PENDING" && (
              <IconButton
                color="primary"
                onClick={async () =>
                  await TaskService.startTask(row.id).then(() =>
                    setStatusChanged(!statusChanged)
                  )
                }
              >
                <StartIcon />
              </IconButton>
            )}
            {row.status.name === "IN_PROGRESS" && (
              <IconButton
                color="success"
                onClick={async () =>
                  await TaskService.completeTask(row.id).then(() =>
                    setStatusChanged(!statusChanged)
                  )
                }
              >
                <CompleteIcon />
              </IconButton>
            )}
          </ButtonGroup>
        ),
        center: true,
      },
  ];

  const ExpandedTaskRow = (row) => {
    return (
      <>
        {row.data.description && (
          <Typography variant="body2" p={2} sx={{ wordBreak: "break-word" }}>
            {row.data.description}
          </Typography>
        )}
      </>
    );
  };

  return (
    <>
      <Container
        fluid
        maxWidth="false"
        component="main"
        sx={{ position: "relative" }}
      >
        <CssBaseline />
        <InputLabel id="employee-select-label">Employee</InputLabel>
        <Select
          fullWidth
          size="medium"
          labelId="employee-select-label"
          id="employee-select"
          value={selectedEmployeeId}
          label="Employee"
          disabled={
            !currentUserRoles.includes(ROLE_MANAGER) &&
            !currentUserRoles.includes(ROLE_ADMIN)
          }
          onChange={(e) => {
            setSelectedEmployeeId(e.target.value);
          }}
        >
          {allEmployees.map((employee) => (
            <MenuItem key={employee.id} value={employee.id}>
              {employee.name}
            </MenuItem>
          ))}
        </Select>

        {/* <div style={{ maxWidth: "500px" }}> */}
        <DataTable
          title="Tasks"
          columns={columns}
          data={tasks}
          persistTableHead
          selectableRows
          selectableRowsComponent={Checkbox}
          onSelectedRowsChange={handleRowSelected}
          clearSelectedRows={toggleClearedRows}
          contextActions={contextAction}
          expandableRows
          expandableRowsHideExpander
          expandOnRowClicked
          expandableRowsComponent={ExpandedTaskRow}
        />
        {/* </div> */}

        {(currentUserRoles.includes(ROLE_ADMIN) ||
          currentUserRoles.includes(ROLE_MANAGER)) && (
          <Fab
            color="primary"
            aria-label="add"
            onClick={() => navigate("/tasks/add")}
            sx={{ position: "fixed", bottom: 16, right: 16 }}
          >
            <AddIcon />
          </Fab>
        )}
      </Container>
    </>
  );
};

export default Task;
