import {
    Box,
    Grid,
    makeStyles,
    Step,
    StepButton,
    StepConnector,
    Stepper,
    Typography,
    withStyles
} from "@material-ui/core";
import { useRouter } from "next/router";
import React from "react";
import * as yup from "yup";
import { FormikStep, FormikStepper } from "../../../components/common/FormikStepper";
import Layout from "../../../components/common/Layout";
import ConsumerTransformations from "../../../components/ConsumerProcess/Steps/ConsumerTransformations";
import ConsumerValidations from "../../../components/ConsumerProcess/Steps/ConsumerValidations";
import Filters from "../../../components/ConsumerProcess/Steps/Filters";
import InputSource from "../../../components/ConsumerProcess/Steps/InputSource";
import Output from "../../../components/ConsumerProcess/Steps/Output";
import Parsers from "../../../components/ConsumerProcess/Steps/Parsers";
import ProcessName from "../../../components/ConsumerProcess/Steps/ProcessName";
import { useConsumerProcess } from "../../../utils/consumerProcess";


const useStyles = makeStyles((theme) => ({
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
        backgroundColor: "#fff",
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
            color: "#01B3FF",
        },
        "& .MuiStepLabel-iconContainer > .MuiStepIcon-active": {
            color: "#01B3FF",
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
        },
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
        marginTop: theme.spacing(12.5),
        padding: theme.spacing(0, 13),
    },
    seperator: {
        position: "absolute",
        right: 0,
        left: 260,
        top: 0,
        background: "#F8FAFB",
        bottom: 0,
    },
}));

const ColorlibConnector = withStyles({
    active: {
        "& $line": {
            borderColor: "#01B3FF",
        },
    },
    completed: {
        "& $line": {
            borderColor: "#01B3FF",
        },
    },
    line: {
        borderColor: "#99AFC73D",
        borderTopWidth: 2,
        borderRadius: 1,
        minHeight: 30,
    },
})(StepConnector);

function getSteps() {
    return [
        "Name",
        "Input",
        "Parsers",
        "Transformations",
        "Validations",
        "Filters",
        "Output",
    ];
}

export interface CreateConsumerProcessFields {
    name: string
    idProcess: string
    processInput: {
        host: string
        port: string
        topicInput: string
    }
    processParser: any[]
    processTransformation: any[]
    processValidation: any[]
    processFilter: any[]
    processOutput: any[]
}
const CreateConsumerProcess = () => {
    const classes = useStyles();
    const router = useRouter()
    const steps = getSteps();

    const [step, setStep] = React.useState(0)

    const { processId } = router.query
    const consumerProcessCtx = useConsumerProcess()
    const { process } = consumerProcessCtx.state
    
    React.useEffect(() => {
        if (processId) {
            consumerProcessCtx.fetchProcessbyId(processId)
        } else {
            consumerProcessCtx.initProcess()
        }
    }, [])

    const onSubmit = (values, actions) => {
        console.log("CREATE CONSUMER PROCESS VALUES", values)
        consumerProcessCtx.saveProcessPost(values)
     };

    return (
        <Layout>
            <Grid container>
                <Grid item xs={3}>
                    <Box component="div" className={classes.headingWrapper}>
                        <Typography variant="h1" component="h1" className={classes.heading}>
                            { processId ? "Edit Process" : "Create Process" }
                        </Typography>
                    </Box>
                    <Stepper
                        className={classes.stepper}
                        activeStep={step}
                        connector={<ColorlibConnector />}
                        orientation="vertical"
                    >
                        {steps.map((label, index) => {
                            return (
                                <Step key={label}>
                                    <StepButton
                                        onClick={() => setStep(index)}
                                    >
                                        {label}
                                    </StepButton>
                                </Step>
                            );
                        })}
                    </Stepper>
                </Grid>
                <Grid item xs={9}>
                    {false ? (
                        <Typography variant="h5" component="h5">
                            Consumer Process Created!!
                        </Typography>
                    ) : (
                            <Box className={classes.seperator}>
                                <Box component="div" className={classes.contentWrapper}>
                                    <FormikStepper
                                        enableReinitialize
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
                                                    processInput: yup.object().shape({
                                                        host: yup.string().required("Required"),
                                                        port: yup.string().required("Required"),
                                                        topicInput: yup.string().required("Required")
                                                    })
                                                })
                                            }
                                        >
                                            <InputSource />
                                        </FormikStep>
                                        <FormikStep>
                                            <Parsers />
                                        </FormikStep>
                                        <FormikStep>
                                            <ConsumerTransformations />
                                        </FormikStep>
                                        <FormikStep>
                                            <ConsumerValidations />
                                        </FormikStep>
                                        <FormikStep>
                                            <Filters />
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
    );
};

CreateConsumerProcess.getInitialProps = async () => {
    return {};
};

export default CreateConsumerProcess;
