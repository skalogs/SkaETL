import { Box, Button, FormControl, FormControlLabel, IconButton, InputBase, makeStyles, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowDropUpIcon from '@material-ui/icons/ArrowDropUp';
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import SearchIcon from "@material-ui/icons/Search";
import { Field, Formik, useFormikContext } from 'formik';
import { Checkbox } from 'formik-material-ui';
import React from 'react';
import ReactPaginate from 'react-paginate';
import { useConsumerProcess } from '../../../utils/consumerProcess';
import FormikField from '../../common/FormikField';
import Modal from '../../common/Modal';
import StepHeading from '../../common/StepHeading';

const useStyles = makeStyles(theme => ({
    table: {
        minWidth: 400,
        border: "1px solid #99AFC73D",
    },
    tableHeadCell: {
        color: "#6282A3",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 400,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        padding: theme.spacing(1, 1.5),
    },
    tableBodyCell: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        fontStyle: "normal",
        backgroundColor: "#fff",
        padding: theme.spacing(1.25, 1),
    },
    actionButton: {
        padding: 0,
        minWidth: 25,
        borderRadius: 2,
        marginRight: theme.spacing(1.5),
        "&:last-child": {
            marginRight: 0,
        }
    },
    tableHead: {
        backgroundColor: "#F5F7F8"
    },
    wrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
    },
    btn: {
        backgroundColor: "#18C151",
        color: "#fff",
        borderRadius: 2,
        textTransform: "capitalize",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        width: 140,
        height: 40,
        fontSize: 14,
        lineHeight: "18px",
        fontWeight: 600,
        marginBottom: theme.spacing(4),
        "&:hover": {
            backgroundColor: "#18C151",
            color: "#fff",
            boxShadow: "none",
        }
    },
    buttonIcon: {
        fontSize: 19,
        marginRight: theme.spacing(1),
    },
    modalButton: {
        backgroundColor: "#01B3FF",
        color: "#fff",
        borderRadius: 2,
        textTransform: "capitalize",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        width: 115,
        height: 40,
        fontSize: 14,
        lineHeight: "18px",
        fontWeight: 600,
        "&:hover": {
            backgroundColor: "#01B3FF",
            color: "#fff",
            boxShadow: "none",
        }
    },
    outlineButton: {
        backgroundColor: "transparent",
        color: "#00274A",
        "&:hover": {
            backgroundColor: "transparent",
            color: "#00274A",
        }
    },
    checkboxText: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        fontStyle: "normal",
    },
    root: {
        "&$checked": {
            color: "#01B3FF"
        }
    },
    checked: {},
    selectArrow: {
        position: "absolute",
        right: 16,
        paddingLeft: theme.spacing(2),
    },
    syntaxButton: {
        backgroundColor: "#fff",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        height: 45,
        padding: theme.spacing(0, 2),
        color: "#00274A",
        border: "1px solid #AABCC480",
        borderRadius: 4,
        textTransform: "capitalize",
        marginBottom: theme.spacing(2),
        width: "100%",
        "&:hover": {
            border: "1px solid #01B3FF",
            backgroundColor: "#fff",
        }
    },
    buttonText: {
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "20px",
        fontWeight: 600,
    },
    menuWrapper: {
        display: "block",
        backgroundColor: "#fff",
        padding: theme.spacing(2, 1.5),
        boxShadow: "0px 8px 15px #20282A1A",
        borderRadius: 4,
        transition: "all 0.3s ease-in-out",
        marginBottom: theme.spacing(2),
    },
    collapseMenuWrapper: {
        display: "none",
        overflow: "hidden",
        transition: "all 0.3s ease-in-out",
    },
    menuHeader: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginBottom: theme.spacing(2),
    },
    input: {
        marginLeft: theme.spacing(1),
        flex: 1,
        color: "#00274A",
        fontSize: 13,
        fontFamily: "'Open Sans', sans-serif",
    },
    iconButton: {
        padding: theme.spacing(0.5),
        backgroundColor: "transparent",
        color: "#6282A3",
        fontSize: 16,
        fontFamily: "'Open Sans', sans-serif",
    },
    headerTitle: {
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 16,
        lineHeight: "20px",
        fontWeight: 600,
        color: "#00274A",
    },
    pagination: {
        "& .pagination": {
            display: "flex",
            alignItems: "center",
            margin: 0,
            padding: 0,
            listStyle: "none",
            justifyContent: "center",
            overflow: "hidden",
            "& li.next": {
                textAlign: "center",
                "&:hover": {
                    borderColor: "#007bff"
                },
                "& a": {
                    marginRight: 0,
                    border: 0,
                    "& span": {
                        fontSize: 18,
                    }
                }
            },
            "& li.previous": {
                textAlign: "center",
                "&:hover": {
                    borderColor: "#007bff"
                },
                "& a": {
                    border: 0,
                    "& span": {
                        fontSize: 18,
                    }
                }
            },
            "& li.active a": {
                fontWeight: 700,
                color: "#fff",
                letterSpacing: "0.4px",
                backgroundColor: "#007bff",
            },
            "& li a": {
                display: "flex",
                fontWeight: 400,
                fontSize: 13,
                lineHeight: "28px",
                cursor: "pointer",
                fontFamily: "'Open Sans', sans-serif",
                color: "#00274A",
                backgroundColor: "#fff",
                border: "1px solid #99AFC780",
                outline: 0,
                width: 28,
                height: 28,
                alignItems: "center",
                justifyContent: "center",
                borderRadius: "50%",
                marginRight: theme.spacing(0.75),
                "&:hover": {
                    textDecoration: "none",
                }
            }
        }
    },
    showing: {
        color: "#00274A80",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        lineHeight: "19px",
        letterSpacing: "0.28px",
    },
    paginationArrow: {
        color: "#00274A",
        fontSize: 10,
    },
    paginationWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginTop: theme.spacing(2),
    },
    searchRoot: {
        padding: theme.spacing(0, 1),
        display: "flex",
        alignItems: "center",
        borderRadius: 2,
        boxShadow: "none",
        backgroundColor: "#F2F5F6",
        width: 247,
        height: 33,
        maxWidth: "100%",
    },
    searchInput: {
        marginLeft: theme.spacing(1),
        flex: 1,
        color: "#00274A",
        fontSize: 13,
        fontFamily: "'Open Sans', sans-serif",
    },
    checkboxWrapper: {
        "& .MuiCheckbox-colorSecondary.Mui-checked": {
            color: "#09b2fb",
        }
    },
    showingWrapper: {
        display: "flex",
        alignItems: "center",
    },
    rowsPerPageWrapper: {
        display: "flex",
        alignItems: "center",
        marginLeft: theme.spacing(3),
    },
    formControlRowsPerPage: {
        marginLeft: theme.spacing(3),
        "& .MuiInputBase-root": {
            "& .MuiSelect-selectMenu": {
                fontFamily: "'Open Sans', sans-serif",
            },
            "& .MuiSelect-icon": {
                color: "#01B3FF",
            },
            "&:hover": {
                "&:before": {
                    borderBottom: "1px solid #01B3FF",
                },
            },
            "&:before": {
                borderBottom: "1px solid #AABCC480",
            },
            "&:after": {
                borderBottom: "1px solid #AABCC480",
            }
        }
    },
}))

