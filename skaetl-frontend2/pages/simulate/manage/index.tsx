import { Box, Button, Grid, makeStyles, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@material-ui/core';
import DeleteIcon from '@material-ui/icons/Delete';
import Link from "next/link";
import React from 'react';
import ReactPaginate from 'react-paginate';
import Card from '../../../components/common/Card';
import Layout from '../../../components/common/Layout';

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
    },
    buttonsWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginTop: theme.spacing(2),
    },
    reloadButton: {
        color: "#fff",
        backgroundColor: "#FE6847",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        width: 112,
        height: 36,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
        marginRight: theme.spacing(2),
        "&:hover": {
            color: "#fff",
            backgroundColor: "#FE6847",
            boxShadow: "none",
        }
    },
    createGrokPatternButton: {
        color: "#fff",
        backgroundColor: "#01B3FF",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        width: 205,
        height: 36,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
        "&:hover": {
            color: "#fff",
            backgroundColor: "#01B3FF",
            boxShadow: "none",
        }
    },
    paginationWrapper: {
        "& .pagination": {
            display: "flex",
            alignItems: "center",
            margin: 0,
            padding: 0,
            listStyle: "none",
            justifyContent: "center",
            border: "1px solid #99AFC780",
            borderRadius: 4,
            overflow: "hidden",
            "& li.next": {
                textAlign: "center",
                "&:hover": {
                    borderColor: "#007bff"
                },
                "& a": {
                    marginRight: 0,
                    borderRight: 0,
                }
            },
            "& li.previous": {
                textAlign: "center",
                "&:hover": {
                    borderColor: "#007bff"
                },
                "& a": {
                    marginRight: 0,
                }
            },
            "& li.active a": {
                fontWeight: 700,
                color: "#fff",
                letterSpacing: "0.4px",
                backgroundColor: "#007bff",
            },
            "& li a": {
                display: "inline-block",
                fontWeight: 400,
                fontSize: 13,
                lineHeight: "20px",
                cursor: "pointer",
                padding: "6px 12px",
                fontFamily: "'Open Sans', sans-serif",
                color: "#00274A",
                backgroundColor: "#fff",
                borderRight: "1px solid #99AFC780",
                outline: 0,
                "&:hover": {
                    textDecoration: "none",
                }
            }
        }
    },
    tableHead: {
        backgroundColor: "#F5F7F8"
    }
}))

function createData(name, list) {
    return { name, list };
}

const rows = [
    createData('NAGIOS_SERVICE_NOTIFICATION', '%{NAGIOS_TYPE_SERVICE_NOTIFICATION:nagios_type}: %{DATA:nagios_notifyname};%{DATA:nagios_hostname} ;%{DATA:nagios_service};%{DATA:nagios_state};%{DATA:nagios_contact};%{GREEDYDATA:nagios_message}….'),
    createData('POSTFIX_TRIVIAL_REWRITE', '%{POSTFIX_WARNING'),
];

const ManageGrokPattern = () => {
    const classes = useStyles()
    return (
        <Layout>
            <Grid container>
                <Grid item xs={12}>
                    <Card title={"Manage Grok Pattern"} link={""} path={""} isLink={false}>
                        <Box component="div">
                            <TableContainer>
                                <Table className={classes.table} aria-label="simple table">
                                    <TableHead className={classes.tableHead}>
                                        <TableRow>
                                            <TableCell className={classes.tableHeadCell}>Name</TableCell>
                                            <TableCell className={classes.tableHeadCell}>List</TableCell>
                                            <TableCell align="right" className={classes.tableHeadCell}>Action</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {rows.map((row) => (
                                            <TableRow key={row.name}>
                                                <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                    {row.name}
                                                </TableCell>
                                                <TableCell className={classes.tableBodyCell}>{row.list}</TableCell>
                                                <TableCell align="right" className={classes.tableBodyCell}>
                                                    <Button className={classes.actionButton}><DeleteIcon /></Button>
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                            <Box component="div" className={classes.buttonsWrapper}>
                                <Box component="div" className={classes.paginationWrapper}>
                                    <ReactPaginate
                                        previousLabel={<span>&#8249;</span>}
                                        nextLabel={<span>&#8250;</span>}
                                        breakLabel={'...'}
                                        breakClassName={'break-me'}
                                        pageCount={Math.ceil(100 / 10)}
                                        marginPagesDisplayed={2}
                                        pageRangeDisplayed={5}
                                        onPageChange={() => { }}
                                        containerClassName={'pagination'}
                                        // subContainerClassName={'pages pagination'}
                                        activeClassName={'active'}
                                    />
                                </Box>
                                <Box component="div">
                                    <Button variant="contained" className={classes.reloadButton}>Reload</Button>
                                    <Link href="/grokpatternsimulation/creategrokpattern">
                                        <Button variant="contained" className={classes.createGrokPatternButton}>
                                            <a>Create Grok Pattern</a>
                                        </Button>
                                    </Link>
                                </Box>
                            </Box>
                        </Box>
                    </Card>
                </Grid>
            </Grid>
        </Layout>
    )
}

export default ManageGrokPattern
