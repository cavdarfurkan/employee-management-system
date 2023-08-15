import ControllerInputField from "../components/ControllerInputField";

import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import VisibilityOnIcon from "@mui/icons-material/Visibility";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";
import RefreshIcon from "@mui/icons-material/Refresh";

import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import Checkbox from "@mui/material/Checkbox";
import Link from "@mui/material/Link";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import FormControlLabel from "@mui/material/FormControlLabel";

import { useForm } from "react-hook-form";
import AuthService from "../services/authService";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import {
  ButtonBase,
  Divider,
  FormHelperText,
  Grid,
  IconButton,
  InputAdornment,
} from "@mui/material";
import { validateJwtExpiration } from "../utils/jwtUtils";
import UserService from "../services/userService";

const LoginPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const [isError, setIsError] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const [showPassword, setShowPassword] = useState(false);

  const [users, setUsers] = useState([]);

  const {
    control,
    handleSubmit,
    formState: { errors },
    setValue,
  } = useForm();

  useEffect(() => {
    const currentUser = AuthService.getCurrentUser();
    if (currentUser && validateJwtExpiration()) {
      navigate("/");
    }

    fetchRandomUsers();
  }, [navigate]);

  const fetchRandomUsers = async () => {
    await UserService.getRandomUsers().then((data) => {
      const usersArray = data.map((user) => ({
        username: user.username,
        password: user.password,
      }));

      setUsers(usersArray);
    });
  };

  const usernameRules = {
    required: "Username is required",
    minLength: {
      value: 3,
      message: "Username must have at least 3 characters",
    },
    maxLength: {
      value: 20,
      message: "Username cannot exceed 20 characters",
    },
    pattern: {
      value: /^[a-zA-Z0-9]+$/,
      message: "Username can only contain alphanumeric characters",
    },
  };

  const passwordRules = {
    required: "Password is required",
    minLength: {
      value: 8,
      message: "Password must have at least 8 characters",
    },
    maxLength: {
      value: 100,
      message: "Password cannot exceed 100 characters",
    },
    // pattern: {
    //   value:
    //     /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/,
    //   message:
    //     "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character",
    // },
  };

  const handleOnSubmit = (e) => {
    const { username, password } = e;

    const login = async () => {
      await AuthService.login(username, password)
        .then((response) => {
          return Promise.resolve(response);
        })
        .catch((error) => {
          return Promise.reject(error);
        });
    };

    login()
      .then(() => {
        setIsError(false);
        navigate("/");
      })
      .catch((error) => {
        setIsError(true);
        setErrorMessage(error);
        fetchRandomUsers();
      });
  };

  return (
    <>
      <Grid
        container
        justifyContent={"space-evenly"}
        alignItems={"center"}
        minHeight={"100%"}
      >
        <CssBaseline />
        <Grid item xs={12} md={6}>
          <Container
            maxWidth="xs"
            sx={{
              p: 5,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography variant="h5">Sign in</Typography>
            <Box
              component="form"
              onSubmit={handleSubmit(handleOnSubmit)}
              noValidate
              sx={{ mt: 1 }}
            >
              <ControllerInputField
                value={username}
                control={control}
                name="username"
                rules={usernameRules}
                isFullWidth={true}
                size="medium"
                label="Username"
                type="text"
                error={errors.username}
                helperText={errors.username && errors.username.message}
                autoFocus={true}
              />
              <ControllerInputField
                value={password}
                control={control}
                name="password"
                rules={passwordRules}
                isFullWidth={true}
                size="medium"
                label="Password"
                type={showPassword ? "text" : "password"}
                error={errors.password}
                helperText={errors.password && errors.password.message}
                autoFocus={false}
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowPassword(!showPassword)}
                      onMouseDown={(e) => e.preventDefault()}
                      edge="end"
                    >
                      {showPassword ? (
                        <VisibilityOffIcon />
                      ) : (
                        <VisibilityOnIcon />
                      )}
                    </IconButton>
                  </InputAdornment>
                }
              />
              <FormControlLabel
                control={<Checkbox value="remember" color="primary" />}
                label="Remember me"
                disabled
              />
              {isError && <FormHelperText error>{errorMessage}</FormHelperText>}
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
              >
                Sign In
              </Button>
              <Link href="#" variant="body2">
                <Typography>Forgot password?</Typography>
              </Link>
            </Box>
          </Container>
        </Grid>
        <Grid item xs={12} md={4}>
          <Container
            sx={{
              textAlign: "center",
            }}
          >
            {users.map((user, index) => (
              <Box
                key={index}
                component={ButtonBase}
                minWidth={"75%"}
                padding={2}
                margin={1}
                borderRadius={2}
                sx={{
                  boxShadow:
                    "0px 2px 1px -1px rgba(0, 0, 0, 0.2), 0px 1px 1px 0px rgba(0, 0, 0, 0.14), 0px 1px 3px 0px rgba(0, 0, 0, 0.12)",
                  ":hover": {
                    backgroundColor: "rgba(0, 0, 0, 0.04)",
                  },
                }}
                onClick={() => {
                  setUsername(user.username);
                  setPassword(user.password);
                  setValue("username", user.username);
                  setValue("password", user.password);
                }}
              >
                <Grid
                  container
                  justifyContent={"space-around"}
                  direction={"column"}
                  spacing={1}
                >
                  <Grid item>
                    <Box display="flex" alignItems="center">
                      <Typography variant="body1">
                        Username: {user.username}
                      </Typography>
                    </Box>
                  </Grid>
                  <Grid item>
                    <Box display="flex" alignItems="center">
                      <Typography variant="body1">
                        Password: {user.password}
                      </Typography>
                    </Box>
                  </Grid>
                </Grid>
              </Box>
            ))}
            <Divider>
              <IconButton onClick={fetchRandomUsers}>
                <RefreshIcon />
              </IconButton>
            </Divider>
          </Container>
        </Grid>
      </Grid>
    </>
  );
};

export default LoginPage;
