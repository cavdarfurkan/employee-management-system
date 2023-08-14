import { useRouteError } from "react-router-dom";

const ErrorPage = () => {
  const error = useRouteError();

  return (
    <div
      className="error-page"
      style={{
        display: "flex",
        flexDirection: "column",
        height: "100vh",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      <h1>{error.status}</h1>
      <p style={{ fontSize: "20px" }}>{error.statusText}</p>
      <p style={{ fontSize: "12px" }}>{error.error.message}</p>
      <button onClick={() => window.history.back()}>Go Back</button>
    </div>
  );
};

export default ErrorPage;
