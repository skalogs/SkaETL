import { Box } from '@material-ui/core'
import React from 'react'
import StepHeading from '../../common/StepHeading'
import FormikField from '../../common/FormikField'

const InputSource = () => {
    return (
        <Box component="div">
            <StepHeading name={"Describe your input source"} />
            <FormikField name={"processInput.host"} label={"Kafka Host"} />
            <FormikField name={"processInput.port"} label={"Kafka Port"} />
            <FormikField name={"processInput.topicInput"} label={"Topic Name"} />
        </Box>
    )
}

export default InputSource
