import { Box, Button, Grid, IconButton, InputBase, makeStyles, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@material-ui/core';
import SearchIcon from "@material-ui/icons/Search";
import Link from "next/link";
import React from 'react';
import Layout from '../../../../components/common/Layout';

const useStyles = makeStyles(theme => ({
    tableContainer: {
        border: "1px solid #99AFC73D",
        overflow: "visible",
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
    createGrokButton: {
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
    link: {
        textDecoration: "none",
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
    innerWrapper: {
        display: "flex",
        alignItems: "flex-end",
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
        borderBottom: "none",
        padding: theme.spacing(1.25, 1),
    },
    tableHead: {
        backgroundColor: "#F5F7F8"
    },
    noDataText: {
        fontSize: 14,
        lineHeight: "19px",
        fontWeight: 600,
        letterSpacing: "0.42px",
        color: "#636568",
        fontFamily: "'Open Sans', sans-serif",
        textAlign: "center",
        borderBottom: "none",
    },
    deleteButton: {
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        textTransform: "capitalize",
        color: "#fff",
        backgroundColor: "red",
        borderRadius: 2,
        boxShadow: "none",
        "&:hover": {
            color: "#fff",
            backgroundColor: "red",
            boxShadow: "none",
        }
    },
}))

function createData(pattern, value) {
    return { pattern, value };
}

const rows = [
    createData('ELASTIC SEARCH', 'myNumericField + myOtherNumericField'),
];

const GrokView = () => {
    const classes = useStyles()
    return (
        <Layout>
            <Grid container>
                <Grid item xs={12}>
                    <Box className={classes.wrapper}>
                        <Box className={classes.innerWrapper}>
                            <Paper component="form" className={classes.root}>
                                <InputBase
                                    className={classes.input}
                                    placeholder="Search"
                                    inputProps={{ "aria-label": "search" }}
                                />
                                <IconButton className={classes.iconButton} aria-label="search">
                                    <SearchIcon />
                                </IconButton>
                            </Paper>
                            <Button className={classes.refreshButton}>Refresh</Button>
                        </Box>
                        <Box component="div">
                            <Link href="/simulate/grok/add">
                                <Button variant="contained" className={classes.createGrokButton}>
                                    <a className={classes.link}>Create Grok Pattern</a>
                                </Button>
                            </Link>
                        </Box>
                    </Box>
                    <Box component="div">
                        <TableContainer className={classes.tableContainer}>
                            <Table className={classes.table} aria-label="simple table">
                                <TableHead className={classes.tableHead}>
                                    <TableRow className={classes.tableRow}>
                                        <TableCell className={classes.tableHeadCell}>Pattern</TableCell>
                                        <TableCell className={classes.tableHeadCell}>Value</TableCell>
                                        <TableCell align="right" className={classes.tableHeadCell}>Action</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {rows.length !== 0 ? rows.map(item => (
                                        <TableRow key={item.pattern} className={classes.tableRow}>
                                            <TableCell className={classes.tableBodyCell}>
                                                {item.pattern}
                                            </TableCell>
                                            <TableCell className={classes.tableBodyCell}>
                                                {item.value}
                                            </TableCell>
                                            <TableCell align="right" className={classes.tableBodyCell}>
                                                <Button className={classes.deleteButton}>Delete</Button>
                                            </TableCell>
                                        </TableRow>
                                    )) : <TableCell colSpan={3} className={classes.noDataText}>No Data Available!</TableCell>
                                    }
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Box>
                </Grid>
            </Grid>
        </Layout>
    )
}

export default GrokView
