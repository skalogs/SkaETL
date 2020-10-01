import { Box, FormGroup, Grid, makeStyles, TextField, Typography } from '@material-ui/core'
import { Field } from 'formik'
import React from 'react'
import { useMetricProcess } from '../../../utils/metricProcess'
// import CustomSelect from '../../common/CustomSelect'
import StepHeading from '../../common/StepHeading'
import FormikSelect from '../../common/FormikSelect'
import FormikField from '../../common/FormikField'

const downImage = "/static/images/down.png";

const useStyles = makeStyles(theme => ({
    spacingleft: {
        paddingLeft: "0 !important",
    },
    spacingRight: {
        paddingRight: "0 !important",
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
        backgroundPosition: "97% center",
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
}))

// const windowList = [
//     { id: 1, name: "Tumbling" },
//     { id: 2, name: "Hopping" },
//     { id: 3, name: "Session" },
// ]
// const windowTimeList = [
//     { id: 1, name: "15 minutes" },
//     { id: 2, name: "30 minutes" },
//     { id: 3, name: "60 minutes" },
//     { id: 4, name: "120 minutes" },
// ]

const MetricWindow = () => {
    const metricProcess = useMetricProcess()
    const { windowTypes, timeunits } = metricProcess.state
    const classes = useStyles()
    return (
        <Box component="div">
            <StepHeading name={"Choose the window for the computation"} />
            <FormGroup className={classes.formGroup}>
                <FormikSelect name="windowType" label="Select window type" items={windowTypes} required/>
            </FormGroup>
            <Grid container>
                <Grid item xs={6} className={classes.spacingleft}>
                    <FormikField label={"Size"} type={"number"} name={"size"} />
                </Grid>
                <Grid item xs={6} className={classes.spacingRight}>
                    <FormGroup className={classes.formGroup}>
                        <FormikSelect name="sizeUnit" label="Select Window TimeUnit" items={timeunits} required/>
                    </FormGroup>
                </Grid>
            </Grid>
        </Box>
    )
}

export default MetricWindow
