import Grid from "@mui/material/Grid";

import EmployeeCount from "../widgets/EmployeeCount";

const AdminBoard = () => {
  return (
    <>
      <Grid container spacing={5}>
        <Grid item>
          <EmployeeCount mode="admin" />
        </Grid>
      </Grid>
    </>
  );
};

export default AdminBoard;
