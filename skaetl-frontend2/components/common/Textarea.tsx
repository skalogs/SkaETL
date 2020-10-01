/* eslint-disable no-nested-ternary */
import { FormGroup, TextareaAutosize, Typography } from "@material-ui/core"
import { makeStyles } from "@material-ui/core/styles"
import { FieldAttributes, Field } from "formik"

const useStyles = makeStyles(theme => ({
    formControl: {
        color: "#00274A",
        borderRadius: 4,
        border: "1px solid #AABCC480",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        padding: theme.spacing(1, 2),
        backgroundColor: "#fff",
        fontWeight: 500,
        "&:focus": {
            outline: 0,
            border: "1px solid #01B3FF",
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
        marginBottom: theme.spacing(2),
    }
}))

interface MyTextFieldProps {
    name: string
    label: string
    type?: string
    placeholder?: string
    visibility?: boolean
}

export const MyTextArea: React.FC<MyTextFieldProps> = ({
    placeholder,
    name,
    type,
    label,
    ...props
}) => {
    const classes = useStyles()
    return (
        <FormGroup className={classes.formGroup}>
            <Typography variant="subtitle1" component="p" className={classes.label}>{label}</Typography>
            {/* <TextareaAutosize
                rowsMin={5}
                className={classes.formControl}
                placeholder={placeholder}
            /> */}
            <Field 
                name={name}
                type={type}
                className={classes.formControl}
                placeholder={placeholder}
                rowsMin={5}
                as={TextareaAutosize}
            />
        </FormGroup>
    )
}
