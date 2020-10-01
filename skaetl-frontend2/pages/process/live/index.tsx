import { Box, Button, Grid, makeStyles, TextField, Typography, LinearProgress } from '@material-ui/core'
import TreeView from '@material-ui/lab/TreeView';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import TreeItem from '@material-ui/lab/TreeItem';
import { Form, Formik } from 'formik'
import React from 'react'
import { useRouter } from "next/router"
import Layout from '../../../components/common/Layout'
import { useProcess } from '../../../utils/process'
import FormikField from '../../../components/common/FormikField';

const useStyles = makeStyles(theme => ({
    spacingLeft: {
        paddingLeft: "0 !important",
    },
    spacingRight: {
        paddingRight: "0 !important",
    },
    buttonsWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "flex-end",
        marginTop: theme.spacing(2),
        marginBottom: theme.spacing(2),
    },
    propertiesValueWrapper: {
        display: "flex",
        alignItems: "center",
    },
    resetButton: {
        backgroundColor: "transparent",
        border: "1px solid #01B3FF",
        color: "#01B3FF",
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
        textTransform: "capitalize",
        marginRight: theme.spacing(2),
        "&:hover": {
            color: "#fff",
            backgroundColor: "#01B3FF",
            boxShadow: "none",
        },
    },
    liveTopicButton: {
        color: "#fff",
        backgroundColor: "#01B3FF",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 600,
        width: 112,
        height: 40,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
        "&:hover": {
            backgroundColor: "transparent",
            border: "1px solid #01B3FF",
            color: "#01B3FF",
            boxShadow: "none",
        }
    },
    dataLabel: {
        color: "#00274ADE",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 600,
        marginRight: theme.spacing(2),
        display: "flex",
        alignItems: "center",
    },
    dataValue: {
        color: "#6282A3",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
    }
}))

interface RenderTree {
    id: string;
    name: string;
    children?: RenderTree[];
}

const ProcessLive = () => {
    const classes = useStyles()
    const router = useRouter()
    const processContext = useProcess()
    const { listCapture, loadingValue, windowTimeInput, maxRecordsInput, deserializer } = processContext.state
    const { topic, hostInput, portInput, offsetInput } = router.query

    const reset = () => {
        processContext.dispatch({ type: "RESET_LIST_CAPTURE" })
    }
    const onSubmit = (values, actions) => {
        processContext.dispatch({ type: "RESET_LIST_CAPTURE" })
        processContext.launchCaptureKafka(values, actions)
    };

    console.log("LIST CAPTURE", listCapture)

    const renderTree = (nodes: RenderTree) => (
        <TreeItem key={nodes.id} nodeId={nodes.id} label={nodes.name}>
            {Array.isArray(nodes.children) ? nodes.children.map((node) => renderTree(node)) : null}
        </TreeItem>
    );

    return (
        <Layout>
            <Grid container>
                <Grid item xs={12}>
                    <Formik
                        enableReinitialize
                        initialValues={{ host: hostInput, port: portInput, topic: topic, window_time: windowTimeInput, max_records: maxRecordsInput, offset: offsetInput, deserializer_value: deserializer }}
                        onSubmit={onSubmit}
                    >
                        {() => (
                            <Form>
                                <Box component="div">
                                    <Grid container>
                                        <Grid item xs={4} className={classes.spacingLeft}>
                                            <FormikField label={"Host"} name={"host"}  />
                                        </Grid>
                                        <Grid item xs={4}>
                                            <FormikField label={"Port"} name={"port"}  />
                                        </Grid>
                                        <Grid item xs={4} className={classes.spacingRight}>
                                            <FormikField label={"Topic"} name={"topic"}  />
                                        </Grid>
                                        <Grid item xs={4} className={classes.spacingLeft}>
                                            <FormikField label={"Window Time (ms)"} type={"number"} name={"window_time"}  />
                                        </Grid>
                                        <Grid item xs={4}>
                                            <FormikField label={"Max Records"} type={"number"} name={"max_records"}  />
                                        </Grid>
                                        <Grid item xs={4} className={classes.spacingRight}>
                                            <FormikField label={"Offset"} name={"offset"}  />
                                        </Grid>
                                        <Grid item xs={12} className={`${classes.spacingLeft} ${classes.spacingRight}`}>
                                            <FormikField label={"Deserializer Value"} placeholder={"org.apache.kafka.common.serialization.StringDeserializer"} type={"text"} name={"deserializer_value"}  />
                                        </Grid>
                                    </Grid>
                                </Box>
                                <Box component="div" className={classes.buttonsWrapper}>
                                    <Box component="div">
                                        <Button onClick={reset} variant="contained" className={classes.resetButton}>Reset</Button>
                                        <Button type="submit" variant="contained" className={classes.liveTopicButton}>
                                            Live Topic
                                        </Button>
                                    </Box>
                                </Box>
                            </Form>
                        )}
                    </Formik>
                    <Box>
                        {loadingValue ? <LinearProgress /> :
                            (
                                <TreeView
                                    defaultCollapseIcon={<ExpandMoreIcon />}
                                    defaultExpandIcon={<ChevronRightIcon />}
                                >
                                    {renderTree(listCapture)}
                                </TreeView>
                            )}
                        {/* <Box component="div" className={classes.propertiesValueWrapper}>
                             <Typography variant="h6" component="h6" className={classes.dataLabel}><ArrowRightIcon />"Data" : </Typography>
                             <Typography variant="subtitle2" component="span" className={classes.dataValue}>0 properties</Typography>
                        </Box> */}
                    </Box>
                </Grid>
            </Grid>
        </Layout>
    )
}

export default ProcessLive
