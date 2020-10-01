import { Box } from '@material-ui/core';
import React from 'react';
import StepHeading from '../../common/StepHeading';
import FormikField from '../../common/FormikField';

const MetricGroupBy = () => {
    return (
        <Box component="div">
            <StepHeading name={"Select group by fields"} optional={"(Optional)"} />
            <FormikField label={"Group By Field"} subLabel={"(Comma separated)"} name={"groupBy"} />
        </Box>
    )
}

export default MetricGroupBy
