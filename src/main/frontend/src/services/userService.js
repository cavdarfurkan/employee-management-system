import axios from "axios";

const BASE = "/api/users";

const getRandomUsers = async () => {
  try {
    const response = await axios.get("random", { baseURL: BASE });
    return Promise.resolve(response.data);
  } catch (error) {
    return Promise.reject(error);
  }
};

const UserService = {
  getRandomUsers,
};

export default UserService;
