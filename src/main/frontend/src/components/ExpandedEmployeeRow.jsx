import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";

import PropTypes from "prop-types";

const ExpandedEmployeeRow = ({ data }) => {
  return (
    <>
      <Card variant="elevation">
        <CardContent>
          <Grid container spacing={3}>
            <Grid item direction="column" md xs={6}>
              <Grid item>
                <Typography variant="body2">
                  First Name: {data.firstName}
                </Typography>
              </Grid>
              <Grid item>
                <Typography variant="body2">
                  Last Name: {data.lastName}
                </Typography>
              </Grid>
              <Grid item>
                <Typography variant="body2">Gender: {data.gender}</Typography>
              </Grid>
            </Grid>

            <Grid item direction="column" md xs={6}>
              <Grid item>
                <Typography variant="body2">
                  Date of Birth: {data.dateOfBirth}
                </Typography>
              </Grid>
              <Grid item>
                <Typography variant="body2">
                  Phone: {data.phoneNumber}
                </Typography>
              </Grid>
              <Grid item>
                <Typography variant="body2">Address: {data.address}</Typography>
              </Grid>
            </Grid>

            <Grid item direction="column" md xs={6}>
              <Grid item>
                <Typography variant="body2">
                  Hire Date: {data.hireDate}
                </Typography>
              </Grid>
              <Grid item>
                <Typography variant="body2">
                  Manager:
                  {data.manager
                    ? ` ${data.manager.firstName} ${data.manager.lastName}`
                    : " No manager"}
                </Typography>
              </Grid>
              <Grid item>
                <Typography variant="body2">
                  Username: {data.user.username}
                </Typography>
              </Grid>
            </Grid>

            <Grid item direction="column" md xs={6}>
              <Grid item>
                <Typography variant="body2">
                  Department:{" "}
                  {data.department ? data.department.name : "No department"}
                </Typography>
              </Grid>
              <Grid item>
                <Typography variant="body2">
                  Job: {data.job ? data.job.name : "No job"}
                </Typography>
              </Grid>
              <Grid item>
                <Typography variant="body2">Salary: {data.salary}</Typography>
              </Grid>
            </Grid>
          </Grid>
        </CardContent>
      </Card>
    </>
  );
};

ExpandedEmployeeRow.propTypes = {
  data: PropTypes.object.isRequired,
};

export default ExpandedEmployeeRow;
