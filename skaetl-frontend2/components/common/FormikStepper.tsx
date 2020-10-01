import { Box, Button, makeStyles } from '@material-ui/core';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import ArrowForwardIcon from '@material-ui/icons/ArrowForward';
import { Form, Formik, FormikConfig, FormikValues } from 'formik';
import * as React from "react";

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
}))


export interface FormikStepProps extends Pick<FormikConfig<FormikValues>, "children" | "validationSchema"> { }

export function FormikStep({ children }: FormikStepProps) {
    return (
        <Box component="div">
            {children}
        </Box>
    )
}



export function FormikStepper({ children, enableReinitialize, step, setStep, initialValues, onSubmit }) {
    const childrenArray = React.Children.toArray(children) as React.ReactElement<FormikStepProps>[]
    const currentChild = childrenArray[step] as React.ReactElement<FormikStepProps>;
    const classes = useStyles()

    const isLastStep = () => {
        return step === childrenArray.length - 1
    }

    const handleSubmit = async (values, actions) => {
        if (isLastStep()) {
            await onSubmit(values, actions)
        } else {
            setStep(s => s + 1)
        }
    }

    return (
        <Formik
            enableReinitialize={enableReinitialize}
            initialValues={initialValues}
            validationSchema={currentChild.props.validationSchema}
            onSubmit={handleSubmit} >
            <Form autoComplete="off">
                {currentChild}
                <Box component="div" className={step == 0 ? classes.forOneButtonWrapper : classes.buttonWrapper}>
                    {step > 0 ? (
                        <Button
                            onClick={() => setStep(s => s - 1)}
                            className={`${classes.filledButton} ${classes.outlinedButton}`}
                        >
                            <ArrowBackIcon className={classes.arrowBackward} />
                            Previous
                        </Button>
                    ) : null}
                    <Button type={"submit"} variant="contained" className={classes.filledButton}>
                        {isLastStep() ? 'Save' : 'Next'}
                        {isLastStep() ? null : <ArrowForwardIcon className={classes.arrowForward} />}
                    </Button>
                </Box>
            </Form>

        </Formik>
    )
}