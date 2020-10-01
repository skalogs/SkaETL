import { Box, Checkbox, FormGroup, makeStyles, TextField, Typography } from '@material-ui/core';
import CheckBoxIcon from '@material-ui/icons/CheckBox';
import CheckBoxOutlineBlankIcon from '@material-ui/icons/CheckBoxOutlineBlank';
import { Autocomplete } from 'formik-material-ui-lab';
import React from 'react';
import { Field } from "formik";
import StepHeading from '../../common/StepHeading';
import { useReferentialProcess } from '../../../utils/referential';
import { useMetricProcess } from '../../../utils/metricProcess';

const useStyles = makeStyles(theme => ({
    formControl: {
        width: "100%",
        minHeight: 45,
        backgroundColor: "#fff",
        "& .MuiOutlinedInput-root": {
            "& fieldset": {
                border: "1px solid #99AFC780",
            },
            "&:hover": {
                "& fieldset": {
                    border: "1px solid #99AFC780",
                },
            },
            "& input": {
                padding: "0 16px !important",
                height: 27,
                color: "#00274A",
                fontFamily: "'Open Sans', sans-serif",
                fontSize: 14,
                fontWeight: 500,
            }
        },
        "& .MuiOutlinedInput-root.Mui-focused": {
            "& fieldset": {
                border: "1px solid #99AFC780",
            },
        },
    },
    label: {
        color: "#00274ADE",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "18px",
        fontWeight: 600,
        marginBottom: theme.spacing(1),
    },
    formGroup: {
        marginBottom: theme.spacing(2),
    },
    icon: {
        color: "#01B3FF"
    }
}))

const ConsumerProcess = () => {
    const classes = useStyles()
    const referentialCtx = useReferentialProcess()
    const { referential, consumerProcesses } = referentialCtx.state
    const icon = <CheckBoxOutlineBlankIcon fontSize="small" />;
    const checkedIcon = <CheckBoxIcon className={classes.icon} fontSize="small" />;
    return (
        <Box component="div">
            <StepHeading name={"Select the consumer processes sources"} />
            <FormGroup className={classes.formGroup}>
                <Typography variant="subtitle1" component="p" className={classes.label}>Select processes</Typography>
                <Field
                    name="listSelected"
                    multiple
                    component={Autocomplete}
                    disableCloseOnSelect
                    options={consumerProcesses}
                    getOptionLabel={(option: any) => option.processDefinition.name}
                    defaultValue={referential.listSelected}
                    renderOption={(option, { selected }) => {
                        return (
                            <React.Fragment>
                                <Checkbox
                                    icon={<CheckBoxOutlineBlankIcon fontSize="small" />}
                                    checkedIcon={<CheckBoxIcon className={classes.icon} fontSize="small" />}
                                    style={{ marginRight: 8 }}
                                    checked={selected}
                                />
                                {option.processDefinition.name}
                            </React.Fragment>
                        )
                    }}
                    renderInput={(params) => (
                        <TextField className={classes.formControl} {...params} variant="outlined" />
                    )}
                />
            </FormGroup>
        </Box>
    )
}

export default ConsumerProcess
