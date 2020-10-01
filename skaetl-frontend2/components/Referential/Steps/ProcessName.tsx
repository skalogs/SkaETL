import { Box } from '@material-ui/core'
import React from 'react'
import StepHeading from '../../common/StepHeading'
import FormikField from '../../common/FormikField'

const ProcessName = () => {
    return (
        <Box component="div">
            <StepHeading name={"Choose a referential process Name"} />
            <FormikField name={"name"} label={"Name of your process"} />
        </Box>
    )
}

export default ProcessName

