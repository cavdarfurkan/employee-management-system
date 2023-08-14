import { useEffect, useState } from "react";

import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardHeader from "@mui/material/CardHeader";
import Typography from "@mui/material/Typography";

import EmployeeService from "../../services/employeeService";

import PropTypes from "prop-types";
import { extractEmployeeId } from "../../utils/jwtUtils";

const EmployeeCount = ({ mode }) => {
  const [employeeCount, setEmployeeCount] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchEmployeeCount = async (fetchFunc) => {
      await fetchFunc
        .then((response) => {
          setEmployeeCount(response.data);
          setLoading(false);
        })
        .catch(() => {
          setLoading(false);
        });
    };

    if (mode === "admin") {
      fetchEmployeeCount(EmployeeService.getEmployeeCount());
    } else if (mode === "manager") {
      fetchEmployeeCount(
        EmployeeService.getEmployeeCountByManagerId(extractEmployeeId())
      );
    }
  }, [mode]);

  if (loading) {
    return <h1>Loading...</h1>;
  }

  return (
    <>
      <Card variant="elevation" sx={{ textAlign: "center" }}>
        <CardHeader title="Employee Count" />
        <CardContent>
          <Typography variant="h1">{employeeCount}</Typography>
        </CardContent>
      </Card>
    </>
  );
};

EmployeeCount.propTypes = {
  mode: PropTypes.oneOf(["admin", "manager"]).isRequired,
};

export default EmployeeCount;
