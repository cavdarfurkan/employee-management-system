import { useEffect, useState } from "react";

import Divider from "@mui/material/Divider";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import InputLabel from "@mui/material/InputLabel";
import Typography from "@mui/material/Typography";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";

import AuthService from "../../services/authService";

import PropTypes from "prop-types";

const UserForm = ({ employee, setEmployee }) => {
  const [allAuthorities, setAllAuthorities] = useState([]);

  useEffect(() => {
    const fetchAuthorities = async () => {
      await AuthService.getAllRoles().then((response) =>
        setAllAuthorities(response.data._embedded.authorityList)
      );
    };

    fetchAuthorities();
  }, []);

  return (
    <>
      <Typography variant="h5" gutterBottom>
        User
        <Divider />
      </Typography>
      <Grid container spacing={3} marginBottom={3}>
        <Grid item>
          <InputLabel htmlFor="username-textfield">Username</InputLabel>
          <TextField
            size="small"
            id="username-textfield"
            value={employee.user.username}
            onChange={(e) =>
              setEmployee((prevState) => ({
                ...prevState,
                user: { ...prevState.user, username: e.target.value },
              }))
            }
          />
        </Grid>
        <Grid item>
          <InputLabel htmlFor="email-textfield">Email</InputLabel>
          <TextField
            size="small"
            id="email-textfield"
            value={employee.user.email}
            placeholder="user@email.com"
            type="email"
            onChange={(e) =>
              setEmployee((prevState) => ({
                ...prevState,
                user: { ...prevState.user, email: e.target.value },
              }))
            }
          />
        </Grid>
        <Grid item>
          <InputLabel id="roles-select-label">Roles</InputLabel>
          <Select
            size="small"
            labelId="roles-select-label"
            id="roles-select"
            multiple
            value={employee.user.authorities.map((authority) => authority.id)}
            label="Roles"
            onChange={(e) => {
              const selectedRoles = allAuthorities.filter((role) =>
                e.target.value.includes(role.id)
              );

              setEmployee((prevState) => ({
                ...prevState,
                user: {
                  ...prevState.user,
                  authorities: selectedRoles,
                },
              }));
            }}
          >
            {allAuthorities.map((authority) => (
              <MenuItem key={authority.id} value={authority.id}>
                {authority.authority.substring(5)}
              </MenuItem>
            ))}
          </Select>
        </Grid>
      </Grid>
    </>
  );
};

UserForm.propTypes = {
  employee: PropTypes.object.isRequired,
  setEmployee: PropTypes.func.isRequired,
};

export default UserForm;