function createData(type, field) {
    return { type, field };
}

const rows = [
    createData('ADD', 'myNumericField + myOtherNumericField'),
    createData('CONTAINS', 'myfield CONTAINS("a","b")'),
    createData('DIVIDE', 'myNumericField / myOtherNumericField'),
];

function createModalData(name, description) {
    return { name, description };
}

const modalRows = [
    createModalData('ADD', 'add two numbers'),
    createModalData('CONTAINS', 'evaluates whether the string contains the specified strings'),
    createModalData('DEVIDE', 'divide two numbers'),
    createModalData('EXP', 'Returns the value of the first argument raised to the power of the second argument.'),
    createModalData('IN', 'finds a match in the given arguments'),
];

const Filters = () => {
    const classes = useStyles()
    const { values, setFieldValue } = useFormikContext<any>()
    const [isShow, setIsShow] = React.useState(false)
    const [isMenu, setIsMenu] = React.useState(false)
    const [isEditing, setIsEditing] = React.useState(false)
    const [pageNumber, setPageNumber] = React.useState(1)
    const [rowPerPage, setRowPerPage] = React.useState(5)
    const [selectedFilter, setSelectedFilter] = React.useState({
        name: "",
        criteria: "",
        failForwardTopic: "",
        activeFailForward: false,
    })
    const consumerProcess = useConsumerProcess()
    const { state } = consumerProcess
    const { filterFunctions } = state
    const [filteredData, setFilteredData] = React.useState(filterFunctions)

    console.log("filter data =>", filteredData)

    const handleSearch = (e) => {
        e.preventDefault()
        const searchString = e.target.value.toLowerCase()
        if (searchString.length === 0) {
            setFilteredData(filterFunctions)
            return
        }
        const filteredFunctions = filterFunctions.filter(element => element.name.toLowerCase().includes(searchString))
        setPageNumber(1)
        console.log("FILTERED PROCESS", filteredFunctions)
        setFilteredData(filteredFunctions)
    }

    const handleRowsPerPageChange = e => {
        setRowPerPage(e.target.value)
    };

    React.useEffect(() => {
        consumerProcess.fetchFilterFunctions()
    }, [])

    React.useEffect(() => {
        setFilteredData(filterFunctions)
    }, [filterFunctions])


    // React.useEffect(() => {
    //     fetchFilters()
    // }, [])


    const handleMenu = () => {
        setIsMenu(!isMenu)
    }

    const handleModal = e => {
        e.preventDefault()
        setIsShow(!isShow)
    }

    const handleEditClick = filter => {
        setIsShow(true)
        setIsEditing(true)
        setSelectedFilter(filter)
    }

    const handleSave = (subFormikValues) => {

        let updatedProcessFilters = []
        if (isEditing) {
            updatedProcessFilters = values.processFilter.map(p => {
                if (p === selectedFilter) {
                    return subFormikValues
                }
                return p
            })
        } else {
            updatedProcessFilters = [...values.processFilter, subFormikValues]
        }
        setFieldValue("processFilter", updatedProcessFilters)
        setIsEditing(false)
        setIsShow(false)
    }

    const handleDeleteAction = filter => {
        const updatedProcessFilters = values.processFilter.filter(f => f !== filter)
        setFieldValue("processFilter", updatedProcessFilters)
        
    }

    const getRows = () => {
        if (rowPerPage !== 0) {
            let fromIndex = (pageNumber * rowPerPage) - rowPerPage
            let toIndex =  (pageNumber * rowPerPage)
            if (rowPerPage >= filteredData.length) {
                fromIndex = 0
                toIndex = rowPerPage
                // setPageNumber(1)
            }
            return filteredData.slice(fromIndex, toIndex)
        } else {
            return filteredData
        }
    }

    const getShowingDetails = () => {
        if (rowPerPage !== 0) {
            return `Showing ${pageNumber}/${Math.ceil(filteredData.length / rowPerPage)}`
        } else {
            return `Showing All`
        }
    }


    return (
        <>
            <Modal title={"New Item"} show={isShow} modalClosed={() => setIsShow(false)}>
                <Formik
                    enableReinitialize
                    initialValues={selectedFilter}
                    onSubmit={handleSave}
                >
                    {(subFormik) => (
                        <Box>
                            <FormikField name={"name"} label={"Name"} />
                            <FormikField name={"criteria"} label={"Criteria"} subLabel={"(SQL like)"} />
                            {subFormik.values.activeFailForward && <FormikField name={"failForwardTopic"} label={"Topic Fail Parser"} />}

                            <Button className={classes.syntaxButton} onClick={handleMenu}>
                                <Typography variant="subtitle1" component="p" className={classes.buttonText}>Language Syntax</Typography>
                                {isMenu ? <ArrowDropUpIcon className={classes.selectArrow} /> : <ArrowDropDownIcon className={classes.selectArrow} />}
                            </Button>
                            <Box className={isMenu ? classes.menuWrapper : classes.collapseMenuWrapper}>
                                <Box component="div">
                                    <Box component="div" className={classes.menuHeader}>
                                        <Typography variant="subtitle1" component="p" className={classes.headerTitle}>List of Functions</Typography>
                                        <Paper component="form" className={classes.searchRoot}>
                                            <InputBase
                                                className={classes.searchInput}
                                                onChange={(e) => handleSearch(e)}
                                                placeholder="Search"
                                                inputProps={{ "aria-label": "search" }}
                                            />
                                            <IconButton className={classes.iconButton} aria-label="search">
                                                <SearchIcon />
                                            </IconButton>
                                        </Paper>
                                    </Box>
                                    {filteredData.length === 0 && !filteredData ? (
                                        <p>loading...</p>
                                    ) : (
                                            <Box component="div">
                                                <TableContainer>
                                                    <Table className={classes.table} aria-label="simple table">
                                                        <TableHead className={classes.tableHead}>
                                                            <TableRow>
                                                                <TableCell className={classes.tableHeadCell}>Function name</TableCell>
                                                                <TableCell className={classes.tableHeadCell}>Description</TableCell>
                                                                <TableCell className={classes.tableHeadCell}>Example</TableCell>
                                                            </TableRow>
                                                        </TableHead>
                                                        <TableBody>
                                                            {getRows().map((row) => (
                                                                <TableRow key={row.name}>
                                                                    <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                                        {row.name}
                                                                    </TableCell>
                                                                    <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                                        {row.description}
                                                                    </TableCell>
                                                                    <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                                        {row.example}
                                                                    </TableCell>
                                                                </TableRow>
                                                            ))}
                                                        </TableBody>
                                                    </Table>
                                                </TableContainer>
                                                <Box component="div" className={classes.paginationWrapper}>
                                                    <Box component="div" className={classes.showingWrapper}>
                                                    <Typography variant="subtitle1" component="p" className={classes.showing}>{getShowingDetails()}</Typography>
                                                        <Box component="div" className={classes.rowsPerPageWrapper}>
                                                            <Typography variant="subtitle1" component="p" className={classes.showing}>Rows per page</Typography>
                                                            <FormControl className={classes.formControlRowsPerPage}>
                                                                <Select
                                                                    labelId="demo-simple-select-filled-label"
                                                                    id="demo-simple-select-filled"
                                                                    value={rowPerPage}
                                                                    onChange={handleRowsPerPageChange}
                                                                >
                                                                    <MenuItem value={5}>5</MenuItem>
                                                                    <MenuItem value={10}>10</MenuItem>
                                                                    <MenuItem value={25}>25</MenuItem>
                                                                    <MenuItem value={0}>All</MenuItem>
                                                                </Select>
                                                            </FormControl>
                                                        </Box>
                                                    </Box>
                                                    { rowPerPage !== 0 && <Box component="div" className={classes.pagination}>
                                                        <ReactPaginate
                                                            previousLabel={<ArrowBackIosIcon className={classes.paginationArrow} />}
                                                            nextLabel={<ArrowForwardIosIcon className={classes.paginationArrow} />}
                                                            breakLabel={'...'}
                                                            breakClassName={'break-me'}
                                                            pageCount={Math.ceil(filteredData.length / rowPerPage)}
                                                            marginPagesDisplayed={2}
                                                            pageRangeDisplayed={3}
                                                            onPageChange={({ selected }) => setPageNumber(selected + 1)}
                                                            containerClassName={'pagination'}
                                                            activeClassName={'active'}
                                                        />
                                                    </Box>
                                                    }
                                                </Box>
                                            </Box>
                                        )}
                                </Box>
                            </Box>
                            <Box className={classes.wrapper}>
                                <Box className={classes.checkboxWrapper}>
                                    <FormControlLabel
                                        control={<Field name="activeFailForward" type="checkbox" component={Checkbox} />}
                                        label={<Typography className={classes.checkboxText}>Active fail parser forward</Typography>}
                                    />
                                </Box>
                                <Box>
                                    <Button className={`${classes.modalButton} ${classes.outlineButton}`} onClick={() => setIsShow(false)}>cancel</Button>
                                    <Button onClick={(e) => {
                                        e.stopPropagation()
                                        subFormik.handleSubmit()
                                    }} className={classes.modalButton}>save</Button>
                                </Box>
                            </Box>
                        </Box>
                    )}
                </Formik>
            </Modal>
            <Box component="div">
                <Box component="div" className={classes.wrapper}>
                    <StepHeading name={"Select Filters"} optional={"(Optional)"} />
                    <Button variant="contained" className={classes.btn} onClick={handleModal}><AddIcon className={classes.buttonIcon} /> Add Filters</Button>
                </Box>
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Filter name</TableCell>
                                <TableCell className={classes.tableHeadCell}>Criteria</TableCell>
                                <TableCell align="right" className={classes.tableHeadCell}>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {values.processFilter.map((item, i) => (
                                <TableRow key={i}>
                                    <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                        {item.name}
                                    </TableCell>
                                    <TableCell className={classes.tableBodyCell}>
                                        {item.criteria}
                                    </TableCell>
                                    <TableCell align="right" className={classes.tableBodyCell}>
                                        <Button onClick={() => handleEditClick(item)} className={classes.actionButton}>
                                            <EditIcon />
                                        </Button>
                                        <Button onClick={() => handleDeleteAction(item)} className={classes.actionButton}>
                                            <DeleteIcon />
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Box>
        </>
    )
}

export default Filters
