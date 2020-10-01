/* eslint-disable no-nested-ternary */
import { FormGroup, IconButton, InputAdornment, TextField, Typography } from "@material-ui/core"
import { makeStyles } from "@material-ui/core/styles"
import Visibility from "@material-ui/icons/Visibility"
import VisibilityOff from "@material-ui/icons/VisibilityOff"
import { FieldAttributes, useField } from "formik"

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
        }
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
    }
}))

interface MyTextFieldProps {
    labelName: string
    subLabel?: string
    visibility?: boolean
    onVisibility?: () => void
}

export const MyTextField: React.FC<FieldAttributes<MyTextFieldProps>> = ({
    placeholder,
    labelName,
    onVisibility,
    visibility,
    ...props
}) => {
    const classes = useStyles()
    const [field, meta] = useField(props)
    const errorText = meta.error && meta.touched ? meta.error : ""
    const { type, subLabel } = props
    return (
        <FormGroup className={classes.formGroup}>
            <Typography variant="subtitle1" component="p" className={classes.label}>{labelName}<Typography variant="subtitle2" component="span" className={classes.subLabel}>{subLabel}</Typography></Typography>
            <TextField
                variant="outlined"
                type={type}
                className={classes.formControl}
                placeholder={placeholder}
                {...field}
                helperText={errorText}
                error={!!errorText}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            {labelName === "Password" ? (
                                <IconButton className={classes.iconButton} aria-label="toggle password visibility" onClick={onVisibility} edge="end">
                                    {visibility ? (
                                        <Visibility className={classes.eye} />
                                    ) : (
                                            <VisibilityOff className={classes.eye} />
                                        )}
                                </IconButton>

                            ) : null}
                        </InputAdornment>
                    ),
                }}
            />
        </FormGroup>
    )
}
