import { Box, Button, FormGroup, Grid, List, ListItem, makeStyles, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography } from '@material-ui/core';
import LaunchIcon from '@material-ui/icons/Launch';
import SettingsIcon from '@material-ui/icons/Settings';
import { Field, Form, Formik } from 'formik';
import Link from "next/link";
import React from 'react';
import Layout from '../../../components/common/Layout';
import Modal from '../../../components/common/Modal';
import { useSimulation } from '../../../utils/simulation';
import FormikField from '../../../components/common/FormikField';

const downImage = "/static/images/down.png";

const useStyles = makeStyles(theme => ({
    buttonsWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginTop: theme.spacing(4),
    },
    button: {
        color: "#fff",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        height: 40,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
    },
    testAllPatternButton: {
        backgroundColor: "transparent",
        color: "#01B3FF",
        width: 180,
        marginLeft: theme.spacing(2),
        border: "1px solid #01B3FF",
        "&:hover": {
            backgroundColor: "#01B3FF",
            boxShadow: "none",
            color: "#fff",
        }
    },
    manageButton: {
        backgroundColor: "#01B3FF",
        width: 120,
        "&:hover": {
            backgroundColor: "transparent",
            border: "1px solid #01B3FF",
            color: "#01B3FF",
            boxShadow: "none",
        },
    },
    launchSimulationButton: {
        backgroundColor: "#01B3FF",
        width: 215,
        "&:hover": {
            backgroundColor: "transparent",
            border: "1px solid #01B3FF",
            color: "#01B3FF",
            boxShadow: "none",
        },
    },
    icon: {
        width: 20,
        marginLeft: theme.spacing(1),
    },
    table: {
        minWidth: 400,
        marginTop: theme.spacing(4),
        border: "1px solid #99AFC73D",
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
        backgroundColor: "#F5F7F8",
        borderBottom: "1px solid #99AFC73D",
    },
    selectField: {
        width: "100%",
        minHeight: 45,
        padding: theme.spacing(0, 2),
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 500,
        backgroundColor: "#fff",
        border: "1px solid #AABCC480",
        borderRadius: 4,
        appearance: "none",
        backgroundImage: `url(${downImage})`,
        backgroundPosition: "98% center",
        backgroundRepeat: "no-repeat",
        "&:hover": {
            border: "1px solid #01B3FF",
        },
        "&:focus": {
            border: "1px solid #01B3FF",
            outline: "none",
        },
    },
    selectTextarea: {
        minHeight: 45,
        padding: theme.spacing(2),
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 500,
        backgroundColor: "#fff",
        border: "1px solid #AABCC480",
        borderRadius: 4,
        "&:hover": {
            border: "1px solid #01B3FF",
        },
        "&:focus": {
            border: "1px solid #01B3FF",
            outline: "none",
        },
    },
    label: {
        color: "#00274ADE",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "18px",
        fontWeight: 600,
        marginBottom: theme.spacing(1),
    },
    formGroup: {
        marginBottom: theme.spacing(2),
    },
    noDataText: {
        fontSize: 14,
        lineHeight: "19px",
        fontWeight: 600,
        height: 50,
        letterSpacing: "0.42px",
        color: "#636568",
        fontFamily: "'Open Sans', sans-serif",
        textAlign: "center",
        borderBottom: "none",
    },
    launchSimulationLists: {
        marginTop: theme.spacing(2),
        border: "1px solid #F2F4F7",
        padding: theme.spacing(2),
    },
    list: {
        padding: 0,
    },
    listItem: {
        padding: 0,
        marginBottom: theme.spacing(1),
    },
    key: {
        fontSize: 14,
        lineHeight: "19px",
        fontWeight: 600,
        letterSpacing: "0.42px",
        color: "#00274ADE",
        fontFamily: "'Open Sans', sans-serif",
        minWidth: 150,
        marginRight: theme.spacing(2),
    },
    value: {
        fontSize: 14,
        lineHeight: "19px",
        fontWeight: 400,
        letterSpacing: "0.42px",
        color: "#636568",
        fontFamily: "'Open Sans', sans-serif",
    },
}))

