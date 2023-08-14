import CancelIcon from "@mui/icons-material/CancelOutlined";
import StartIcon from "@mui/icons-material/PlayCircleOutlineOutlined";
import CompleteIcon from "@mui/icons-material/CheckCircleOutlined";

import { useMemo, useState } from "react";

import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardHeader from "@mui/material/CardHeader";

import TaskService from "../../services/taskService";
import { extractEmployeeId } from "../../utils/jwtUtils";
import {
  ButtonGroup,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableRow,
} from "@mui/material";

const TasksWidget = () => {
  const employeeId = extractEmployeeId();

  const [tasks, setTasks] = useState([]);
  const [statusChanged, setStatusChanged] = useState(false);

  // eslint-disable-next-line no-unused-vars
  const tasksMemo = useMemo(() => {
    const fetchTasksOfEmployee = async () => {
      await TaskService.getAllTasksOfEmployee(employeeId).then((response) => {
        if (response.data._embedded) {
          const filteredTasks = response.data._embedded.taskList.filter(
            (task) => {
              if (
                task.status.name === "COMPLETED" ||
                task.status.name === "CANCELED"
              ) {
                return false;
              }
              return true;
            }
          );
          setTasks(filteredTasks);
        } else {
          setTasks([]);
        }
      });
    };

    fetchTasksOfEmployee();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [employeeId, statusChanged]);

  return (
    <>
      <Card
        variant="elevation"
        sx={{ maxHeight: "300px", maxWidth: "500px", overflow: "auto" }}
      >
        <CardHeader title="Tasks" />
        <CardContent>
          <Table>
            <TableBody>
              {tasks.map((task) => (
                <TableRow key={task.id}>
                  <TableCell>{task.title}</TableCell>
                  <TableCell>
                    <ButtonGroup variant="outlined">
                      {(task.status.name === "PENDING" ||
                        task.status.name === "IN_PROGRESS") && (
                        <IconButton
                          color="error"
                          onClick={async () =>
                            await TaskService.cancelTask(task.id).then(() =>
                              setStatusChanged(!statusChanged)
                            )
                          }
                        >
                          <CancelIcon />
                        </IconButton>
                      )}
                      {task.status.name === "PENDING" && (
                        <IconButton
                          color="primary"
                          onClick={async () =>
                            await TaskService.startTask(task.id).then(() =>
                              setStatusChanged(!statusChanged)
                            )
                          }
                        >
                          <StartIcon />
                        </IconButton>
                      )}
                      {task.status.name === "IN_PROGRESS" && (
                        <IconButton
                          color="success"
                          onClick={async () =>
                            await TaskService.completeTask(task.id).then(() =>
                              setStatusChanged(!statusChanged)
                            )
                          }
                        >
                          <CompleteIcon />
                        </IconButton>
                      )}
                    </ButtonGroup>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </>
  );
};

export default TasksWidget;
