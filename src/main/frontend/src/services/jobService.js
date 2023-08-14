import api from "./api";

const BASE = "/jobs";

const getAllJobs = async () => {
  return await api.get(BASE + "/all");
};

const getAllJobsPaged = async (page = 0, size = 10, search) => {
  return await api.get(BASE + "/all-paged", {
    params: { page: page, size: size, search: search },
  });
};

const getOneJob = async (id) => {
  return await api.get(BASE + "/" + id);
};

const deleteJob = async (id) => {
  return await api.delete(BASE + "/" + id);
};

const createNewJob = async (job) => {
  return await api.post(BASE, job);
};

const updateJob = async (id, job) => {
  return await api.put(BASE + "/" + id, job);
};

const JobService = {
  getAllJobs,
  getAllJobsPaged,
  getOneJob,
  deleteJob,
  createNewJob,
  updateJob,
};

export default JobService;
