import { Box, Button, ClickAwayListener, FormControl, Grid, IconButton, InputBase, List, ListItem, makeStyles, MenuItem, Paper, Select, Switch, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, withStyles } from '@material-ui/core';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import MoreHorizIcon from '@material-ui/icons/MoreHoriz';
import SearchIcon from "@material-ui/icons/Search";
import Skeleton from '@material-ui/lab/Skeleton';
import Link from "next/link";
import { useRouter } from 'next/router';
import React from 'react';
import ReactPaginate from 'react-paginate';
import Layout from '../../components/common/Layout';
import NoData from '../../components/common/NoData';
import PageTitle from '../../components/common/PageTitle';
import { useReferentialProcess } from '../../utils/referential';
import StatusIcon from '../../components/common/StatusIcon';

const useStyles = makeStyles(theme => ({
    tableContainer: {
        border: "1px solid #99AFC73D",
        // overflow: "visible",
        minHeight: 275,
        "& .table-row": {
            "&:nth-child(3)": {
                "& .filter-wrapper": {
                    top: "auto",
                    bottom: "-100%",
                }
            },
            "&:nth-child(4)": {
                "& .filter-wrapper": {
                    top: "auto",
                    bottom: "-100%",
                }
            },
            "&:nth-child(5)": {
                "& .filter-wrapper": {
                    top: "auto",
                    bottom: 0,
                }
            }
        }
    },
    table: {
        minWidth: 400,
        position: "relative",
    },
    tableRow: {
        borderBottom: "1px solid #99AFC73D",
        "&:last-child": {
            borderBottom: "none",
        }
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
        whiteSpace: "nowrap",
        overflow: "hidden",
        backgroundColor: "#F5F7F8",
        "&:last-child": {
            position: "sticky",
            right: 0,
        }
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
        maxWidth: 600,
        textOverflow: "ellipsis",
        // wordBreak: "break-word",
        whiteSpace: "nowrap",
        overflow: "hidden",
        borderBottom: "none",
        "& ul": {
            margin: 0,
            padding: 0,
            listStyle: "none",
            "& li": {
                backgroundColor: "#f5f7f8",
                padding: theme.spacing(0.25, 1),
                borderRadius: 4,
                marginBottom: theme.spacing(0.5),
            }
        },
        "&:last-child": {
            position: "sticky",
            right: 0,
        }
    },
    actionButton: {
        padding: 0,
        minWidth: 25,
        borderRadius: 2,
    },
    paginationWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginTop: theme.spacing(4),
    },
    visualizeButton: {
        color: "#01B3FF",
        backgroundColor: "#fff",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        width: 112,
        height: 40,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        border: "1px solid #01B3FF",
        textTransform: "capitalize",
        marginRight: theme.spacing(2),
        "&:hover": {
            color: "#fff",
            backgroundColor: "#01B3FF",
            boxShadow: "none",
        },
    },
    createReferentialButton: {
        color: "#fff",
        backgroundColor: "#01B3FF",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        width: 215,
        height: 40,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
        "&:hover": {
            color: "#01B3FF",
            backgroundColor: "#fff",
            border: "1px solid #01B3FF",
            boxShadow: "none",
        }
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
    root: {
        padding: theme.spacing(0, 1),
        display: "flex",
        alignItems: "center",
        borderRadius: 0,
        boxShadow: "none",
        // backgroundColor: "#F2F5F6",
        width: 247,
        height: 40,
        maxWidth: "100%",
        borderBottom: "1px solid #AABCC480",
        "&:hover": {
            borderColor: "#01B3FF",
        }
    },
    input: {
        // marginLeft: theme.spacing(1),
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
    wrapper: {
        display: "flex",
        alignItems: "flex-end",
        justifyContent: "space-between",
        marginBottom: theme.spacing(4),
    },
    innerWrapper: {
        display: "flex",
        alignItems: "flex-end",
    },
    tableHead: {
        backgroundColor: "#F5F7F8"
    },
    link: {
        textDecoration: "none",
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
    utilityWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        position: "relative",
        marginLeft: theme.spacing(2),
        "& > div": {
            width: 263,
            minWidth: 160,
            margin: "0 0 0 auto"
        }
    },
    menuListItem: {
        listStyleType: "none",
        padding: theme.spacing(1, 2),
        marginBottom: theme.spacing(0.5),
        cursor: "pointer",
        fontFamily: "'Open Sans', sans-serif",
        color: "#0D0D0D",
        fontSize: 13,
        lineHeight: "19px",
        fontWeight: 400,
        textTransform: "capitalize",
        userSelect: "none",
        display: "flex",
        justifyContent: "space-between",
        "&:hover": {
            backgroundColor: "#F3F4F5",
        },
        "&:last-child": {
            marginBottom: 0,
        }
    },
    menuWrapper: {
        position: "absolute",
        top: 40,
        backgroundColor: "#fff",
        boxShadow: "0px 8px 15px #2E3B4029",
        borderRadius: 0,
        transition: "all 0.3s ease",
        width: 263,
        zIndex: 1,
        padding: theme.spacing(0),
    },
    down: {
        transform: "rotate(0deg)",
        transition: "all 0.3s ease",
        color: "#01B3FF",
    },
    up: {
        transform: "rotate(180deg)",
        transition: "all 0.3s ease",
        color: "#01B3FF",
    },
    formControlMarket: {
        margin: theme.spacing(0),
        minWidth: "100%",
        height: 40,
        fontFamily: "'Open Sans', sans-serif",
        color: "rgba(0, 39, 74, 0.4)",
        backgroundColor: "#fff",
        display: "flex",
        justifyContent: "space-between",
        padding: theme.spacing(0, 1, 0, 2),
        borderRadius: 0,
        fontSize: 13,
        lineHeight: "15px",
        textTransform: "capitalize",
        fontWeight: 400,
        borderBottom: "1px solid #AABCC480",
        "&:hover": {
            borderColor: "#01B3FF",
            backgroundColor: "#fff",
        }
    },
    tableCellToggle: {
        display: "none"
    },
    refreshButton: {
        color: "#01B3FF",
        textTransform: "capitalize",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 400,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 0,
        marginLeft: theme.spacing(4),
    },
    whenIsFilter: {
        // position: "static",
        overflow: "visible",
        backgroundColor: "transparent",
    },
    statusCell: {
        display: "flex",
        alignItems: "center",
    },
    filterWrapper: {
        position: "absolute",
        top: 5,
        right: 40,
        zIndex: 99,
        width: 150,
        backgroundColor: "#fff",
        boxShadow: "0px 8px 15px #2E3B4029",
    },
    list: {
        padding: 0,
    },
    listItem: {
        color: "#222",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        padding: theme.spacing(0.75, 2)
    },
    enable: {
        color: "#067c30",
    },
    delete: {
        color: "#FE6847",
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

const CustomSwitch = withStyles(theme => ({
    root: {
        width: 37,
        height: 16,
        padding: 0,
        display: "flex",
        "& .MuiSwitch-colorSecondary.Mui-checked + .MuiSwitch-track": {
            backgroundColor: "#01B3FF",
        },
    },
    switchBase: {
        padding: 2,
        color: theme.palette.grey[500],
        "&$checked": {
            transform: "translateX(12px)",
            color: theme.palette.common.white,
            "& + $track": {
                opacity: 1,
                backgroundColor: "#2164E8",
            },
        },
    },
    thumb: {
        width: 12,
        height: 12,
        boxShadow: "none",
        color: "#fff",
    },
    track: {
        borderRadius: 16 / 2,
        opacity: "1 !important",
        backgroundColor: "#b8b9b9",
    },
}))(Switch)

const Referential = () => {
    const classes = useStyles()
    const router = useRouter()
    const [showCellTypes, setShowCellTypes] = React.useState(false)
    const [actionMenuClicked, setActionMenuClicked] = React.useState(null)
    const [pageNumber, setPageNumber] = React.useState(1)
    const [rowPerPage, setRowPerPage] = React.useState(5)
    const referentialProcess = useReferentialProcess()
    const { data, isLoading } = referentialProcess.state.list
    const [filteredData, setFilteredData] = React.useState(data)

    React.useEffect(() => {
        referentialProcess.fetchList()
        // consumerProcess.dispatch({ type: "RESET_PROCESS" })
    }, [])

    React.useEffect(() => {
        setFilteredData(data)
    }, [data])

    const handleRefresh = () => {
        referentialProcess.fetchList()
    }


    const [processFilters, setProcessFilters] = React.useState([
        { id: "name", title: "Name", isVisible: true, isInFilters: false },
        { id: "datareferential", title: "Data Referential", isVisible: true, isInFilters: true },
        { id: "key", title: "Key", isVisible: true, isInFilters: true },
        { id: "metadata", title: "Metadata", isVisible: true, isInFilters: true },
        { id: "process", title: "Process", isVisible: true, isInFilters: true },
        { id: "tracking", title: "Tracking", isVisible: true, isInFilters: true },
        { id: "validationall", title: "Validation All", isVisible: true, isInFilters: true },
        { id: "validationfield", title: "Validation Field", isVisible: true, isInFilters: true },
        { id: "status", title: "Status", isVisible: true, isInFilters: true },
    ])

    const handleDeactivateAction = async referential => {
        setActionMenuClicked(null)
        const _ = await referentialProcess.deactivateReferential(referential.id)
        referentialProcess.fetchList()
    }

    const handleActivateAction = async referential => {
        setActionMenuClicked(null)
        const _ = await referentialProcess.activateReferential(referential.id)
        referentialProcess.fetchList()
    }

    const handleEditActionClick = refrerential => {
        router.push(`/referential/create?referentialId=${refrerential.id}`)
    }

    const handleDeleteAction = async refrerential => {
        setActionMenuClicked(null)
        const _ = await referentialProcess.deleteReferential(refrerential.id)
        referentialProcess.fetchList()
    }

    const handleRowsPerPageChange = e => {
        setRowPerPage(e.target.value);
    };

    const handleClickAwayMenu = () => {
        setActionMenuClicked(null)
    }

    const handleActionMenuClick = id => {
        setActionMenuClicked(prevId => {
            if (prevId) {
                return null
            }
            return id
        })
    }

    const handleSwitch = id => {
        setProcessFilters(prevArr => {
            const updatedArray = prevArr.map(element => {
                if (element.id === id) {
                    const updatedElement = { ...element, isVisible: !element.isVisible }
                    return updatedElement
                }
                return element
            })
            return updatedArray
        })
    }

    const handleClickAway = () => {
        setShowCellTypes(false)
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

    const handleSearch = (e) => {
        e.preventDefault()
        const searchContent = e.target.value.toLowerCase()
        if (searchContent.length === 0) {
            setFilteredData(data)
            return
        }
        const filteredProcesses = data.filter(element => {
            return element.processDefinition.name.toLowerCase().includes(searchContent)
        })
        console.log("FILTERED PROCESS", filteredProcesses)
        setFilteredData(filteredProcesses)
    }


    return (
        <Layout>
            <Grid container>
                <Grid item xs={12}>
                    <PageTitle title={"Referential List"} />
                    <Box className={classes.wrapper}>
                        <Box className={classes.innerWrapper}>
                            <Paper component="form" className={classes.root}>
                                <InputBase
                                    className={classes.input}
                                    onChange={(e) => handleSearch(e)}
                                    placeholder="Search"
                                    inputProps={{ "aria-label": "search" }}
                                />
                                <IconButton className={classes.iconButton} aria-label="search">
                                    <SearchIcon />
                                </IconButton>
                            </Paper>
                            <ClickAwayListener onClickAway={handleClickAway}>
                                <Box className={classes.utilityWrapper}>
                                    <Box>
                                        {/* <Typography variant="subtitle1" component="p" className={classes.label}>{"Optional Filters"}</Typography> */}
                                        <Button
                                            className={classes.formControlMarket}
                                            onClick={() => setShowCellTypes(value => !value)}
                                        >
                                            {"Select Filters"}
                                            <ArrowDropDownIcon className={showCellTypes ? classes.up : classes.down} />
                                            {/* <img
                                                src="/static/images/down.png"
                                                alt=""
                                                className={showCellTypes ? classes.up : classes.down}
                                            /> */}
                                        </Button>
                                        {showCellTypes ? (
                                            <Box component="div" className={classes.menuWrapper}>
                                                {processFilters.map(ele => {
                                                    if (ele.isInFilters) {
                                                        return (
                                                            <ListItem
                                                                button
                                                                key={ele.id}
                                                                className={classes.menuListItem}
                                                            // onClick={() =>
                                                            //     onCellTypeSelection(CellType.transformation)
                                                            // }
                                                            >
                                                                {ele.title}
                                                                <CustomSwitch checked={ele.isVisible} onChange={() => handleSwitch(ele.id)} />
                                                            </ListItem>
                                                        )
                                                    }
                                                    return null
                                                })}
                                            </Box>
                                        ) : null}
                                    </Box>
                                </Box>
                            </ClickAwayListener>
                            <Button onClick={handleRefresh} className={classes.refreshButton}>Refresh</Button>
                        </Box>
                        <Box component="div">
                            <Link href="/visualize">
                                <a className={classes.link}><Button variant="contained" className={classes.visualizeButton}>Visualize</Button></a>
                            </Link>
                            <Link href="/referential/create">
                                <Button variant="contained" className={classes.createReferentialButton}>
                                    <a className={classes.link}>Create Referential</a>
                                </Button>
                            </Link>
                        </Box>
                    </Box>
                    <Box component="div">
                        <ClickAwayListener onClickAway={handleClickAwayMenu}>
                            <TableContainer className={classes.tableContainer}>
                                <Table className={classes.table} aria-label="simple table">
                                    <TableHead className={classes.tableHead}>
                                        <TableRow className={classes.tableRow}>
                                            {processFilters.map(header => {
                                                const { id, title, isVisible } = header
                                                return (
                                                    <TableCell key={id} className={!isVisible ? `${classes.tableHeadCell} ${classes.tableCellToggle}` : classes.tableHeadCell}>{title}</TableCell>
                                                )
                                            })}
                                            <TableCell align="right" className={classes.tableHeadCell}>Action</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    {(filteredData.length === 0) ?
                                        (
                                            <NoData text={"No data Available"} cols={9} />
                                        )
                                        : (
                                            <TableBody>
                                                {(filteredData && filteredData.length !== 0) && getRows().map((row) => (
                                                    <TableRow key={row.id} className={`${classes.tableRow} table-row`}>
                                                        <TableCell component="th" scope="row" className={!processFilters.find(x => x.id === "name").isVisible ? `${classes.tableBodyCell} ${classes.tableCellToggle}` : classes.tableBodyCell}>
                                                            {isLoading ? <Skeleton animation={"wave"} /> : row.processDefinition.name}
                                                        </TableCell>
                                                        <TableCell className={!processFilters.find(x => x.id === "datareferential").isVisible ? `${classes.tableBodyCell} ${classes.tableCellToggle}` : classes.tableBodyCell}>
                                                            {isLoading ? <Skeleton animation={"wave"} /> : row.processDefinition.referentialKey}
                                                        </TableCell>
                                                        <TableCell className={!processFilters.find(x => x.id === "key").isVisible ? `${classes.tableBodyCell} ${classes.tableCellToggle}` : classes.tableBodyCell}>
                                                            {isLoading ? <Skeleton animation={"wave"} /> : (
                                                                <>
                                                                    <ul>
                                                                        {
                                                                            row.processDefinition.listAssociatedKeys.map((item, i) => {
                                                                                return <li key={i}>{item}</li>
                                                                            })
                                                                        }
                                                                    </ul>
                                                                </>
                                                            )}
                                                        </TableCell>
                                                        <TableCell className={!processFilters.find(x => x.id === "metadata").isVisible ? `${classes.tableBodyCell} ${classes.tableCellToggle}` : classes.tableBodyCell}>
                                                            {isLoading ? <Skeleton animation={"wave"} /> : (
                                                                <>
                                                                    <ul>
                                                                        {
                                                                            row.processDefinition.listMetadata.map((item, i) => {
                                                                                return <li key={i}>{item}</li>
                                                                            })
                                                                        }
                                                                    </ul>
                                                                </>
                                                            )}
                                                        </TableCell>
                                                        <TableCell className={!processFilters.find(x => x.id === "process").isVisible ? `${classes.tableBodyCell} ${classes.tableCellToggle}` : classes.tableBodyCell}>
                                                            {isLoading ? <Skeleton animation={"wave"} /> : (
                                                                <>
                                                                    <ul>
                                                                        {
                                                                            row.processDefinition.listIdProcessConsumer.map((item, i) => {
                                                                                return <li key={i}>{item}</li>
                                                                            })
                                                                        }
                                                                    </ul>
                                                                </>
                                                            )}
                                                        </TableCell>
                                                        <TableCell className={!processFilters.find(x => x.id === "tracking").isVisible ? `${classes.tableBodyCell} ${classes.tableCellToggle}` : classes.tableBodyCell}>
                                                            {isLoading ? <Skeleton animation={"wave"} /> : `${row.processDefinition.isNotificationChange}`}
                                                        </TableCell>
                                                        <TableCell className={!processFilters.find(x => x.id === "validationall").isVisible ? `${classes.tableBodyCell} ${classes.tableCellToggle}` : classes.tableBodyCell}>
                                                            {isLoading ? <Skeleton animation={"wave"} /> : `${row.processDefinition.isValidationTimeAllField}`}
                                                        </TableCell>
                                                        <TableCell className={!processFilters.find(x => x.id === "validationfield").isVisible ? `${classes.tableBodyCell} ${classes.tableCellToggle}` : classes.tableBodyCell}>
                                                            {isLoading ? <Skeleton animation={"wave"} /> : `${row.processDefinition.fieldChangeValidation}`}
                                                        </TableCell>
                                                        <TableCell className={!processFilters.find(x => x.id === "status").isVisible ? `${classes.tableBodyCell} ${classes.tableCellToggle}` : classes.tableBodyCell}>
                                                            {isLoading ? <Skeleton animation={"wave"} /> : <span className={classes.statusCell}><StatusIcon status={row.statusProcess} /> {row.statusProcess} </span>}
                                                        </TableCell>
                                                        <TableCell align="right" className={actionMenuClicked ? `${classes.tableBodyCell} ${classes.whenIsFilter}` : classes.tableBodyCell}>
                                                            <Box style={{ position: "relative" }}>
                                                                {isLoading ? <Skeleton animation={"wave"} /> : <Button className={classes.actionButton} onClick={() => handleActionMenuClick(row.id)}><MoreHorizIcon /></Button>}
                                                                {(actionMenuClicked === row.id) ? (
                                                                    <Box component="div" className={`${classes.filterWrapper} filter-wrapper`}>
                                                                        <List className={classes.list}>
                                                                            {/* <Link href="/consumer-process/create"> */}
                                                                            <ListItem className={classes.listItem} button onClick={() => handleEditActionClick(row)} ><a>Edit</a></ListItem>
                                                                            {/* </Link> */}
                                                                            <ListItem className={classes.listItem} button disabled={row.statusProcess == 'DISABLE' || row.statusProcess == 'INIT'} onClick={() => handleDeactivateAction(row)}>Deactivate</ListItem>
                                                                            <ListItem className={`${classes.listItem} ${classes.enable}`} button disabled={row.statusProcess == 'ENABLE' || row.statusProcess == 'ERROR' || row.statusProcess == 'DEGRADED'} onClick={() => handleActivateAction(row)}>Activate</ListItem>
                                                                            <ListItem className={`${classes.listItem} ${classes.delete}`} button onClick={() => handleDeleteAction(row)}>Delete</ListItem>
                                                                        </List>
                                                                    </Box>
                                                                ) : null}
                                                            </Box>
                                                        </TableCell>
                                                    </TableRow>
                                                ))}
                                            </TableBody>
                                        )}
                                </Table>
                            </TableContainer>
                        </ClickAwayListener>
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
                            { rowPerPage !== 0 &&  <Box component="div" className={classes.pagination}>
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
                                    // subContainerClassName={'pages pagination'}
                                    activeClassName={'active'}
                                />
                            </Box>
                            }
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </Layout >
    )
}

export default Referential
