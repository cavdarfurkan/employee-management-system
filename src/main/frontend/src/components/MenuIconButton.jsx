import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import SettingsIcon from "@mui/icons-material/Settings";
import ExitToAppIcon from "@mui/icons-material/ExitToApp";

import IconButton from "@mui/material/IconButton";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";

import { useState } from "react";
import { useNavigate } from "react-router-dom";

const MenuIconButton = () => {
  const [openMenu, setOpenMenu] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  const navigate = useNavigate();

  const handleClick = (event) => {
    setAnchorEl(event.target);
    setOpenMenu(true);
  };

  const handleClose = () => {
    setAnchorEl(null);
    setOpenMenu(false);
  };

  const handleSettingsClick = () => {
    navigate("/settings");
    handleClose();
  };

  const handleLogoutClick = () => {
    navigate("/logout");
    handleClose();
  };

  return (
    <>
      <IconButton
        onClick={handleClick}
        // edge="end"
        // sx={{
        //   marginLeft: "auto",
        // }}
      >
        <AccountCircleIcon />
      </IconButton>
      <Menu anchorEl={anchorEl} open={openMenu} onClose={handleClose}>
        <MenuItem
          onClick={handleSettingsClick}
          sx={{ display: "flex", alignItems: "center" }}
        >
          <SettingsIcon sx={{ marginRight: 1 }} />
          Settings
        </MenuItem>
        <MenuItem
          onClick={handleLogoutClick}
          sx={{ display: "flex", alignItems: "center" }}
        >
          <ExitToAppIcon sx={{ marginRight: 1 }} />
          Logout
        </MenuItem>
      </Menu>
    </>
  );
};

export default MenuIconButton;
