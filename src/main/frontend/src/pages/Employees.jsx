import SearchIcon from "@mui/icons-material/Search";
import EditIcon from "@mui/icons-material/Edit";
import AddIcon from "@mui/icons-material/Add";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useLoaderData, useNavigate } from "react-router-dom";

import Checkbox from "@mui/material/Checkbox";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import Fab from "@mui/material/Fab";

import DataTable from "react-data-table-component";

import ExpandedEmployeeRow from "../components/ExpandedEmployeeRow";
import EmployeeService from "../services/employeeService";
import { ROLE_ADMIN } from "../utils/constants";

const Employees = () => {
  const currentUserRoles = useLoaderData();

  const navigate = useNavigate();

  const [rowData, setRowData] = useState([]);
  const [totalRows, setTotalRows] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const [searchText, setSearchText] = useState(null);

  const [selectedRows, setSelectedRows] = useState([]);
  const [toggleClearedRows, setToggleClearedRows] = useState(false);

  const [isLoading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAllEmplooyeesPaged = async () => {
      try {
        const response = await EmployeeService.getAllEmployeesPaged(
          currentPage - 1,
          pageSize,
          searchText
        );

        if (response.data._embedded) {
          setRowData(response.data._embedded.employeeList);
          setTotalRows(response.data.page.totalElements);

          return Promise.resolve(response.data);
        } else {
          setRowData([]);
          setTotalRows(0);
          setCurrentPage(1);

          return Promise.reject("There are no records to display");
        }
      } catch (error) {
        setRowData([]);
        setTotalRows(0);
        setCurrentPage(1);

        console.log(error);
        return Promise.reject(error);
      } finally {
        setLoading(false);
      }
    };

    fetchAllEmplooyeesPaged();
  }, [currentPage, pageSize, searchText, toggleClearedRows]);

  const handleRowSelected = useCallback((state) => {
    setSelectedRows(state.selectedRows);
  }, []);

  const subHeaderSearchComponent = useMemo(() => {
    const handleSearchChange = (e) => {
      const searchText = e.target.value;

      if (searchText === "") setSearchText(null);
      else setSearchText(searchText);
    };

    return (
      <TextField
        id="input-with-icon-textfield"
        placeholder="Search"
        value={searchText}
        variant="standard"
        size="small"
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon />
            </InputAdornment>
          ),
        }}
        onChange={handleSearchChange}
      />
    );
  }, [searchText]);

  const contextAction = useMemo(() => {
    const handleDelete = async () => {
      if (window.confirm("Are you sure you want to delete?")) {
        await Promise.all(
          selectedRows.map((row) => EmployeeService.deleteEmployee(row.id))
        );
        setToggleClearedRows(!toggleClearedRows);
      }
    };

    return (
      <>
        <Button
          key="delete"
          onClick={handleDelete}
          style={{ backgroundColor: "red", color: "white" }}
        >
          Delete
        </Button>
      </>
    );
  }, [selectedRows, toggleClearedRows]);

  const handlePageChange = (page) => setCurrentPage(page);

  const handlePageSizeChange = async (newPageSize) => {
    setLoading(true);
    setPageSize(newPageSize);
    setCurrentPage(1);
    setLoading(false);
  };

  const handleEditOnClick = (row) => {
    return navigate(`/employees/${row.id}`);
  };

  const columns = [
    { name: "ID", selector: (row) => row.id, sortable: true },
    {
      name: "Name",
      selector: (row) => row.firstName + " " + row.lastName,
      sortable: true,
    },
    {
      name: "Job",
      selector: (row) => (row.job ? row.job.name : "No job"),
      sortable: true,
    },
    {
      cell: (row) => (
        <Button
          color="primary"
          onClick={() => handleEditOnClick(row)}
          startIcon={<EditIcon />}
        >
          Edit
        </Button>
      ),
      center: true,
    },
  ];

  return (
    <>
      <div style={{ position: "relative" }}>
        <DataTable
          title="Employees"
          columns={columns}
          data={rowData}
          subHeader
          subHeaderComponent={subHeaderSearchComponent}
          persistTableHead
          pagination
          paginationServer
          paginationTotalRows={totalRows}
          paginationDefaultPage={currentPage}
          paginationPerPage={pageSize}
          onChangePage={handlePageChange}
          onChangeRowsPerPage={handlePageSizeChange}
          selectableRows
          selectableRowsComponent={Checkbox}
          onSelectedRowsChange={handleRowSelected}
          clearSelectedRows={toggleClearedRows}
          contextActions={contextAction}
          progressPending={isLoading}
          expandableRows
          expandableRowsHideExpander
          expandOnRowClicked
          expandableRowsComponent={ExpandedEmployeeRow}
        />
        {currentUserRoles.includes(ROLE_ADMIN) && (
          <Fab
            color="primary"
            aria-label="add"
            onClick={() => navigate("/employees/add")}
            sx={{ position: "fixed", bottom: 16, right: 16 }}
          >
            <AddIcon />
          </Fab>
        )}
      </div>
    </>
  );
};

export default Employees;
