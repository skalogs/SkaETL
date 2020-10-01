import { Box } from '@material-ui/core';
import React from 'react';
import StepHeading from '../../common/StepHeading';
import FormikField from '../../common/FormikField';

const HavingResult = () => {
    return (
        <Box component="div">
            <StepHeading name={"Define an output condition"} optional={"(Optional)"} />
            <FormikField label={"Having Result"} subLabel={"( A condition such as >= 42)"} name={"having"} />
        </Box>
    )
}

export default HavingResult
