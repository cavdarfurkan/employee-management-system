import TextField from "@mui/material/TextField";
import { Controller } from "react-hook-form";

import PropTypes from "prop-types";

const ControllerInputField = ({
  value,
  control,
  name,
  rules,
  isFullWidth,
  size,
  label,
  type,
  error,
  helperText,
  autoFocus,
  endAdornment,
}) => {
  return (
    <>
      <Controller
        control={control}
        defaultValue={value}
        name={name}
        rules={rules}
        render={({ field }) => (
          <TextField
            {...field}
            margin="normal"
            fullWidth={isFullWidth}
            size={size}
            label={label}
            type={type}
            error={!!error}
            helperText={helperText}
            autoFocus={autoFocus}
            InputProps={{ endAdornment: endAdornment }}
          />
        )}
      />
    </>
  );
};

ControllerInputField.propTypes = {
  value: PropTypes.string,
  control: PropTypes.any.isRequired,
  name: PropTypes.string.isRequired,
  rules: PropTypes.object.isRequired,
  isFullWidth: PropTypes.bool.isRequired,
  size: PropTypes.string.isRequired,
  label: PropTypes.string,
  type: PropTypes.string.isRequired,
  error: PropTypes.any.isRequired,
  helperText: PropTypes.string.isRequired,
  autoFocus: PropTypes.bool.isRequired,
  endAdornment: PropTypes.node,
};

export default ControllerInputField;
