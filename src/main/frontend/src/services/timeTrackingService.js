import api from "./api";
import { extractEmployeeId } from "../utils/jwtUtils";

const BASE = "/timetracking";

const getOneTimeTracker = async (id) => {
  return await api.get(BASE + "/" + id);
};

const getAllTimeTrackerByEmployee = async () => {
  const employeeId = extractEmployeeId();
  return await api.get(BASE + "/all/employee", { params: { id: employeeId } });
};

const getActiveTimeTracker = async () => {
  const employeeId = extractEmployeeId();
  return await api.get(BASE + "/active/employee", {
    params: { id: employeeId },
  });
};

const startTimeTracker = async () => {
  const employeeId = extractEmployeeId();
  return await api.post(BASE + "/start/employee", null, {
    params: { id: employeeId },
  });
};

const stopTimeTracker = async () => {
  const employeeId = extractEmployeeId();
  return await api.put(BASE + "/stop/employee", null, {
    params: { id: employeeId },
  });
};

const deleteTimeTracker = async (id) => {
  return await api.delete(BASE + "/" + id);
};

const TimeTrackingService = {
  getOneTimeTracker,
  getAllTimeTrackerByEmployee,
  getActiveTimeTracker,
  startTimeTracker,
  stopTimeTracker,
  deleteTimeTracker,
};

export default TimeTrackingService;
