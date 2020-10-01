import { Box } from '@material-ui/core'
import React from 'react'
import FormikField from '../../common/FormikField'
import StepHeading from '../../common/StepHeading'

const ProcessName = () => {
    return (
        <Box component="div">
            <StepHeading name={"Choose a metric name"} />
            <FormikField name={"name"} label={"Name of your process"} />
        </Box>
    )
}

export default ProcessName
