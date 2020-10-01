import { Box, Grid, makeStyles, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@material-ui/core';
import Skeleton from '@material-ui/lab/Skeleton';
import React, { useState } from 'react';
import { useApplicationState } from "../../../utils/application";
import { useDashboardState } from '../../../utils/dashboard';
import DetailModal from '../../common/DetailModal';
import NoData from '../../common/NoData';
import Card from './Card';

const useStyles = makeStyles(theme => ({
    boxStyle: {
        background: "#fff",
        height: 120,
        textAlign: "center",
        padding: theme.spacing(2),
        position: "relative",
        borderRadius: 2,
        border: "1px solid transparent",
        display: "flex",
        alignItems: "center",
        flexDirection: "column",
        marginBottom: theme.spacing(4),
        justifyContent: "center",
        boxShadow: "rgba(0, 0, 0, 0.12) 0px 0px 10px 0",
        cursor: "pointer",
        transition: "all 0.3s ease-in-out",
        // "&:hover": {
        //     boxShadow: "none",
        //     border: "1px solid #01B3FF",
        // },
        // "& .hoverData": {
        //     display: "none",
        //     padding: theme.spacing(0, 1),
        //     transition: "all 0.3s ease-in-out",
        // },
        // "&:hover > .hoverData": {
        //     position: "absolute",
        //     top: 0,
        //     right: 0,
        //     left: "110%",
        //     width: 200,
        //     height: "100%",
        //     zIndex: 9,
        //     display: "block",
        //     backgroundColor: "#00274A",
        //     borderRadius: 4,
        //     transition: "all 0.3s ease-in-out",
        //     "&:before": {
        //         content: "''",
        //         position: "absolute",
        //         top: "50%",
        //         left: -5,
        //         transform: "translate(-50%, -50%)",
        //         borderStyle: "solid",
        //         borderWidth: "10px 10px 10px 0",
        //         borderColor: "transparent #002749 transparent transparent",
        //     },
        // }
    },
    title: {
        color: "#6282A3",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        fontStyle: "normal",
    },
    lastGrid: {
        "& .box": {
            "&:hover > .hoverData": {
                right: "110%",
                left: "auto",
                "&:before": {
                    top: "50%",
                    right: -15,
                    left: "auto",
                    transform: "translate(-50%, -50%)",
                    borderWidth: "10px 0 10px 10px",
                    borderColor: "transparent transparent transparent #002749",
                },
            }
        }
    },
    table: {
        minWidth: 800,
        tableLayout: "fixed",
        // border: "1px solid #99AFC73D",
    },
    tableHeadCell: {
        color: "#6282A3",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 400,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        padding: theme.spacing(1.25, 1),
        borderBottom: "none",
        // textAlign: "center",
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
        // textAlign: "center",
        padding: theme.spacing(1.25, 1),
    },
    tableHead: {
        backgroundColor: "#F5F7F8",
    },
    tableScroll: {
        overflow: "auto",
        maxHeight: 565,
    }
}))

const TopSection = () => {
    const classes = useStyles()
    const { state: { home, dataCapture, consumerProcessList, metricProcessList } } = useDashboardState()
    const { isSidebarOpen } = useApplicationState()
    const [isShow, setIsShow] = useState(false)
    const [selectedId, setSelectedId] = useState("")

    const handleModal = (id: string) => {
        if (home.data) {
            setSelectedId(id)
            setIsShow(true)
        }
    }
    const { data } = home
    const dataChartsData = dataCapture.data
    // const dataChartsData = null // Testing something,use above line
    console.log("HOME TOPSECTION", home, dataCapture)

    return (
        <>
            <DetailModal title={"Details"} show={isShow} modalClosed={() => setIsShow(false)} isSidebarOpen={isSidebarOpen}>
                <DetailTable
                    id={selectedId}
                    data={data}
                />
            </DetailModal>

            <Grid container>
                <Grid item xs={2} onClick={() => handleModal("configuration")}>
                    {data
                        ? (<Card
                            title={"Configuration"}
                            isHoverData={true}
                            total={data.numberConfigurationTotal}
                            active={data.numberConfigurationActive}
                            inActive={data.numberConfigurationDeActive}
                            error={data.numberConfigurationError}
                            init={data.numberConfigurationInit}
                        />)
                        : (<Box className={`${classes.boxStyle} box`} component="div">
                            <Skeleton animation="wave" width={40} height={10} style={{ marginBottom: 16 }} />
                            <Typography className={classes.title} variant="subtitle1" component="h5">
                                {"Configuration"}
                            </Typography>
                        </Box>)
                    }
                </Grid>
                <Grid item xs={2} onClick={() => handleModal("consumer")}>
                    {data
                        ? (<Card
                            title={"Consumer"}
                            isHoverData={true}
                            total={data.numberProcessTotal}
                            active={data.numberProcessActive}
                            inActive={data.numberProcessDeActive}
                            error={data.numberProcessError}
                            init={data.numberProcessInit}
                            degraded={data.numberProcessDegraded}
                            creation={data.numberProcessCreation}
                        />)
                        : (<Box className={`${classes.boxStyle} box`} component="div">
                            <Skeleton animation="wave" width={40} height={10} style={{ marginBottom: 16 }} />
                            <Typography className={classes.title} variant="subtitle1" component="h5">
                                {"Consumer"}
                            </Typography>
                        </Box>)
                    }
                </Grid>
                <Grid item xs={2} onClick={() => handleModal("metric")}>
                    {data
                        ? (<Card
                            title={"Metric"}
                            isHoverData={true}
                            total={data.numberMetricTotal}
                            active={data.numberMetricActive}
                            inActive={data.numberMetricDeActive}
                            error={data.numberMetricError}
                            init={data.numberMetricInit}
                            degraded={data.numberMetricDegraded}
                            creation={data.numberMetricCreation}
                        />)
                        : (<Box className={`${classes.boxStyle} box`} component="div">
                            <Skeleton animation="wave" width={40} height={10} style={{ marginBottom: 16 }} />
                            <Typography className={classes.title} variant="subtitle1" component="h5">
                                {"Metric"}
                            </Typography>
                        </Box>)
                    }
                </Grid>
                <Grid item xs={2} onClick={() => handleModal("referential")}>
                    {data
                        ? (<Card
                            title={"Referential"}
                            isHoverData={true}
                            total={data.numberReferentialTotal}
                            active={data.numberReferentialActive}
                            inActive={data.numberReferentialDeActive}
                            error={data.numberReferentialError}
                            init={data.numberReferentialInit}
                            degraded={data.numberReferentialDegraded}
                            creation={data.numberReferentialCreation}
                        />)
                        : (<Box className={`${classes.boxStyle} box`} component="div">
                            <Skeleton animation="wave" width={40} height={10} style={{ marginBottom: 16 }} />
                            <Typography className={classes.title} variant="subtitle1" component="h5">
                                {"Referential"}
                            </Typography>
                        </Box>)
                    }
                </Grid>
                <Grid item xs={2} className={classes.lastGrid} onClick={() => handleModal("worker")}>
                    {data
                        ? (<Card
                            title={"Worker"}
                            isHoverData={true}
                            total={data.numberWorkerTotal}
                            process={data.numberWorkerProcess}
                            metric={data.numberWorkerMetric}
                            referential={data.numberWorkerReferential}

                        />)
                        : (<Box className={`${classes.boxStyle} box`} component="div">
                            <Skeleton animation="wave" width={40} height={10} style={{ marginBottom: 16 }} />
                            <Typography className={classes.title} variant="subtitle1" component="h5">
                                {"Worker"}
                            </Typography>
                        </Box>)
                    }
                </Grid>
                <Grid item xs={2} className={classes.lastGrid} onClick={() => handleModal("logstash")}>
                    {dataChartsData
                        ? (<Card
                            title={"Client Logstash"}
                            isHoverData={true}
                            total={dataChartsData.numberAllClientConfiguration}
                            all={dataChartsData.numberAllClientConfiguration}
                            production={dataChartsData.numberProdClientConfiguration}
                            error={dataChartsData.numberErrorClientConfiguration}
                        />)
                        : (<Box className={`${classes.boxStyle} box`} component="div">
                            <Skeleton animation="wave" width={40} height={10} style={{ marginBottom: 16 }} />
                            <Typography className={classes.title} variant="subtitle1" component="h5">
                                {"Client Logstash"}
                            </Typography>
                        </Box>)
                    }
                </Grid>
            </Grid>
        </>
    )
}


const DetailTable = ({ id, data }) => {
    const classes = useStyles()
    console.log("Data Modal =>", data)
    switch (id) {
        case "consumer":
            const { listStatProcess } = data
            return (
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Process Name</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Process Status</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Number of Read</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Number of Processiing</TableCell>
                            </TableRow>
                        </TableHead>
                    </Table>
                    <Box className={classes.tableScroll}>
                        <Table className={classes.table}>
                            {listStatProcess.length === 0 ? (<NoData text={"No data available!"} cols={4} />) : (
                                <TableBody>
                                    {listStatProcess.map(row => (
                                        <TableRow key={row.id}>
                                            <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                {row.name}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.status}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.nbRead}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.nbOutput}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            )}
                        </Table>
                    </Box>
                </TableContainer>

            )
        case "metric":
            const { listStatMetric } = data
            return (
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Process Name</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Process Status</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Nb Input Todo </TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Number of Processing</TableCell>
                            </TableRow>
                        </TableHead>
                    </Table>
                    <Box className={classes.tableScroll}>
                        <Table className={classes.table}>
                            {listStatMetric.length === 0 ? (<NoData text={"No data available!"} cols={4} />) : (
                                <TableBody>
                                    {listStatMetric.map(row => (
                                        <TableRow key={row.id}>
                                            <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                {row.name}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.status}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.nbInput}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.nbOutput || "--"}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            )}
                        </Table>
                    </Box>
                </TableContainer>

            )
        case "logstash":
            const { listStatClient } = data
            return (
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Client hostname</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Activity date</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Client Environment</TableCell>
                            </TableRow>
                        </TableHead>
                    </Table>
                    <Box className={classes.tableScroll}>
                        <Table className={classes.table}>
                            {listStatClient.length === 0 ? (<NoData text={"No data available!"} cols={4} />) : (
                                <TableBody>
                                    {listStatClient.map(row => (
                                        <TableRow key={row.id}>
                                            <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                {row.hostname}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.dateActivity}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.env}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            )}
                        </Table>
                    </Box>
                </TableContainer>

            )
        case "configuration":
            const { listStatConfiguration } = data
            return (
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Configuration Name</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Configuration Status</TableCell>
                            </TableRow>
                        </TableHead>
                    </Table>
                    <Box className={classes.tableScroll}>
                        <Table className={classes.table}>
                            {listStatConfiguration.length === 0 ? (<NoData text={"No data available!"} cols={4} />) : (
                                <TableBody>
                                    {listStatConfiguration.map(row => (
                                        <TableRow key={row.id}>
                                            <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                {row.name}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.status}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            )}
                        </Table>
                    </Box>
                </TableContainer>

            )
        case "referential":
            const { listStatReferential } = data
            return (
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Referential Name</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Referential Status</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Number of Processing</TableCell>
                            </TableRow>
                        </TableHead>
                    </Table>
                    <Box className={classes.tableScroll}>
                        <Table className={classes.table}>
                            {listStatReferential.length === 0 ? (<NoData text={"No data available!"} cols={4} />) : (
                                <TableBody>
                                    {listStatReferential.map(row => (
                                        <TableRow key={row.id}>
                                            <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                {row.name}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.status}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.nbProcess}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            )}
                        </Table>
                    </Box>
                </TableContainer>

            )
        case "worker":
            const { listStatWorker } = data
            return (
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Worker Name</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Worker IP address</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Number of Processing</TableCell>
                                <TableCell className={classes.tableHeadCell} align="center">Worker Type</TableCell>
                            </TableRow>
                        </TableHead>
                    </Table>
                    <Box className={classes.tableScroll}>
                        <Table className={classes.table}>
                            {listStatWorker.length === 0 ? (<NoData text={"No data available!"} cols={4} />) : (
                                <TableBody>
                                    {listStatWorker.map(row => (
                                        <TableRow key={row.id}>
                                            <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                {row.name}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.ip}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.nbProcess}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell} align="center">
                                                {row.type}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            )}
                        </Table>
                    </Box>
                </TableContainer>

            )
        default:
            return null
    }
}

export default TopSection
