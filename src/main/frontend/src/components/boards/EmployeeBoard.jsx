import Grid from "@mui/material/Grid";

import Timer from "../widgets/Timer";
import TasksWidget from "../widgets/TasksWidget";

export default function EmployeeBoard() {
  return (
    <>
      <Grid container spacing={5}>
        <Grid item>
          <Timer />
        </Grid>
        <Grid item>
          <TasksWidget />
        </Grid>
      </Grid>
    </>
  );
}
