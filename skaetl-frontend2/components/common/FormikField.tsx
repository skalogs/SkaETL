import { FormGroup, TextField, Typography } from "@material-ui/core"
import { makeStyles } from "@material-ui/core/styles"
import { ErrorMessage, Field } from "formik"
import React from "react"

const useStyles = makeStyles(theme => ({
    formControl: {
        width: "100%",
        "& .MuiOutlinedInput-root": {
            "& fieldset": {
                border: "1px solid #AABCC480",
            },
            "&:hover": {
                "& fieldset": {
                    border: "1px solid #01B3FF",
                },
            },
            "& input": {
                padding: theme.spacing(0, 2),
                height: 45,
                color: "#00274A",
                fontFamily: "'Open Sans', sans-serif",
                fontSize: 14,
                fontWeight: 500,
                backgroundColor: "#fff",
            }
        },
        "& .MuiOutlinedInput-root.Mui-focused": {
            "& fieldset": {
                border: "1px solid #01B3FF",
            },
        },
        "& .MuiInputAdornment-positionEnd": {
            marginLeft: 0,
        },
        "& .MuiOutlinedInput-adornedEnd": {
            paddingRight: 0,
        },
        "& .MuiFormHelperText-root": {
            color: "red",
            fontFamily: "'Open Sans', sans-serif",
            fontSize: 13,
            lineHeight: "18px",
            fontWeight: 400,
            margin: theme.spacing(0.5, 0, 0, 0),
        },
    },
    label: {
        color: "#00274ADE !important",
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
    subLabel: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "18px",
        fontWeight: 400,
        marginLeft: theme.spacing(1),
    },
    eye: {
        color: "#99AFC7CC",
    },
    iconButton: {
        padding: theme.spacing(0.75),
    },
}))

interface FormikFieldProps {
    name: string;
    label: string;
    subLabel?: string;
    type?: string;
    required?: boolean;
    placeholder?: string;
}
const FormikField: React.FC<FormikFieldProps> = ({ name, label, type = "text", required = false, subLabel, placeholder="" }) => {
    const classes = useStyles()

    return (
        <FormGroup className={classes.formGroup}>
            <Typography variant="subtitle1" component="p" className={classes.label}>{label}<Typography variant="subtitle2" component="span" className={classes.subLabel}>{subLabel}</Typography></Typography>
            <Field
                className={classes.formControl}
                name={name}
                type={type}
                placeholder={placeholder}
                fullWidth
                autoComplete="off"
                // label={label}
                variant="outlined"
                as={TextField}
                helperText={<ErrorMessage name={name} />}
                required={required}
            />
        </FormGroup>
    )
}

export default FormikField