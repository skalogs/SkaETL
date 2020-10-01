import { Box } from '@material-ui/core'
import React from 'react'
import StepHeading from '../../common/StepHeading'
import FormikField from '../../common/FormikField'

const ProcessName = () => {
    return (
        <Box component="div">
            <StepHeading name={"Select Process Name"} />
            <FormikField name={"name"} label={"Process Name"} />
        </Box>
    )
}

export default ProcessName
