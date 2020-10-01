import { Box } from '@material-ui/core'
import React from 'react'
import StepHeading from '../../common/StepHeading'
import FormikField from '../../common/FormikField'

const ReferentialKey = () => {
    return (
        <Box component="div">
            <StepHeading name={"Choose a key Name"} />
            <FormikField name={"referentialKey"} label={"Name of your key"} />
        </Box>
    )
}

export default ReferentialKey