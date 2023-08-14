import Grid from "@mui/material/Grid";

import EmployeeCount from "../widgets/EmployeeCount";

const ManagerBoard = () => {
  return (
    <>
      <Grid container spacing={5}>
        <Grid item>
          <EmployeeCount mode="manager" />
        </Grid>
      </Grid>
    </>
  );
};

export default ManagerBoard;
