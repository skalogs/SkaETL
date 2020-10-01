import { Box, FormGroup, makeStyles, TextField } from '@material-ui/core'
import React from 'react'
import { useMetricProcess } from '../../../utils/metricProcess'
import FormikSelect from '../../common/FormikSelect'
import StepHeading from '../../common/StepHeading'
import FormikField from '../../common/FormikField'

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
    formGroup: {
        marginBottom: theme.spacing(2),
    },
}))

// const funtionsList = [
//     { id: 1, name: "Count" },
//     { id: 2, name: "Count-Distinct" },
//     { id: 3, name: "Sum" },
//     { id: 4, name: "Avg" },
//     { id: 5, name: "Min" },
//     { id: 6, name: "Max" },
//     { id: 7, name: "Stddev" },className={classes.formGroup}
//     { id: 8, name: "Mean" },
// ]

const MetricFunction = () => {
    const classes = useStyles()
    const metricProcess = useMetricProcess()
    const { functions } = metricProcess.state
    return (
        <Box component="div">
            <StepHeading name={"Choose a function to apply"} />
            <FormGroup className={classes.formGroup}>
                <FormikSelect name="functionName" label="Select window type" items={functions} required />
            </FormGroup>
            <FormikField label={"Field"} name={"functionField"} required />
        </Box>
    )
}

export default MetricFunction
