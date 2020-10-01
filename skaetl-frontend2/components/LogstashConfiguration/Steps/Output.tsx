import { Box, Button, Checkbox, FormGroup, Grid, makeStyles, TextField, Typography } from '@material-ui/core';
import CheckBoxIcon from '@material-ui/icons/CheckBox';
import CheckBoxOutlineBlankIcon from '@material-ui/icons/CheckBoxOutlineBlank';
import CloseIcon from '@material-ui/icons/Close';
import { Field, useFormikContext } from 'formik';
import { Autocomplete } from 'formik-material-ui-lab';
import React from 'react';
import { CreateConfigurationFields } from '../../../pages/logstash-configuration/create';
import { useLogstashConfiguration } from '../../../utils/logstashConfiguration';
import FormikField from '../../common/FormikField';
import StepHeading from '../../common/StepHeading';

const downImage = "/static/images/down.png";

const useStyles = makeStyles(theme => ({
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
    icon: {
        color: "#01B3FF"
    },
    formGroup: {
        marginBottom: theme.spacing(2),
    },
    spacingLeft: {
        paddingLeft: "0 !important",
    },
    spacingRight: {
        paddingRight: "0 !important",
    },
    addValue: {
        display: "inline-block",
        margin: theme.spacing(2, 0),
        width: "100%",
    },
    valueCard: {
        display: "inline-block",
        backgroundColor: "rgb(255, 152, 0)",
        borderRadius: 28,
        minWidth: 50,
        padding: theme.spacing(1, 1, 1, 2),
        margin: theme.spacing(0.75),
    },
    closeIcon: {
        fontSize: 14,
    },
    button: {
        backgroundColor: "#01B3FF",
        color: "#fff",
        borderRadius: 2,
        textTransform: "capitalize",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        width: 184,
        height: 40,
        fontSize: 14,
        lineHeight: "18px",
        fontWeight: 600,
        marginLeft: theme.spacing(2),
        "&:hover": {
            backgroundColor: "#fff",
            color: "#01B3FF",
            border: "1px solid #01B3FF",
            boxShadow: "none",
        },
    },
    closeButton: {
        color: "rgb(255, 152, 0)",
        minWidth: 15,
        width: 20,
        height: 20,
        borderRadius: 25,
        marginLeft: theme.spacing(0.5),
        padding: theme.spacing(0.5),
        backgroundColor: "rgba(255, 255, 255, 0.4)",
        "&:hover": {
            color: "#fff",
        }
    },
    addValueText: {
        color: "#fff",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 400,
        lineHeight: "14px",
    },
    innerWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
    },
    label: {
        color: "#00274ADE !important",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "18px",
        fontWeight: 600,
        marginBottom: theme.spacing(1),
    },
}))

const Output = () => {
    const classes = useStyles()
    const logstash = useLogstashConfiguration()
    const { dispatch } = logstash
    const { values, setFieldValue } = useFormikContext<CreateConfigurationFields>()

    console.log("ON OUTPUT STEP VALUEs", values)
    const handleCreateOutput = () => {
        let listTagResult = values.listTag.map(x => x.tag);
        const { hostOutput, portOutput, topicOutput, codecOutput } = values
        const anOutput = {
            "host": hostOutput,
            "port": portOutput,
            "topic": topicOutput,
            "codec": codecOutput,
            "listTag": listTagResult,
        }
        setFieldValue("output", [...values.output, { ...anOutput }])
        setFieldValue("listTag", [])
    }

    return (
        <Box component="div">
            <StepHeading name={"Select your outputs"} />
            <Box>
                <Grid container>
                    <Grid item xs={6} className={classes.spacingLeft}>
                        <FormikField name={"hostOutput"} label={"Host Name"} />
                    </Grid>
                    <Grid item xs={6} className={classes.spacingRight}>
                        <FormikField name={"hostOutput"} label={"Host Port"} />
                    </Grid>
                    <Grid item xs={6} className={classes.spacingLeft}>
                        <FormikField name={"topicOutput"} label={"Topic Name"} />
                    </Grid>
                    <Grid item xs={6} className={classes.spacingRight}>
                        <FormikField name={"codecOutput"} label={"Codec"} />
                    </Grid>
                </Grid>
            </Box>
            <FormGroup className={classes.formGroup}>
                <Typography variant="subtitle1" component="p" className={classes.label}>Select a tag to apply</Typography>
                <Field
                    name="listTag"
                    multiple
                    component={Autocomplete}
                    disableCloseOnSelect
                    options={values.input}
                    getOptionLabel={(option: any) => option.tag}
                    // defaultValue={process.selectedProcessB}
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
                                {option.tag}
                            </React.Fragment>
                        )
                    }}
                    renderInput={(params) => (
                        <TextField className={classes.formControl} {...params} variant="outlined" />
                    )}
                />
            </FormGroup>

            <Box className={classes.addValue}>
                {values.output.map((item, index) =>
                    (
                        <Box key={index} className={classes.valueCard}>
                            <Box className={classes.innerWrapper}>
                                <Typography className={classes.addValueText}>{item.topic}</Typography>
                                <Button onClick={() => {
                                    setFieldValue("output", values.output.filter(e => e !== item))
                                }} className={classes.closeButton}><CloseIcon className={classes.closeIcon} /></Button>
                            </Box>
                        </Box>
                    ))}
            </Box>
            <Box textAlign="right">
                <Button disabled={!values.topicOutput} onClick={handleCreateOutput} className={classes.button}>Create Output</Button>
            </Box>
        </Box>
    )
}

export default Output