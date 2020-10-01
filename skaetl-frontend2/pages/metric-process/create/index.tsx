import { Box, Grid, makeStyles, Step, StepButton, StepConnector, Stepper, Typography, withStyles } from '@material-ui/core';
import { useRouter } from 'next/router';
import React from 'react';
import * as yup from "yup";
import { FormikStep, FormikStepper } from '../../../components/common/FormikStepper';
import Layout from '../../../components/common/Layout';
import FilterCondition from '../../../components/MetricProcess/Steps/FilterCondition';
import HavingResult from '../../../components/MetricProcess/Steps/HavingResult';
import MetricFunction from '../../../components/MetricProcess/Steps/MetricFunction';
import MetricGroupBy from '../../../components/MetricProcess/Steps/MetricGroupBy';
import MetricJoinType from '../../../components/MetricProcess/Steps/MetricJoinType';
import MetricSource from '../../../components/MetricProcess/Steps/MetricSource';
import MetricWindow from '../../../components/MetricProcess/Steps/MetricWindow';
import Output from '../../../components/MetricProcess/Steps/Output';
import ProcessName from '../../../components/MetricProcess/Steps/ProcessName';
import { useMetricProcess } from '../../../utils/metricProcess';

const useStyles = makeStyles(theme => ({
    buttonWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginTop: theme.spacing(4),
    },
    forOneButtonWrapper: {
        display: "flex",
        justifyContent: "flex-end",
        marginTop: theme.spacing(4),
    },
    filledButton: {
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
            backgroundColor: "#fff",
            color: "#01B3FF",
            border: "1px solid #01B3FF",
            boxShadow: "none",
        },
    },
    outlinedButton: {
        backgroundColor: "transparent",
        color: "#01B3FF",
        border: "1px solid #01B3FF",
        "&:hover": {
            backgroundColor: "#01B3FF",
            color: "#fff",
            boxShadow: "none",
        },
    },
    arrowForward: {
        fontSize: 18,
        marginLeft: theme.spacing(1),
    },
    arrowBackward: {
        fontSize: 18,
        marginRight: theme.spacing(1),
    },
    stepper: {
        padding: theme.spacing(3, 0),
        "& .MuiStepLabel-iconContainer > .MuiStepIcon-completed": {
            color: '#01B3FF',
        },
        "& .MuiStepLabel-iconContainer > .MuiStepIcon-active": {
            color: '#01B3FF',
            border: "1px solid #09b2fb",
            borderRadius: "50%",
            padding: theme.spacing(0.25),
            marginLeft: -4,
        },
        "& .MuiStepConnector-vertical": {
            padding: 0,
            marginLeft: "14px",
        },
        "& .MuiStepLabel-iconContainer": {
            paddingRight: theme.spacing(2),
        },
        "& .MuiStepIcon-root": {
            color: "#D9DFE4",
            width: 28,
            height: 28,
        }
    },
    headingWrapper: {
        margin: theme.spacing(0, 0, 2.75, 0),
    },
    heading: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 22,
        fontWeight: 600,
        lineHeight: "30px",
        letterSpacing: "0.44px",
    },
    contentWrapper: {
        margin: theme.spacing(12.5, 0, 4, 0),
        padding: theme.spacing(0, 13),
    },
    seperator: {
        position: "absolute",
        right: 0,
        left: 260,
        top: 0,
        background: "#F8FAFB",
        bottom: 0,
        overflow: "auto"
    }
}))

function getSteps() {
    return ['Name', 'Source', 'Window', 'Function', 'Where', 'Group By', 'Having', 'Join', 'Output'];
}

const ColorlibConnector = withStyles({
    active: {
        '& $line': {
            borderColor: '#01B3FF',
        },
    },
    completed: {
        '& $line': {
            borderColor: '#01B3FF',
        },
    },
    line: {
        borderColor: '#99AFC73D',
        borderTopWidth: 2,
        borderRadius: 1,
        minHeight: 30,
    },
})(StepConnector);

