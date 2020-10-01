import { Box, Button, FormGroup, Grid, List, ListItem, makeStyles, TextField, Typography } from '@material-ui/core';
import { Field, Form, Formik } from 'formik';
import React from 'react';
import Layout from '../../../components/common/Layout';
import { useSimulation } from '../../../utils/simulation';
import { useRouter } from 'next/router';
import FormikSelect from '../../../components/common/FormikSelect';
import FormikField from '../../../components/common/FormikField';

const downImage = "/static/images/down.png";

const useStyles = makeStyles(theme => ({
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
    buttonsWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "flex-start",
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
    simulateText: {
        backgroundColor: "rgb(224, 224, 224)",
        color: "rgba(0, 0, 0, 0.87)",
        marginRight: theme.spacing(2),
        padding: theme.spacing(1, 3),
        borderRadius: 25,
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
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
    formWrapper: {
        margin: theme.spacing(3, 0),
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
const SimulateView = () => {
    const classes = useStyles()
    const router = useRouter()
    const simulationCtx = useSimulation()
    const { simulateView } = simulationCtx.state
    const { processId } = router.query

    console.log("SIMULATE VIEW", simulateView)
    React.useEffect(() => {
        if (processId) {
            simulationCtx.fetchProcessbyId(processId)
        }
    }, [processId])

    const handleSubmit = async (values, actions) => {
        if (!simulateView.simulationActive && values.typeSimulation === "Kafka") {
            const _ = simulationCtx.launchSimulate(simulateView.process)
            simulationCtx.dispatch({ type: "SET_AFTER_LAUNCH_SIMULATE" })
            simulationCtx.launchCaptureKafka(values, actions)
        } else {
            simulationCtx.launchCaptureText(values, actions, simulateView.process)
        }
    }


    return (
        <Layout>
            <Grid container>
                <Formik
                    enableReinitialize
                    initialValues={{
                        typeSimulation: "Kafka",
                        hostInput: simulateView.hostInput,
                        portInput: simulateView.portInput,
                        topicInput: simulateView.topicInput,
                        maxRecordsInput: simulateView.maxRecordsInput,
                        pollingTimeInput: simulateView.pollingTimeInput,
                        textSubmit: simulateView.textRawData,
                    }}
                    onSubmit={handleSubmit}
                >
                    {({ values }) => (
                        <Grid item xs={12}>
                            <Form>
                                <Grid item xs={12}>
                                    <Box component="div">
                                        <Box component="div">
                                            <Grid container>
                                                <Grid item xs={12}>
                                                    <FormGroup className={classes.formGroup}>
                                                        {/* <Typography variant="subtitle1" component="p" className={classes.label}>{"Type"}</Typography> */}
                                                        {/* <Field as="select" name="grokSelect" className={classes.selectField}>
                                                        {typeSimulation.map(item => <option key={item.id} value={item.value}>{item.value}</option>)}
                                                    </Field> */}
                                                        <FormikSelect name="typeSimulation" label="Type" items={simulateView.typeSimulation} />
                                                    </FormGroup>
                                                </Grid>
                                            </Grid>
                                        </Box>
                                        {values.typeSimulation === "Kafka" ? (
                                            <Box className={classes.formWrapper}>
                                                <Grid container>
                                                    <Grid item xs={6}>
                                                        <FormikField name={"hostInput"} label={"Host"} />
                                                    </Grid>
                                                    <Grid item xs={6}>
                                                        <FormikField name={"pollingTimeInput"} label={"Polling Time (ms)"} />
                                                    </Grid>
                                                    <Grid item xs={6}>
                                                        <FormikField name={"portInput"} label={"Port"} />
                                                    </Grid>
                                                    <Grid item xs={6}>
                                                        <FormikField name={"maxRecordsInput"} label={"Max records"} />
                                                    </Grid>
                                                    <Grid item xs={6}>
                                                        <FormikField name={"topicInput"} label={"Topic"} />
                                                    </Grid>
                                                </Grid>
                                            </Box>
                                        ) : (
                                                <Box className={classes.formWrapper}>
                                                    <Grid container>
                                                        <Grid item xs={12}>
                                                            <FormGroup className={classes.formGroup}>
                                                                <Typography variant="subtitle1" component="p" className={classes.label}>{"Raw Data"}</Typography>
                                                                <Field as="textarea" name="textSubmit" className={classes.selectTextarea} />
                                                            </FormGroup>
                                                        </Grid>
                                                    </Grid>
                                                </Box>
                                            )}
                                    </Box>
                                    <Box component="div" className={classes.buttonsWrapper}>
                                        <Typography variant="subtitle1" component="p" className={classes.simulateText}>{simulateView.messageActive}</Typography>
                                        <Button type="submit" variant="contained" className={`${classes.button} ${classes.launchSimulationButton}`}>Launch Simulation</Button>
                                    </Box>
                                </Grid>
                                <Grid item xs={12}>
                                        <Box component="div" className={classes.launchSimulationLists}>
                                            {simulateView.listCapture.map(itemCapture => {
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
                                </Grid>
                                <Grid item xs={12}>
                                    {(values.typeSimulation !== "Kafka" && simulateView.itemText) && (
                                        <Box component="div" className={classes.launchSimulationLists}>
                                                    <Box>
                                                        <List className={classes.list}>
                                                            <ListItem className={classes.listItem}>
                                                                <Typography className={classes.key} variant="h5" component="h5">Status Treatment</Typography>
                                                                <Typography className={classes.value} variant="subtitle1" component="p">{simulateView.itemText.message}</Typography>
                                                            </ListItem>
                                                            <ListItem className={classes.listItem}>
                                                                <Typography className={classes.key} variant="h5" component="h5">Raw Data</Typography>
                                                                <Typography className={classes.value} variant="subtitle1" component="p">{simulateView.itemText.value}</Typography>
                                                            </ListItem>
                                                            <ListItem className={classes.listItem}>
                                                                <Typography className={classes.key} variant="h5" component="h5">Transformed</Typography>
                                                                <Typography className={classes.value} variant="subtitle1" component="p">{simulateView.itemText.jsonValue}</Typography>
                                                            </ListItem>
                                                        </List>
                                                    </Box>
                                            </Box>
                                    )}
                                </Grid>

                            </Form>
                        </Grid>
                    )}
                </Formik>
            </Grid>
        </Layout>
    )
}

export default SimulateView
