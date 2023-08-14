import api from "./api";
import { extractEmployeeId } from "../utils/jwtUtils";

const BASE = "/tasks";

const getAllTasks = async () => {
  return await api.get(BASE + "/all");
};

const getOneTask = async (id) => {
  return await api.get(BASE + "/" + id);
};

const getAllTasksOfEmployee = async (employeeId = extractEmployeeId()) => {
  return await api.get(BASE + "/all/employee", { params: { id: employeeId } });
};

const getTaskCountOfEmployee = async () => {
  const employeeId = extractEmployeeId();
  return await api.get(BASE + "/count/employee", {
    params: { id: employeeId },
  });
};

const createNewTask = async (task) => {
  return await api.post(BASE, task);
};

const updateTask = async (id, task) => {
  return await api.put(BASE + "/" + id, task);
};

const deleteTask = async (id) => {
  return await api.delete(BASE + "/" + id);
};

const startTask = async (id) => {
  return await api.put(BASE + "/start/" + id);
};

const completeTask = async (id) => {
  return await api.put(BASE + "/complete/" + id);
};

const cancelTask = async (id) => {
  return await api.put(BASE + "/cancel/" + id);
};

const TaskService = {
  getAllTasks,
  getOneTask,
  getAllTasksOfEmployee,
  getTaskCountOfEmployee,
  createNewTask,
  updateTask,
  deleteTask,
  startTask,
  completeTask,
  cancelTask,
};

export default TaskService;
