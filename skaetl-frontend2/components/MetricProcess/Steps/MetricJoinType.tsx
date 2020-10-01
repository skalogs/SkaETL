/* eslint-disable */
import { Box, Checkbox, FormGroup, Grid, makeStyles, TextField, Typography } from '@material-ui/core';
import CheckBoxIcon from '@material-ui/icons/CheckBox';
import CheckBoxOutlineBlankIcon from '@material-ui/icons/CheckBoxOutlineBlank';
import { Field, useFormikContext } from "formik";
import { Autocomplete } from 'formik-material-ui-lab';
import React from 'react';
import { useMetricProcess } from '../../../utils/metricProcess';
import StepHeading from '../../common/StepHeading';
import FormikSelect from '../../common/FormikSelect';
import FormikField from '../../common/FormikField';

const downImage = "/static/images/down.png";

const useStyles = makeStyles(theme => ({
    dropdownMenu: {
        position: "relative",
        transition: "all 0.3s ease-in-out",
        // "& .MuiGrid-item": {
        //     padding: "0 !important",
        // },
    },
    title: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 18,
        lineHeight: "20px",
        fontWeight: 600,
        marginBottom: theme.spacing(2.5),
    },
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
    },
    selectField: {
        width: "100%",
        minHeight: 45,
        padding: theme.spacing(0, 2),
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 500,
        backgroundColor: "#fff",
        border: "1px solid #AABCC480",
        borderRadius: 4,
        appearance: "none",
        backgroundImage: `url(${downImage})`,
        backgroundPosition: "97% center",
        backgroundRepeat: "no-repeat",
        "&:hover": {
            border: "1px solid #01B3FF",
        },
        "&:focus": {
            border: "1px solid #01B3FF",
            outline: "none",
        },
    },
}))

// const joinTypeList = [
//     { id: 1, name: "None" },
//     { id: 2, name: "Inner" },
//     { id: 3, name: "Outer" },
//     { id: 4, name: "Left" },
// ]

const MetricJoinType = () => {
    const classes = useStyles()
    const metricProcessCtx = useMetricProcess()
    const { joinTypes, process, timeunits } = metricProcessCtx.state
    const { values } = useFormikContext<any>()

    return (
        <>
            <Box component="div">
                <StepHeading name={"Corralate with another dataset"} optional={"(Optional)"} />
                <FormGroup className={classes.formGroup}>
                    <FormikSelect name="joinType" label="Select join type" items={joinTypes} required/>

                </FormGroup>
            </Box>
            {values.joinType === "NONE" ? null : (
                <Box component="div" className={classes.dropdownMenu}>
                    <Grid container>
                        <Grid item xs={12}>
                            <FormGroup className={classes.formGroup}>
                                <Typography variant="h4" component="h4" className={classes.title}>Join on process consumers</Typography>
                                <Field
                                    name="selectedProcessB"
                                    multiple
                                    component={Autocomplete}
                                    disableCloseOnSelect
                                    options={metricProcessCtx.state.listProcess}
                                    getOptionLabel={(option: any) => option.processDefinition.name}
                                    defaultValue={process.selectedProcessB}
                                    // style={{ width: 300 }}
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
                        </Grid>
                        <Grid item xs={12}>
                            <Typography variant="h4" component="h4" className={classes.title}>Join window</Typography>
                        </Grid>
                        <Grid item xs={6}>
                            <FormGroup className={classes.formGroup}>
                                <FormikField type="number" label={"Window size"} name={"joinWindowSize"} />
                            </FormGroup>
                        </Grid>
                        <Grid item xs={6}>
                            <FormGroup className={classes.formGroup}>
                                <FormikSelect name="joinWindowUnit" label="Select Window Size TimeUnit" items={timeunits} required/>

                            </FormGroup>
                        </Grid>
                        <Grid item xs={12}>
                            <Typography variant="h4" component="h4" className={classes.title}>Join rule</Typography>
                        </Grid>
                        <Grid item xs={6}>
                            <FormGroup className={classes.formGroup}>
                                <FormikField label={"Field from A"} name={"joinKeyFromA"} />
                            </FormGroup>
                        </Grid>
                        <Grid item xs={6}>
                            <FormGroup className={classes.formGroup}>
                                <FormikField label={"Field from B"} name={"joinKeyFromB"} />
                            </FormGroup>
                        </Grid>
                        <Grid item xs={12}>
                            <FormGroup className={classes.formGroup}>
                                <Typography variant="h4" component="h4" className={classes.title}>Join where condition</Typography>
                                <FormikField label={"Where condition"} subLabel={"(A condition such as myfield = 'Something')"} name={"joinWhere"} />
                            </FormGroup>
                        </Grid>
                    </Grid>
                </Box>
            )}
        </>
    )
}

export default MetricJoinType
