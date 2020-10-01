import { Box, Button, Grid, makeStyles, Step, StepButton, StepConnector, Stepper, Typography, withStyles } from '@material-ui/core';
import React from 'react';
import { useRouter } from "next/router";
import Layout from '../../../components/common/Layout';
import AddEntry from '../../../components/Referential/Steps/AddEntry';
import ConsumerProcess from '../../../components/Referential/Steps/ConsumerProcess';
import MetaData from '../../../components/Referential/Steps/MetaData';
import Output from '../../../components/Referential/Steps/Output';
import ProcessName from '../../../components/Referential/Steps/ProcessName';
import ReferentialKey from '../../../components/Referential/Steps/ReferentialKey';
import Tracking from '../../../components/Referential/Steps/Tracking';
import Validations from '../../../components/Referential/Steps/Validation';
import { FormikStep, FormikStepper } from '../../../components/common/FormikStepper';
import { useReferentialProcess } from '../../../utils/referential';

const useStyles = makeStyles(theme => ({
    buttonWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginTop: theme.spacing(2),
    },
    forOneButtonWrapper: {
        display: "flex",
        justifyContent: "flex-end",
        marginTop: theme.spacing(2),
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
            backgroundColor: "transparent",
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
        }
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
    },
}))

function getSteps() {
    return ['Process Name', 'Referential Key', 'Consumer Process', 'Add Entry', 'Extra Metadata', 'Output/Notifications', 'Validation', 'Tracking'];
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

export interface CreateReferentialProcessFields {
    idReferential: string
    listAssociatedKeys: any[]
    name: string
    referentialKey: string
    listIdProcessConsumer: any[]
    listMetadata: any[]
    isNotificationChange: boolean
    fieldChangeNotification: string
    timeValidationInSec: number
    isValidationTimeAllField: boolean
    isValidationTimeField: boolean
    fieldChangeValidation: string
    processOutputs: any[]
    trackingOuputs: any[]
    validationOutputs: any[]
    listSelected: any[]
    newEntry: string
    newMetadata: string
}

const CreateReferentialProcess = () => {
    const classes = useStyles();
    const router = useRouter()
    const { referentialId } = router.query
    const steps = getSteps();
    const [step, setStep] = React.useState(0)
    const referentialCtx = useReferentialProcess()
    const {referential} = referentialCtx.state

    React.useEffect(() => {
        referentialCtx.fetchConsumerProcessList()
    }, [])

    React.useEffect(() => {
        console.log("REFERENTIAL ID", referentialId)
        if (referentialId) {
            referentialCtx.fetchReferentialById(referentialId)
        } else {
            referentialCtx.initReferential()
        }
    }, [])

    const onSubmit = (values, actions) => {
        for (var i= 0; i < values.listSelected.length; i++) {
            values.listIdProcessConsumer.push(values.listSelected[i].processDefinition.idProcess);
          }
        referentialCtx.updateReferential(values)
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
                        <Typography variant="h5" component="h5">Referential Process Created!!</Typography>) : (
                            <Box className={classes.seperator}>
                                <Box component="div" className={classes.contentWrapper}>
                                <FormikStepper
                                        enableReinitialize={true}
                                        initialValues={referential}
                                        step={step}
                                        setStep={setStep}
                                        onSubmit={onSubmit}
                                    >
                                        <FormikStep>
                                            <ProcessName />
                                        </FormikStep>
                                        <FormikStep>
                                            <ReferentialKey />
                                        </FormikStep>
                                        <FormikStep>
                                            <ConsumerProcess />
                                        </FormikStep>
                                        <FormikStep>
                                            <AddEntry />
                                        </FormikStep>
                                        <FormikStep>
                                            <MetaData />
                                        </FormikStep>
                                        <FormikStep>
                                            <Output />
                                        </FormikStep>
                                        <FormikStep>
                                            <Validations />
                                        </FormikStep>
                                        <FormikStep>
                                            <Tracking />
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

CreateReferentialProcess.getInitialProps = async () => {
    return {};
};

export default CreateReferentialProcess
