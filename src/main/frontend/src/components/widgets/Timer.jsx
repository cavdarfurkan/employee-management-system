import { useEffect, useState } from "react";

import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardHeader from "@mui/material/CardHeader";
import Button from "@mui/material/Button";

import TimeTrackingService from "../../services/timeTrackingService";
import AuthService from "../../services/authService";

const Timer = () => {
  const userId = AuthService.getCurrentUser().user.id;

  const [isTimerActive, setIsTimerActive] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchActiveTimer = async () => {
      await TimeTrackingService.getActiveTimeTracker()
        .then((response) => {
          if (response.status === 200) {
            setIsTimerActive(true);
          }
        })
        .catch((error) => {
          if (error.response.status === 404) {
            setIsTimerActive(false);
          }
        })
        .finally(() => {
          setLoading(false);
        });
    };

    fetchActiveTimer();
  }, [userId]);

  const handleOnClick = (e) => {
    const startTimer = async () => await TimeTrackingService.startTimeTracker();
    const stopTimer = async () => await TimeTrackingService.stopTimeTracker();

    if (isTimerActive) {
      stopTimer().then((response) => {
        setIsTimerActive(false);
      });
    } else {
      startTimer().then((response) => {
        setIsTimerActive(true);
      });
    }
  };

  if (loading) {
    return <h1>Loading...</h1>;
  }

  return (
    <>
      <Card variant="elevation">
        <CardHeader title="Timer" sx={{ textAlign: "center" }} />
        <CardContent>
          <Button
            variant="contained"
            size="large"
            color={isTimerActive ? "error" : "primary"}
            style={{ width: "150px", height: "100px" }}
            onClick={handleOnClick}
          >
            {isTimerActive ? "STOP" : "START"}
          </Button>
        </CardContent>
      </Card>
    </>
  );
};

export default Timer;
