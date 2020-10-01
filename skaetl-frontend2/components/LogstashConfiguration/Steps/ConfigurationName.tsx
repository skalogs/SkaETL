import { Box } from '@material-ui/core'
import React from 'react'
import FormikField from '../../common/FormikField'
import StepHeading from '../../common/StepHeading'

const ConfigurationName = () => {
    return (
        <Box component="div">
            <StepHeading name={"Choose a Logstash configuration name"} />
            <FormikField label={"Name of your configuration"} name={"name"}/>
        </Box>
    )
}

export default ConfigurationName
