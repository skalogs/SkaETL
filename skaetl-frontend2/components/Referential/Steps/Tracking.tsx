import { Box, Divider, FormControlLabel, makeStyles, Typography } from '@material-ui/core';
import { Field, useFormikContext } from 'formik';
import { Checkbox } from 'formik-material-ui';
import React from 'react';
import { CreateReferentialProcessFields } from '../../../pages/referential/create';
import FormikSelect from '../../common/FormikSelect';
import OutputStep from '../../common/OutputStep';
import StepHeading from '../../common/StepHeading';

const useStyles = makeStyles({
    checkboxText: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        fontStyle: "normal",
    },
    root: {
        "&$checked": {
            color: "#01B3FF"
        }
    },
    checked: {},
    divider: {
        marginTop: 20,
        marginBottom: 20,
        marginLeft: 0,
        marginRight: 0
    }
})

const Tracking = () => {
    const classes = useStyles()
    const {values, setFieldValue } = useFormikContext<CreateReferentialProcessFields>()
    
    const handleSave = updatedOutputs => {
        setFieldValue("trackingOuputs", updatedOutputs)
    }

    const handleDeleteAction = item => {
        const updatedOutputs = values.trackingOuputs.filter(e => e !== item)
        setFieldValue("trackingOuputs", updatedOutputs)
    }

    return (
        <Box component="div">
            <StepHeading name={"Tracking"} />
            <Box>
                <FormControlLabel
                    control={<Field name="isNotificationChange" type="checkbox" component={Checkbox} />}
                    label={<Typography className={classes.checkboxText}>Notification on change event</Typography>}
                />
                { values.isNotificationChange && <FormikSelect name="fieldChangeNotification" label="Field" items={values.listMetadata} /> }
            </Box>
            <Divider className={classes.divider} variant="middle" />
            <OutputStep processOutputs={values.trackingOuputs} onSave={handleSave} onDelete={handleDeleteAction}/>
        </Box>
    )
}

export default Tracking