const SimulateGrok = () => {
    const classes = useStyles()
    const simulation = useSimulation()
    const [dialogAllPattern, setDialogAllPattern] = React.useState(false)
    const { grokSelect, listGrok, listCapture, listCaptureAllPattern, textSingleData } = simulation.state
    const { isLoading, data } = listGrok

    // console.log("listCapture =>", listCapture.length)

    const onSubmit = (values, actions) => {
        simulation.captureData(values, actions)
    };

    React.useEffect(() => {
        simulation.fetchGrokList()
    }, [])

    const handleTestAllPattern = () => {
        setDialogAllPattern(true)
        simulation.dispatch({ type: "CAPTURED_DATA_ALL_PATTERN", payload: [] })
    }


    const onTestAllPatternSubmit = (values, actions) => {
        simulation.captureDataAllPattern(values, actions)
    }

    const handleTestAllPatternReset = () => {
        simulation.dispatch({ type: "CAPTURED_DATA_ALL_PATTERN", payload: [] })
        simulation.dispatch({ type: "RESET_TEXT_SINGLE_DATA" })
    }

    return (
        <>
            <Modal title={"Test Pattern"} show={dialogAllPattern} modalClosed={() => setDialogAllPattern(false)} isClose={true}>
                <Box>
                    <Grid item xs={12}>
                        <Formik
                            enableReinitialize
                            initialValues={{ textSingleData: textSingleData }}
                            onSubmit={onTestAllPatternSubmit}
                        >
                            {() => (
                                <Form>
                                    <Box component="div">
                                        <FormGroup>
                                            <Typography variant="subtitle1" component="p" className={classes.label}>{"A line of log"}</Typography>
                                            <Field as="textarea" className={classes.selectTextarea} placeholder={""} name={"textSingleData"} />
                                        </FormGroup>
                                    </Box>
                                    <Box component="div" className={classes.buttonsWrapper}>
                                        <Box component="div">
                                            <Button variant="contained" onClick={handleTestAllPatternReset} className={`${classes.button} ${classes.manageButton}`}>Reset</Button>
                                            <Button type="submit" variant="contained" className={`${classes.button} ${classes.testAllPatternButton}`}>Test All Patterns<LaunchIcon className={classes.icon} /></Button>
                                            {/* <Button onClick={() => setDialogAllPattern(false)} variant="contained" className={`${classes.button} ${classes.launchSimulationButton}`}>Close</Button> */}
                                        </Box>
                                    </Box>
                                </Form>
                            )}

                        </Formik>
                    </Grid>
                    <Box component="div">
                        <TableContainer>
                            <Table className={classes.table} aria-label="simple table">
                                <TableHead className={classes.tableHead}>
                                    <TableRow>
                                        <TableCell className={classes.tableHeadCell}>Pattern</TableCell>
                                        <TableCell className={classes.tableHeadCell}>Value</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {listCaptureAllPattern.length !== 0 ? listCaptureAllPattern.map(item => (
                                        <TableRow key={item.pattern}>
                                            <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                {item.pattern}
                                            </TableCell>
                                            <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                                {item.value}
                                            </TableCell>
                                        </TableRow>
                                    )) : <TableRow>
                                            <TableCell colSpan={2} className={classes.noDataText}>No Data Available!</TableCell>
                                        </TableRow>
                                    }
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Box>

                </Box>
            </Modal>

            <Layout>
                <Grid container>
                    <Grid item xs={12}>
                        <Formik
                            enableReinitialize
                            initialValues={{ grokSelect: grokSelect, grokPattern: "", valueList: "" }}
                            onSubmit={onSubmit}
                        >
                            {({ setFieldValue, values }) => (
                                <Form>
                                    <Box component="div">
                                        <Box component="div">
                                            {/* <CustomSelect optionsList={grokList} labelName={"Available grok list"} /> */}
                                            <FormGroup className={classes.formGroup}>
                                                <Typography variant="subtitle1" component="p" className={classes.label}>{"Available Grok List"}</Typography>
                                                <Field as="select" name="grokSelect" className={classes.selectField} onChange={(event) => {
                                                    // console.log("ON CHANGE", event.currentTarget.value)

                                                    setFieldValue("grokSelect", event.currentTarget.value)
                                                    setFieldValue("grokPattern", `${values.grokPattern}%{${event.currentTarget.value}}`)
                                                }}>
                                                    {data.map(item => <option key={item.keyPattern} value={item.keyPattern}>{item.keyPattern}</option>)}
                                                </Field>
                                            </FormGroup>
                                            <FormikField label={"My pattern"} name={"grokPattern"} />
                                            <FormGroup>
                                                <Typography variant="subtitle1" component="p" className={classes.label}>{"Raw data"}</Typography>
                                                <Field as="textarea" className={classes.selectTextarea} placeholder={""} name={"valueList"} />
                                            </FormGroup>
                                        </Box>
                                    </Box>
                                    <Box component="div" className={classes.buttonsWrapper}>
                                        <Box>
                                            <Button type="submit" variant="contained" className={`${classes.button} ${classes.launchSimulationButton}`}>Launch Simulation<LaunchIcon className={classes.icon} /></Button>
                                            <Button variant="contained" onClick={handleTestAllPattern} className={`${classes.button} ${classes.testAllPatternButton}`}>Test All Pattern</Button>
                                        </Box>
                                        <Box component="div">
                                            <Link href="/simulate/grok/view">
                                                <Button variant="contained" className={`${classes.button} ${classes.manageButton}`}>
                                                    <a>Manage</a>
                                                    <SettingsIcon className={classes.icon} />
                                                </Button>
                                            </Link>
                                        </Box>
                                    </Box>
                                </Form>
                            )}
                        </Formik>
                    </Grid>
                    <Grid item xs={12} >
                        {listCapture.length !== 0 && (
                            <Box component="div" className={classes.launchSimulationLists}>
                                {listCapture.map(itemCapture => {
                                    return (
                                        <Box>
                                            <List className={classes.list}>
                                                <ListItem className={classes.listItem}>
                                                    <Typography className={classes.key} variant="h5" component="h5">Status Treatment</Typography>
                                                    <Typography className={classes.value} variant="subtitle1" component="p">{itemCapture.message}</Typography>
                                                </ListItem>
                                                <ListItem className={classes.listItem}>
                                                    <Typography className={classes.key} variant="h5" component="h5">Raw Data</Typography>
                                                    <Typography className={classes.value} variant="subtitle1" component="p">{itemCapture.value}</Typography>
                                                </ListItem>
                                                <ListItem className={classes.listItem}>
                                                    <Typography className={classes.key} variant="h5" component="h5">Transformed</Typography>
                                                    <Typography className={classes.value} variant="subtitle1" component="p">{itemCapture.jsonValue}</Typography>
                                                </ListItem>
                                            </List>
                                        </Box>
                                    )
                                })}
                            </Box>
                        )
                        }
                    </Grid>
                </Grid>
            </Layout>
        </>
    )
}

export default SimulateGrok
