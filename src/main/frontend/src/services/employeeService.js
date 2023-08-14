import api from "./api";

const BASE = "/employees";

const getAllEmployees = async () => {
  return await api.get(BASE + "/all");
};

const getAllEmployeesPaged = async (page = 0, size = 10, search) => {
  return await api.get(BASE + "/all-paged", {
    params: { page: page, size: size, search: search },
  });
};

const getAllEmployeesByManager = async (id) => {
  return await api.get(BASE + "/all/managers/" + id);
};

const getOneEmployee = async (id) => {
  return await api.get(BASE + "/" + id);
};

const createNewEmployee = async (employee) => {
  return await api.post(BASE, employee);
};

const updateEmployee = async (id, employee) => {
  return await api.put(BASE + "/" + id, employee);
};

const deleteEmployee = async (id) => {
  return await api.delete(BASE + "/" + id);
};

const getAllManagers = async () => {
  return await api.get(BASE + "/managers/all");
};

const getAllManagersExcept = async (id) => {
  return await api.get(BASE + "/managers/all-except/" + id);
};

const getEmployeeCount = async () => {
  return await api.get(BASE + "/count");
};

const getEmployeeCountByManagerId = async (id) => {
  return await api.get(BASE + "/count/managers/" + id);
};

const getEmployeeByUserId = async (id) => {
  return await api.get(BASE + "/user", { params: { id: id } });
};

const EmployeeService = {
  getAllEmployees,
  getAllEmployeesPaged,
  getAllEmployeesByManager,
  getOneEmployee,
  createNewEmployee,
  updateEmployee,
  deleteEmployee,
  getAllManagers,
  getAllManagersExcept,
  getEmployeeCount,
  getEmployeeCountByManagerId,
  getEmployeeByUserId,
};

export default EmployeeService;
