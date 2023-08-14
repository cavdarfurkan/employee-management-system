import SearchIcon from "@mui/icons-material/Search";
import EditIcon from "@mui/icons-material/Edit";
import AddIcon from "@mui/icons-material/Add";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import Checkbox from "@mui/material/Checkbox";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import Fab from "@mui/material/Fab";

import DataTable from "react-data-table-component";

import DepartmentService from "../services/departmentService";

const Departments = () => {
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
    const fetchAllDepartmentsPaged = async () => {
      try {
        const response = await DepartmentService.getAllDepartmentsPaged(
          currentPage - 1,
          pageSize,
          searchText
        );

        if (response.data._embedded) {
          setRowData(response.data._embedded.departmentList);
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

    fetchAllDepartmentsPaged();
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
    const handleDelete = () => {
      if (window.confirm("Are you sure you want to delete?")) {
        selectedRows.forEach((row) => {
          DepartmentService.deleteDepartment(row.id);
        });

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
    return navigate(`/departments/${row.id}`);
  };

  const columns = [
    { name: "ID", selector: (row) => row.id, sortable: true },
    {
      name: "Name",
      selector: (row) => row.name,
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
          title="Departments"
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
        />
        <Fab
          color="primary"
          aria-label="add"
          onClick={() => navigate("/departments/add")}
          sx={{ position: "fixed", bottom: 16, right: 16 }}
        >
          <AddIcon />
        </Fab>
      </div>
    </>
  );
};

export default Departments;
