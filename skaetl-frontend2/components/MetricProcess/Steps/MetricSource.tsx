import { Box, Checkbox, FormGroup, makeStyles, TextField, Typography } from '@material-ui/core';
import CheckBoxIcon from '@material-ui/icons/CheckBox';
import CheckBoxOutlineBlankIcon from '@material-ui/icons/CheckBoxOutlineBlank';
import { ErrorMessage, Field } from "formik";
import { Autocomplete } from 'formik-material-ui-lab';
import React from 'react';
import { useMetricProcess } from '../../../utils/metricProcess';
import StepHeading from '../../common/StepHeading';

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
        // marginBottom: theme.spacing(2),
        minHeight: 95,
    },
    icon: {
        color: "#01B3FF"
    },
    helperText: {
        color: "red",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "18px",
        fontWeight: 400,
        margin: theme.spacing(0.5, 0, 0, 0),
    },
}))


const MetricSource = () => {
    const classes = useStyles()
    const metricProcessCtx = useMetricProcess()
    const process = metricProcessCtx.state.process
    console.log("METRIC SOURCE METRIC PROCESS", metricProcessCtx.state)
    const icon = <CheckBoxOutlineBlankIcon fontSize="small" />;
    const checkedIcon = <CheckBoxIcon className={classes.icon} fontSize="small" />;

    React.useEffect(() => {
        const consumerProcessesList = [...metricProcessCtx.state.listProcess]
        let updatedMetricProcess = { ...process }
        for (var i = 0; i < consumerProcessesList.length; i++) {
            var processConsumer = consumerProcessesList[i];
            if (updatedMetricProcess.sourceProcessConsumers.indexOf(processConsumer.processDefinition.idProcess) >= 0) {
                updatedMetricProcess.selectedProcess.push(processConsumer);
            }

            if (updatedMetricProcess.sourceProcessConsumersB.indexOf(processConsumer.processDefinition.idProcess) >= 0) {
                updatedMetricProcess.selectedProcessB.push(processConsumer);
            }
        }
        metricProcessCtx.dispatch({ type: "UPDATE_PROCESS", payload: updatedMetricProcess })

    }, [metricProcessCtx.state.listProcess])

    return (
        <Box component="div">
            <StepHeading name={"Select process consumers source"} />
            <FormGroup className={classes.formGroup}>
                <Typography variant="subtitle1" component="p" className={classes.label}>Select processes</Typography>
                <Field
                    name="selectedProcess"
                    multiple
                    component={Autocomplete}
                    disableCloseOnSelect
                    options={metricProcessCtx.state.listProcess}
                    getOptionLabel={(option: any) => option.processDefinition.name}
                    defaultValue={process.selectedProcess}
                    // style={{width: 300}}
                    renderOption={(option, { selected }) => {
                        console.log("RENDER OPTION", option, selected)
                        return (
                            <React.Fragment>
                                <Checkbox
                                    icon={icon}
                                    checkedIcon={checkedIcon}
                                    style={{ marginRight: 8 }}
                                    checked={selected}
                                />
                                {option.processDefinition.name}
                                {/* <p>BLAH BLAH</p> */}
                            </React.Fragment>
                        )
                    }}
                    renderInput={(params) => (
                        <TextField className={classes.formControl} {...params} variant="outlined" />
                    )}
                    required
                />
                <Box className={classes.helperText}><ErrorMessage name="selectedProcess" /></Box>
            </FormGroup>
        </Box>
    )
}

export default MetricSource
