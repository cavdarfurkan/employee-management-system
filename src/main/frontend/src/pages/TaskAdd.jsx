import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Button from "@mui/material/Button";

import dayjs from "dayjs";
import { useState } from "react";
import TaskForm from "../components/forms/TaskForm";
import TaskService from "../services/taskService";
import { useLoaderData } from "react-router-dom";
import { extractEmployeeId } from "../utils/jwtUtils";

const TaskAdd = () => {
  const currentUserRoles = useLoaderData();

  const [task, setTask] = useState({
    title: "",
    description: "",
    dueDate: dayjs(),
    employee: { id: extractEmployeeId() },
  });

  const handleSubmit = (event) => {
    event.preventDefault();

    TaskService.createNewTask(task);
  };

  return (
    <>
      <Container fluid maxWidth="false" component="main">
        <CssBaseline />
        <Box component="form" onSubmit={handleSubmit} noValidate>
          <TaskForm
            task={task}
            setTask={setTask}
            currentUserRoles={currentUserRoles}
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

export default TaskAdd;
