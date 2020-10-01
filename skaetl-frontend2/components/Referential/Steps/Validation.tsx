import { Box, FormControlLabel, makeStyles, Grid, Divider, Typography } from '@material-ui/core'
import { Field, useFormikContext } from 'formik'
import { Checkbox } from 'formik-material-ui';
import React from 'react'
import StepHeading from '../../common/StepHeading'
import FormikSelect from '../../common/FormikSelect';
import OutputStep from '../../common/OutputStep';
import FormikField from '../../common/FormikField';
import { CreateReferentialProcessFields } from '../../../pages/referential/create';

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
    spacingLeft: {
        paddingLeft: "0 !important",
    },
    spacingRight: {
        paddingRight: "0 !important",
    },
    divider: {
        marginTop: 20,
        marginBottom: 20,
        marginLeft: 0,
        marginRight: 0
    }
})

const Validation = () => {
    const classes = useStyles()    
    const { values, setFieldValue } = useFormikContext<CreateReferentialProcessFields>()

    console.log("VALUES ON REFRENTIALS VALIDATION STEP", values)

    const handleSave = updatedOutputs => {
        setFieldValue("validationOutputs", updatedOutputs)
    }

    const handleDeleteAction = item => {
        const updatedOutputs = values.validationOutputs.filter(e => e !== item)
        setFieldValue("validationOutputs", updatedOutputs)
    }


    return (
        <Box component="div">
            <StepHeading name={"Validation"} />
            <Box>
                <FormControlLabel
                    control={<Field name="isValidationTimeAllField" type="checkbox" component={Checkbox} />}
                    label={<Typography className={classes.checkboxText}>Validity event</Typography>}
                />
                { values.isValidationTimeAllField && <FormikField name="timeValidationAllFieldInSec" label="time (sec)" /> }

            </Box>
            <Box>
                <FormControlLabel
                    control={<Field name="isValidationTimeField" type="checkbox" component={Checkbox} />}
                    label={<Typography className={classes.checkboxText}>Validity field</Typography>}
                />
                { values.isValidationTimeField && 
                <Grid container>
                    <Grid item xs={6} className={classes.spacingLeft}>
                        <FormikField label={"time (sec)"} name={"timeValidationFieldInSec"} />
                    </Grid>
                    <Grid item xs={6} className={classes.spacingRight}>
                        <FormikSelect name="fieldChangeValidation" label="Field" items={values.listMetadata} />
                    </Grid>
                </Grid> }
            </Box>
            <Divider className={classes.divider} variant="middle" />
            <OutputStep processOutputs={values.validationOutputs} onSave={handleSave} onDelete={handleDeleteAction}/>
        </Box>
    )
}

export default Validation