const CreateMetricProcess = () => {
    const classes = useStyles();
    const router = useRouter()
    const { processId } = router.query
    const metricProcessCtx = useMetricProcess()
    const steps = getSteps();
    const { process } = metricProcessCtx.state
    const [step, setStep] = React.useState(0)


    React.useEffect(() => {
        metricProcessCtx.fetchConsumerProcessList()
    }, [])

    React.useEffect(() => {
        (async () => {
            if (processId) {
                try {
                    const { fetchConsumerProcessList, fetchProcessbyIdPromise, dispatch } = metricProcessCtx
                    const p = await fetchProcessbyIdPromise(processId)
                    const mProcess = await p.json()
                    const aggFunctionArr = mProcess.aggFunction.match(/(.*)\((.*)\)/);
                    console.log("PROCESS ===>", process)
                    console.log("MPROCESS ===>", mProcess)
                    let updatedMetricProcess = { ...process, ...mProcess, functionName: aggFunctionArr[2], functionField: aggFunctionArr[2] }
                    // for (var i = 0; i < consumerProcessesList.length; i++) {
                    //     var processConsumer = consumerProcessesList[i];
                    //     if (updatedMetricProcess.sourceProcessConsumers.indexOf(processConsumer.processDefinition.idProcess) >= 0) {
                    //         updatedMetricProcess.selectedProcess.push(processConsumer);
                    //     }

                    //     if (updatedMetricProcess.sourceProcessConsumersB.indexOf(processConsumer.processDefinition.idProcess) >= 0) {
                    //         updatedMetricProcess.selectedProcessB.push(processConsumer);
                    //     }
                    // }
                    console.log("UPDATED")
                    dispatch({ type: "UPDATE_PROCESS", payload: updatedMetricProcess })
                } catch (err) {
                    console.log("ERRROR WHILE FETCHING CONSUMER PROCESSES LIST", err)
                }

            } else {
                const response = await metricProcessCtx.initProcess()
                const data = await response.json()
                const updatedMetricProcess = { ...process, idProcess: data.idProcess, '@class': data['@class']}
                console.log("INIT RESPONSE DATA CHECK", data['@class'], updatedMetricProcess )
                metricProcessCtx.dispatch({ type: "UPDATE_PROCESS", payload: updatedMetricProcess })
            }   
        })()

    }, [processId])

    const onSubmit = (values, actions) => {
        const aggFunction = `${values.functionName}(${values.functionField})`
        const sourceProcessConsumers = values.selectedProcess.map(item => item.id)
        const sourceProcessConsumersB = values.selectedProcessB.map(item => item.id)
        const updatedValues = { ...values, aggFunction, sourceProcessConsumers, sourceProcessConsumersB }
        const response = metricProcessCtx.updateMetricProcess(updatedValues)
        console.log("METRIC UPDATE RESPONSE", response)
        router.push("/metric-process")
    }


    return (
        <Layout>
            <Grid container>
                <Grid item xs={3}>
                    <Box component="div" className={classes.headingWrapper}>
                        <Typography variant="h1" component="h1" className={classes.heading}>Create Process</Typography>
                    </Box>
                    <Stepper className={classes.stepper} activeStep={step} connector={<ColorlibConnector />} orientation="vertical">
                        {steps.map((label, index) => {
                            return (
                                <Step key={label}>
                                    <StepButton onClick={() => setStep(index)}>
                                        {label}
                                    </StepButton>
                                </Step>
                            );
                        })}
                    </Stepper>
                </Grid>
                <Grid item xs={9}>
                    {false ? (
                        <Typography variant="h5" component="h5">Metric Process Created!!</Typography>) : (
                            <Box className={classes.seperator}>
                                <Box component="div" className={classes.contentWrapper}>
                                    <FormikStepper
                                        enableReinitialize={true}
                                        initialValues={process}
                                        step={step}
                                        setStep={setStep}
                                        onSubmit={onSubmit}
                                    >
                                        <FormikStep
                                            validationSchema={
                                                yup.object().shape({
                                                    name: yup.string().required("Required")
                                                })
                                            }
                                        >
                                            <ProcessName />
                                        </FormikStep>
                                        <FormikStep
                                            validationSchema={
                                                yup.object().shape({
                                                    selectedProcess: yup.array().min(1, "Must have at least one item").required("Required")
                                                })}
                                        >
                                            <MetricSource />
                                        </FormikStep>
                                        <FormikStep
                                            validationSchema={
                                                yup.object().shape({
                                                    size: yup.number().moreThan(0, "Must be greater than 0"),
                                                    windowType: yup.string().min(1).required("Required"),
                                                    sizeUnit: yup.string().min(1).required("Required")
                                                })}>
                                            <MetricWindow />
                                        </FormikStep>
                                        <FormikStep
                                            validationSchema={
                                                yup.object().shape({
                                                    functionField: yup.string().required("Required"),
                                                    functionName: yup.string().required("Required"),
                                                })}
                                        >
                                            <MetricFunction />
                                        </FormikStep>
                                        <FormikStep>
                                            <FilterCondition />
                                        </FormikStep>
                                        <FormikStep>
                                            <MetricGroupBy />
                                        </FormikStep>
                                        <FormikStep>
                                            <HavingResult />
                                        </FormikStep>
                                        <FormikStep>
                                            <MetricJoinType />
                                        </FormikStep>
                                        <FormikStep>
                                            <Output />
                                        </FormikStep>
                                    </FormikStepper>
                                </Box>
                            </Box>
                        )}
                </Grid>
            </Grid>
        </Layout>
    )
}


CreateMetricProcess.getInitialProps = async () => {
    return {};
};

export default CreateMetricProcess


