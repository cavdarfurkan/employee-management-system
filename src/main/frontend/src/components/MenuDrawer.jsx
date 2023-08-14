import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";

import Box from "@mui/material/Box";
import CssBaseline from "@mui/material/CssBaseline";
import Drawer from "@mui/material/Drawer";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import List from "@mui/material/List";

import { styled } from "@mui/material/styles";

import { useState } from "react";
import { Outlet } from "react-router-dom";
import AuthService from "../services/authService";
import ListItems from "../utils/ListItems";
import MyAppBar from "./MyAppBar";

const drawerWidth = 240;

const openedMixin = (theme) => ({
  width: drawerWidth,
  transition: theme.transitions.create("width", {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.enteringScreen,
  }),
  overflowX: "hidden",
});

const closedMixin = (theme) => ({
  transition: theme.transitions.create("width", {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  overflowX: "hidden",
  width: `calc(${theme.spacing(7)} + 1px)`,
  [theme.breakpoints.up("sm")]: {
    width: `calc(${theme.spacing(8)} + 1px)`,
  },
});

const DrawerHeader = styled("div")(({ theme }) => ({
  display: "flex",
  alignItems: "center",
  justifyContent: "flex-end",
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
}));

const MyDrawer = styled(Drawer, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme, open }) => ({
  width: drawerWidth,
  flexShrink: 0,
  whiteSpace: "nowrap",
  boxSizing: "border-box",
  ...(open && {
    ...openedMixin(theme),
    "& .MuiDrawer-paper": openedMixin(theme),
  }),
  ...(!open && {
    ...closedMixin(theme),
    "& .MuiDrawer-paper": closedMixin(theme),
  }),
}));

const MenuDrawer = () => {
  const [isOpen, setOpen] = useState(false);

  const currentUser = AuthService.getCurrentUser();
  let roles = [];
  if (currentUser) {
    roles = currentUser.user.authorities.map(
      (authority) => authority.authority
    );
  }

  const handleDrawerToggle = () => {
    setOpen(!isOpen);
  };

  return (
    <>
      <Box sx={{ display: "flex" }}>
        <CssBaseline />
        <MyAppBar
          user={currentUser}
          isOpen={isOpen}
          handleDrawerToggle={handleDrawerToggle}
        />
        <MyDrawer variant="permanent" open={isOpen}>
          <DrawerHeader>
            <IconButton onClick={handleDrawerToggle}>
              <ChevronLeftIcon />
            </IconButton>
          </DrawerHeader>
          <Divider />
          <List component="nav">
            <ListItems userRoles={roles} />
          </List>
        </MyDrawer>
        <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
          <DrawerHeader />
          <Outlet />
        </Box>
      </Box>
    </>
  );
};

export default MenuDrawer;
