import AssessmentIcon from "@mui/icons-material/Assessment";
import AssignmentIcon from "@mui/icons-material/Assignment";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import DashboardIcon from "@mui/icons-material/Dashboard";
import MailIcon from "@mui/icons-material/Mail";
import TimeToLeaveIcon from "@mui/icons-material/TimeToLeave";
import WorkHistoryIcon from "@mui/icons-material/WorkHistory";
import ManageAccountsIcon from "@mui/icons-material/ManageAccounts";
import BusinessIcon from "@mui/icons-material/Business";
import AssignmentIndIcon from "@mui/icons-material/AssignmentInd";

import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";

import { Fragment, useContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { AppContext } from "../AppContext";

import PropTypes from "prop-types";
import { ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_MANAGER } from "./constants";

const MyListItemButton = ({
  path,
  primaryText,
  updatePrimaryText,
  icon,
  userRoles,
  roles,
  isDisabled,
}) => {
  const navigate = useNavigate();
  const location = useLocation();

  const [isSelected, setSelected] = useState(false);

  useEffect(() => {
    setSelected(location.pathname === path);
    if (location.pathname === path) {
      updatePrimaryText(primaryText);
    }
  }, [location.pathname]);

  const handleItemClick = () => {
    navigate(path);
  };

  return (
    <>
      {(roles.length === 0 ||
        roles.some((role) => userRoles.includes(role))) && (
        <ListItemButton
          selected={isSelected}
          onClick={handleItemClick}
          disabled={isDisabled}
        >
          <ListItemIcon>{icon}</ListItemIcon>
          <ListItemText primary={primaryText} />
        </ListItemButton>
      )}
    </>
  );
};

MyListItemButton.propTypes = {
  path: PropTypes.string.isRequired,
  primaryText: PropTypes.string.isRequired,
  updatePrimaryText: PropTypes.func.isRequired,
  icon: PropTypes.element.isRequired,
  userRoles: PropTypes.array.isRequired,
  roles: PropTypes.array.isRequired,
  isDisabled: PropTypes.bool,
};

MyListItemButton.defaultProps = {
  isDisabled: false,
};

const ListItems = ({ userRoles }) => {
  const { updateItemPrimaryText } = useContext(AppContext);

  return (
    <Fragment>
      <MyListItemButton
        path="/"
        primaryText="Dashboard"
        icon={<DashboardIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[]}
      />

      {/* ADMIN */}
      <MyListItemButton
        path="/employees"
        primaryText="Employees"
        icon={<ManageAccountsIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[ROLE_ADMIN, ROLE_MANAGER]}
      />
      <MyListItemButton
        path="/departments"
        primaryText="Departments"
        icon={<BusinessIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[ROLE_ADMIN]}
      />
      <MyListItemButton
        path="/jobs"
        primaryText="Jobs"
        icon={<AssignmentIndIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[ROLE_ADMIN]}
      />

      {/* MANAGER */}

      {/* EMPLOYEEE */}
      <MyListItemButton
        path="/work-times"
        primaryText="Work Times"
        icon={<WorkHistoryIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[ROLE_EMPLOYEE, ROLE_MANAGER]}
      />
      <MyListItemButton
        path="/tasks"
        primaryText="Tasks"
        icon={<AssignmentIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[ROLE_EMPLOYEE, ROLE_MANAGER, ROLE_ADMIN]}
      />

      <MyListItemButton
        path="/schedule"
        primaryText="Schedule"
        icon={<CalendarMonthIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[ROLE_EMPLOYEE]}
        isDisabled={true}
      />

      <MyListItemButton
        path="/performance"
        primaryText="Performance"
        icon={<AssessmentIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[ROLE_EMPLOYEE]}
        isDisabled={true}
      />

      <MyListItemButton
        path="/leave"
        primaryText="Leave"
        icon={<TimeToLeaveIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[ROLE_EMPLOYEE]}
        isDisabled={true}
      />

      <MyListItemButton
        path="/petition"
        primaryText="Petition"
        icon={<MailIcon />}
        updatePrimaryText={updateItemPrimaryText}
        userRoles={userRoles}
        roles={[ROLE_EMPLOYEE]}
        isDisabled={true}
      />
    </Fragment>
  );
};

ListItems.propTypes = {
  userRoles: PropTypes.array.isRequired,
};

export default ListItems;
