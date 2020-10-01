/* eslint-disable */
import { Box, Button, FormControl, IconButton, InputBase, makeStyles, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@material-ui/core';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowDropUpIcon from '@material-ui/icons/ArrowDropUp';
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import SearchIcon from "@material-ui/icons/Search";
import React from 'react';
import ReactPaginate from 'react-paginate';
import { useMetricProcess } from '../../../utils/metricProcess';
import FormikField from '../../common/FormikField';
import StepHeading from '../../common/StepHeading';

const useStyles = makeStyles(theme => ({
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
    },
    root: {
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
    table: {
        minWidth: 400,
        marginTop: theme.spacing(2),
    },
    tableHeadCell: {
        color: "#6282A3",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 400,
        minWidth: 100,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        padding: theme.spacing(1, 1.5),
        border: 0,
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
    tableHead: {
        backgroundColor: "#F5F7F8"
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

function createData(name, description) {
    return { name, description };
}

const rows = [
    createData('ADD', 'add two numbers'),
    createData('CONTAINS', 'evaluates whether the string contains the specified strings'),
    createData('DEVIDE', 'divide two numbers'),
    createData('EXP', 'Returns the value of the first argument raised to the power of the second argument.'),
    createData('IN', 'finds a match in the given arguments'),
];

const FilterCondition = () => {
    const classes = useStyles()
    const [isMenu, setIsMenu] = React.useState(false)
    const [pageNumber, setPageNumber] = React.useState(1)
    const [rowPerPage, setRowPerPage] = React.useState(5)
    const metricProcess = useMetricProcess()
    const { filterFunctions } = metricProcess.state
    const [filteredData, setFilteredData] = React.useState(filterFunctions)

    console.log("length =>", filteredData.length)

    React.useEffect(() => {
        metricProcess.fetchFilterFunctions()
    }, [])

    React.useEffect(() => {
        setFilteredData(filterFunctions)
    }, [filterFunctions])

    const handleMenu = () => {
        setIsMenu(!isMenu)
    }

    const handleRowsPerPageChange = e => {
        setRowPerPage(e.target.value)
    };

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

    return (
        <Box component="div">
            <StepHeading name={"Define a filter condition"} optional={"(Optional)"} />
            <FormikField name={"where"} label={"Where Condition"} />
            <Button className={classes.syntaxButton} onClick={handleMenu}>
                <Typography variant="subtitle1" component="p" className={classes.buttonText}>Language Syntax</Typography>
                {isMenu ? <ArrowDropUpIcon className={classes.selectArrow} /> : <ArrowDropDownIcon className={classes.selectArrow} />}
            </Button>
            <Box className={isMenu ? classes.menuWrapper : classes.collapseMenuWrapper}>
                <Box component="div">
                    <Box component="div" className={classes.menuHeader}>
                        <Typography variant="subtitle1" component="p" className={classes.headerTitle}>List of Functions</Typography>
                        <Paper component="form" className={classes.root}>
                            <InputBase
                                className={classes.input}
                                placeholder="Search"
                                onChange={(e) => handleSearch(e)}
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
                                            pageRangeDisplayed={5}
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
        </Box>
    )
}

export default FilterCondition
