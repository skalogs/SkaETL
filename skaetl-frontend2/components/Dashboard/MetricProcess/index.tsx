import { Box, Button, ClickAwayListener, Grid, List, ListItem, makeStyles, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@material-ui/core';
import MoreHorizIcon from '@material-ui/icons/MoreHoriz';
import Link from 'next/link';
import { useRouter } from "next/router";
import React from 'react';
import { useDashboardState } from '../../../utils/dashboard';
import { useMetricProcess } from '../../../utils/metricProcess';
import Card from '../../common/Card';
import NoData from '../../common/NoData';
import StatusIcon from '../../common/StatusIcon';
import ProcessName from '../../MetricProcess/ProcessName';

const useStyles = makeStyles(theme => ({
    tableContainer: {
        // border: "1px solid #99AFC73D",
        // overflow: "visible",
        minHeight: 275,
        "& .table-row": {
            "&:nth-child(2)": {
                "& .filter-wrapper": {
                    top: "-10px",
                    bottom: "auto",
                }
            },
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
        backgroundColor: "#fff",
        "&:first-child": {
            paddingLeft: 0,
        },
        "&:last-child": {
            position: "sticky",
            right: 0,
            paddingRight: 0,
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
        "&:first-child": {
            paddingLeft: 0,
        },
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
            paddingRight: 0,
        }
    },
    actionButton: {
        padding: 0,
        minWidth: 25,
        borderRadius: 2,
    },
    buttonsWrapper: {
        textAlign: "right",
        marginTop: theme.spacing(2),
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
    createConsumerProcessButton: {
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
        },
    },
    tableHead: {
        // backgroundColor: "#F5F7F8",
    },
    link: {
        textDecoration: "none",
    },
    active: {
        color: "#067c30",
    },
    alignDiv: {
        display: "flex",
        alignItems: "center",
    },
    icon: {
        width: 15,
        marginRight: theme.spacing(1),
    },
    filterWrapper: {
        position: "absolute",
        top: 5,
        right: 30,
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
    error: {
        color: "red"
    },
    power: {
        color: "blue"
    },
    disable: {
        color: "orange"
    },
    delete: {
        color: "#FE6847",
    },
    noDataWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        height: 185,
    },
    tableCellToggle: {
        display: "none"
    },
    noDataText: {
        fontSize: 14,
        lineHeight: "19px",
        fontWeight: 600,
        letterSpacing: "0.42px",
        color: "#636568",
        fontFamily: "'Open Sans', sans-serif",
    },
    whenIsFilter: {
        overflow: "visible",
        backgroundColor: "transparent",
    },
    modalButtonsWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
    },
    modalIcon: {
        marginLeft: theme.spacing(2),
    },
}))

const MetricProcess = () => {
    const classes = useStyles()
    const router = useRouter()
    const { state: { metricProcessList } } = useDashboardState()
    const metricProcess = useMetricProcess()
    const [actionMenuClicked, setActionMenuClicked] = React.useState(null)
    const [isOpen, setIsOpen] = React.useState(false)

    const handleModal = () => {
        setIsOpen(!isOpen)
    }

    const handleClickAwayMenu = () => {
        setActionMenuClicked(null)
    }

    const handleMetricEditClick = process => {
        router.push(`/metric-process/create?processId=${process.processDefinition.idProcess}`)
    }

    const handleMetricActionClick = process => {
        router.push(`/metric/action?processId=${process.processDefinition.idProcess}`)
    }

    const handleActionMenuClick = (id) => {
        setActionMenuClicked(prevId => {
            if (prevId) {
                return null
            }
            return id
        })
    }

    const handleDeactivateAction = async process => {
        setActionMenuClicked(null)
        const _ = await metricProcess.deactivateProcess(process.processDefinition.idProcess)
        console.log("FETCH LIST NOW")
        metricProcess.fetchList()
    }

    const handleActivateAction = async process => {
        setActionMenuClicked(null)
        const _ = await metricProcess.activateProcess(process.processDefinition.idProcess)
        console.log("FETCH LIST NOW")
        metricProcess.fetchList()
    }

    const handleDeleteAction = async process => {
        setActionMenuClicked(null)
        const _ = await metricProcess.deleteProcess(process.processDefinition.idProcess)
        console.log("FETCH LIST NOW")
        metricProcess.fetchList()
    }

    const getProcessName = async idProcess => {
        const { state: { sourceProcesses }, fetchReferentialByIdPromise, dispatch } = metricProcess
        try {
            const res = await fetchReferentialByIdPromise(idProcess)
            const data = await res.json()
            console.log("FIND RESULT API", data)

            dispatch({ type: "SET_SOURCE_PROCESSES", payload: [...sourceProcesses, { ...data }] })
            return data
        } catch (error) {
            console.log("ERROR")
        }
    }

    const { isLoading, isError, error, data } = metricProcessList
    console.log("METRIC PROCESS LIST", metricProcessList)

    return (
        <>
            {/* <Modal title={"Select your Kafka live"} show={isOpen} modalClosed={() => setIsOpen(false)} isClose={true}>
                <Box component="div" className={classes.modalButtonsWrapper}>
                    <Link href="/process/live">
                        <Button className={classes.createConsumerProcessButton}>Live After Parsing <CallSplitIcon className={classes.modalIcon} /></Button>
                    </Link>
                    <Link href="/process/live">
                        <Button className={classes.createConsumerProcessButton}>Live After Process <CallSplitIcon className={classes.modalIcon} /></Button>
                    </Link>
                </Box>
            </Modal> */}
            <Grid container>
                <Grid item xs={12}>
                    <Card title={"Metric Process"} link={"See More"} path={"/metric-process"} isLink={true} loading={data.length === 0 && isLoading ? true : false}>
                        <Box component="div">
                            <ClickAwayListener onClickAway={handleClickAwayMenu}>
                                <TableContainer className={classes.tableContainer}>
                                    <Table className={classes.table} aria-label="simple table">
                                        <TableHead className={classes.tableHead}>
                                            <TableRow>
                                                <TableCell className={classes.tableHeadCell}>Name</TableCell>
                                                <TableCell className={classes.tableHeadCell}>Process</TableCell>
                                                <TableCell className={classes.tableHeadCell}>Function</TableCell>
                                                <TableCell className={classes.tableHeadCell}>Status</TableCell>
                                                <TableCell align="right" className={classes.tableHeadCell}>Action</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        {(data.length === 0) ?
                                            (
                                                <NoData text={"No data Available"} cols={5} />
                                            ) : (
                                                <TableBody>
                                                    {data.slice(0, 5).map((row) => (
                                                        <TableRow key={row.processDefinition.name} className={`${classes.tableRow} table-row`}>
                                                            <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                                {row.processDefinition.name}
                                                            </TableCell>
                                                            <TableCell className={classes.tableBodyCell}>{
                                                                <ul>
                                                                    {
                                                                        row.processDefinition.sourceProcessConsumers.map((item, i) => {
                                                                            return <ProcessName key={i} id={item} />
                                                                        })
                                                                    }
                                                                </ul>
                                                            }
                                                            </TableCell>

                                                            <TableCell className={classes.tableBodyCell}>{row.processDefinition.aggFunction}</TableCell>
                                                            <TableCell className={classes.tableBodyCell}>
                                                                <Box className={classes.alignDiv}>
                                                                    <StatusIcon status={row.statusProcess} /> {row.statusProcess}
                                                                </Box>
                                                            </TableCell>
                                                            <TableCell align="right" className={actionMenuClicked ? `${classes.tableBodyCell} ${classes.whenIsFilter} filtermenu` : classes.tableBodyCell}>
                                                                <Box style={{ position: "relative" }}>
                                                                    <Button className={classes.actionButton} onClick={() => handleActionMenuClick(row.id)}><MoreHorizIcon /></Button>
                                                                    {(actionMenuClicked === row.id) ? (
                                                                        <Box component="div" className={`${classes.filterWrapper} filter-wrapper`}>
                                                                            <List className={classes.list}>
                                                                                <ListItem className={classes.listItem} button onClick={() => handleMetricEditClick(row)}><a>Edit</a></ListItem>
                                                                                {/* <ListItem className={classes.listItem} onClick={handleModal} button>Live</ListItem> */}
                                                                                <ListItem className={classes.listItem} onClick={() => handleMetricActionClick(row)} button>Action</ListItem>
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
                            <Box component="div" className={classes.buttonsWrapper}>
                                <Link href="/visualize">
                                    <a className={classes.link}><Button variant="contained" className={classes.visualizeButton}>Visualize</Button></a>
                                </Link>
                                <Link href="/metric-process/create">
                                    <a className={classes.link}>
                                        <Button variant="contained" className={classes.createConsumerProcessButton}>Create Metric Process</Button>
                                    </a>
                                </Link>
                            </Box>
                        </Box>
                    </Card>
                </Grid>
            </Grid>
        </>
    )
}

export default MetricProcess
