import PropTypes from "prop-types";

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const WorkTimesChart = ({ data }) => {
  const chartData = {
    labels: data.map((entry) => entry.day),
    datasets: [
      {
        label: "Work Hours",
        data: data.map((entry) => entry.hours),
        fill: false,
        borderColor: "rgb(53, 162, 235)",
        backgroundColor: "rgba(53, 162, 235, 0.5)",
      },
    ],
  };

  const options = {
    responsive: true,
    scales: {
      x: {
        title: {
          display: true,
          text: "Days",
        },
      },
      y: {
        title: {
          display: true,
          text: "Work Hours",
        },
      },
    },
  };

  return <Bar data={chartData} options={options} />;
};

WorkTimesChart.propTypes = {
  data: PropTypes.array.isRequired,
};

export default WorkTimesChart;
