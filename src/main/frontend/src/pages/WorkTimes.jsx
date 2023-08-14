import { useEffect, useMemo, useState } from "react";

import Container from "@mui/material/Container";

import WorkTimesChart from "../components/WorkTimesChart";

import TimeTrackingService from "../services/timeTrackingService";

const WorkTimes = () => {
  const [timeTrackerData, setTimeTrackerData] = useState([]);
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    const fetchWorkTimes = async () => {
      await TimeTrackingService.getAllTimeTrackerByEmployee().then(
        (response) => {
          if (response.data._embedded) {
            setTimeTrackerData(response.data._embedded.timeTrackingList);
          }
        }
      );
    };

    fetchWorkTimes();
  }, []);

  const chartDataMemo = useMemo(() => {
    const parsedData = timeTrackerData
      .map((data) => ({
        id: data.id,
        start_time: new Date(data.startTime),
        end_time: new Date(data.endTime),
      }))
      .sort((a, b) => a.start_time - b.start_time);

    const workHoursPerDay = parsedData.reduce((result, data) => {
      const date = data.start_time.toDateString();
      const hours = (data.end_time - data.start_time) / (1000 * 60 * 60);

      if (!result[date]) {
        result[date] = 0;
      }

      result[date] += hours;

      return result;
    }, {});

    const chartData = Object.entries(workHoursPerDay).map(([date, hours]) => ({
      day: date,
      hours: hours,
    }));

    setChartData(chartData);
  }, [timeTrackerData]);

  return (
    <>
      <Container maxWidth="md">
        <WorkTimesChart data={chartData} />
      </Container>
    </>
  );
};

export default WorkTimes;
