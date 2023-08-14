import ControllerInputField from "../components/ControllerInputField";

import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import VisibilityOnIcon from "@mui/icons-material/Visibility";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";

import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import Checkbox from "@mui/material/Checkbox";
import Link from "@mui/material/Link";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import FormControlLabel from "@mui/material/FormControlLabel";
import Paper from "@mui/material/Paper";

import { useForm } from "react-hook-form";
import AuthService from "../services/authService";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { FormHelperText, IconButton, InputAdornment } from "@mui/material";
import { validateJwtExpiration } from "../utils/jwtUtils";

const LoginPage = () => {
  const navigate = useNavigate();

  const [isError, setIsError] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const [showPassword, setShowPassword] = useState(false);

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm();

  useEffect(() => {
    const currentUser = AuthService.getCurrentUser();

    if (currentUser && validateJwtExpiration()) {
      navigate("/");
    }
  });

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
      });
  };

  return (
    <>
      <div className="center-container">
        <Container component="main" maxWidth="xs">
          <CssBaseline />
          <Paper
            elevation="3"
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
                Forgot password?
              </Link>
            </Box>
          </Paper>
        </Container>
      </div>
    </>
  );
};

export default LoginPage;
