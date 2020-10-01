import { Box, Button, Grid, makeStyles, Step, StepButton, StepConnector, Stepper, Typography, withStyles } from '@material-ui/core';
import React from 'react';
import * as yup from "yup";
import Layout from '../../../components/common/Layout';
import Attribute from '../../../components/LogstashConfiguration/Steps/Attribute';
import Input from '../../../components/LogstashConfiguration/Steps/Input';
import Output from '../../../components/LogstashConfiguration/Steps/Output';
import ConfigurationName from '../../../components/LogstashConfiguration/Steps/ConfigurationName';
import { FormikStepper, FormikStep } from '../../../components/common/FormikStepper';
import { useLogstashConfiguration } from '../../../utils/logstashConfiguration';
import { useRouter } from 'next/router';

const useStyles = makeStyles(theme => ({
    headingWrapper: {
        margin: theme.spacing(0, 0, 2.75, 0),
    },
    heading: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 17,
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
}))

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


function getSteps() {
    return ['Process Name', 'Attribute', 'Input', 'Topic Output'];
}

export interface CreateConfigurationFields {
    idConfiguration: string
    name: string
    input: any[]
    output: any[]
    confData: {
        env: string
        apiKey: string
        category: string
    }
    statusCustomConfiguration: boolean
    customConfiguration: string
    tag: string
    hostOutput: string
    portOutput: string
    topicOutput: string
    codecOutput: string
    typeForced: string
    path: string
    typeInput: string,
    host: string,
    port: string,
    topic: string,
    codec: string,
    listTag: any[]
}

const CreateLogstashProcess = () => {
    const classes = useStyles()
    const router = useRouter()
    const { configurationId } = router.query
    const [ stepsViewTitle, setStepsViewTitle ] = React.useState("Create Configuration")
    const steps = getSteps();
    const [step, setStep] = React.useState(0)
    const logstashCtx = useLogstashConfiguration()
    const { fetchConfigurationbyId, editConfig, createConfig } = logstashCtx

    const { configuration } = logstashCtx.state

    React.useEffect(() => {
        console.log("ID CONFIGURATION", router.query)
        if (configurationId) {
            fetchConfigurationbyId(configurationId)
            setStepsViewTitle("Edit Configuration")
        }
    }, [])

    const onSubmit = (values, actions) => {
        if (configurationId) {
            editConfig(values)
        } else {
            createConfig(values)
        }
    }

    return (
        <Layout>
            <Grid container>
                <Grid item xs={3}>
                    <Box component="div" className={classes.headingWrapper}>
                        <Typography variant="h1" component="h1" className={classes.heading}>{stepsViewTitle}</Typography>
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
                        <Typography variant="h5" component="h5">Logstash Configuration Created!!</Typography>) : (
                            <Box className={classes.seperator}>
                                <Box component="div" className={classes.contentWrapper}>
                                    <FormikStepper
                                        enableReinitialize={true}
                                        initialValues={configuration.configurationLogstash}
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
                                            <ConfigurationName />
                                        </FormikStep>
                                        <FormikStep>
                                            <Attribute />
                                        </FormikStep>
                                        <FormikStep
                                            validationSchema={
                                                yup.object().shape({
                                                    input: yup.array().min(1, "Add at least one tag.").required("Required")
                                                })}
                                        >
                                            <Input />
                                        </FormikStep>
                                        <FormikStep
                                        // validationSchema={
                                        //     yup.object().shape({
                                        //         input: yup.string().required("Required")
                                        //     })}
                                        >
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

CreateLogstashProcess.getInitialProps = async () => {
    return {};
};

export default CreateLogstashProcess
