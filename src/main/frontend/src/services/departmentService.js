import api from "./api";

const BASE = "/departments";

const getAllDepartments = async () => {
  return await api.get(BASE + "/all");
};

const getAllDepartmentsPaged = async (page = 0, size = 10, search) => {
  return await api.get(BASE + "/all-paged", {
    params: { page: page, size: size, search: search },
  });
};

const getOneDepartment = async (id) => {
  return await api.get(BASE + "/" + id);
};

const deleteDepartment = async (id) => {
  return await api.delete(BASE + "/" + id);
};

const createNewDepartment = async (department) => {
  return await api.post(BASE, department);
};

const updateDepartment = async (id, department) => {
  return await api.put(BASE + "/" + id, department);
};

const DepartmentService = {
  getAllDepartments,
  getAllDepartmentsPaged,
  getOneDepartment,
  deleteDepartment,
  createNewDepartment,
  updateDepartment,
};

export default DepartmentService;
