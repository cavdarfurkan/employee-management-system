import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Button from "@mui/material/Button";
import TaskForm from "../components/forms/TaskForm";
import { useEffect, useState } from "react";
import TaskService from "../services/taskService";
import { useLoaderData, useParams } from "react-router-dom";

const TaskEdit = () => {
  const params = useParams();
  const currentUserRoles = useLoaderData();

  const [task, setTask] = useState({});

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchTask = async () => {
      await TaskService.getOneTask(params.id).then((response) =>
        setTask(response.data)
      );
    };

    fetchTask().then(() => setLoading(false));
  }, [params.id]);

  if (loading) {
    return "Loading...";
  }

  const handleSubmit = (event) => {
    event.preventDefault();

    TaskService.updateTask(params.id, task);
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

export default TaskEdit;
