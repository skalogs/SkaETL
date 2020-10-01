import { FormGroup, makeStyles, Typography } from "@material-ui/core";
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import { ErrorMessage, Field, FieldInputProps } from "formik";
import React, { ReactNode } from "react";

export interface FormikSelectItem {
    label: string;
    value: string;
}
interface FormikSelectProps {
    name: string;
    label: string;
    items: any[];
    required?: boolean
    optionalLabel?: string
}

interface MaterialUISelectProps extends FieldInputProps<string> {
    label: string;
    children: ReactNode;
    errorString?: string;
    required: boolean;
    optionalLabel?: string
}

const useStyles = makeStyles(theme => ({
    label: {
        color: "#00274ADE !important",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "18px",
        fontWeight: 600,
        marginBottom: theme.spacing(1),
        transform: "none",
        position: "relative",
    },
    select: {
        marginTop: "0 !important",
        height: 45,
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 500,
        backgroundColor: "#fff",
        border: "1px solid #AABCC480",
        borderRadius: 4,
        "&:hover": {
            border: "1px solid #01B3FF",
            "&:before": {
                borderBottom: "none !important",
            },
            "&:after": {
                borderBottom: "none !important",
            }
        },
        "&:before": {
            borderBottom: "none",
        },
        "&:after": {
            borderBottom: "none",
        },
        "& .MuiSelect-selectMenu": {
            backgroundColor: "#fff",
        },
        "& .MuiSelect-select.MuiSelect-select": {
            paddingLeft: theme.spacing(2),
        },
        "& .MuiSelect-icon": {
            right: 12,
        }
    },
    menuItem: {
        padding: theme.spacing(1, 2),
        marginBottom: theme.spacing(0.5),
        cursor: "pointer",
        fontFamily: "'Open Sans', sans-serif",
        color: "#0D0D0D",
        fontSize: 14,
        lineHeight: "19px",
        fontWeight: 600,
        textTransform: "uppercase",
        userSelect: "none",
        display: "flex",
        alignItems: "flex-start",
        flexDirection: "column",
        "&:hover": {
            backgroundColor: "#F3F4F5",
        },
        "&:last-child": {
            marginBottom: 0,
        }
    },
    dropdownStyle: {
        border: "1px solid #AABCC480",
        backgroundColor: '#fff',
    },
    formGroup: {
        minHeight: 95,
    },
    optionalLabel: {
        color: "rgba(0, 0, 0, 0.54)",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 10,
        lineHeight: "14px",
        fontWeight: 400,
        textTransform: "capitalize",
        marginTop: theme.spacing(0.5),
    }
}))

const MaterialUISelect: React.FC<MaterialUISelectProps> = ({
    label,
    children,
    errorString,
    name,
    value,
    optionalLabel,
    onChange,
    onBlur,
    required
}) => {
    const classes = useStyles()
    return (
        <FormControl fullWidth>
            <FormGroup className={classes.formGroup}>
                <InputLabel className={classes.label} required={required}>{label}</InputLabel>
                <Select MenuProps={{ classes: { paper: classes.dropdownStyle } }} className={classes.select} name={name} onChange={onChange} onBlur={onBlur} value={value} required={false} >
                    {children}
                </Select>
                <FormHelperText>{errorString}</FormHelperText>
                {optionalLabel && <Typography variant="subtitle1" component="p" className={classes.optionalLabel}>{optionalLabel}</Typography>}
            </FormGroup>
        </FormControl>
    )
}
const FormikSelect: React.FC<FormikSelectProps> = ({ name, label, optionalLabel, items, required = false }) => {
    const classes = useStyles()
    return (
        <Field
            name={name}
            as={MaterialUISelect}
            label={label}
            optionalLabel={optionalLabel}
            errorString={<ErrorMessage name={name} />}
            required={required}
        >
            {/* <MenuItem value={""}></MenuItem> */}
            {items.map(item => {
                return (
                    <MenuItem disableRipple={true} className={classes.menuItem} key={item.value} value={item.value || item}>
                        {item.label || item} 
                        {/* {item.optionalLabel && <Typography variant="subtitle1" component="p" className={classes.optionalLabel}>{item.optionalLabel}</Typography>} */}
                    </MenuItem>
                )
            })}
        </Field>
    )
}

export default FormikSelect