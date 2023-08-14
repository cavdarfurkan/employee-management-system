import MenuIcon from "@mui/icons-material/Menu";
import NotificationsIcon from "@mui/icons-material/Notifications";

import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Stack from "@mui/material/Stack";
import IconButton from "@mui/material/IconButton";
import Badge from "@mui/material/Badge";

import { styled } from "@mui/material/styles";

import MenuIconButton from "./MenuIconButton";

import PropTypes from "prop-types";
import { useContext } from "react";
import { AppContext } from "../AppContext";

const drawerWidth = 240;

const AppBarStyled = styled(AppBar, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme, open }) => ({
  zIndex: theme.zIndex.drawer + 1,
  transition: theme.transitions.create(["width", "margin"], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(["width", "margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));

const MyAppBar = (props) => {
  const { itemPrimaryText } = useContext(AppContext);

  return (
    <>
      <AppBarStyled position="fixed" open={props.isOpen}>
        <Toolbar>
          <IconButton
            onClick={props.handleDrawerToggle}
            edge="start"
            sx={{
              marginRight: 5,
              ...(props.isOpen && { display: "none" }),
            }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap component={"div"}>
            {/* {props.user && "Welcome " + props.user.user.username} */}
            {itemPrimaryText}
          </Typography>
          <Stack direction={"row"} spacing={1} marginLeft={"auto"}>
            <IconButton>
              <Badge color="secondary" variant="dot">
                <NotificationsIcon />
              </Badge>
            </IconButton>
            <MenuIconButton />
          </Stack>
        </Toolbar>
      </AppBarStyled>
    </>
  );
};

MyAppBar.propTypes = {
  user: PropTypes.object.isRequired,
  isOpen: PropTypes.bool.isRequired,
  handleDrawerToggle: PropTypes.func.isRequired,
};

export default MyAppBar;
