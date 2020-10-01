import { Box, Button, FormControlLabel, FormGroup, makeStyles, TextField, Typography } from '@material-ui/core'
import { Field, useFormikContext } from "formik"
import { Checkbox } from "formik-material-ui"
import { useRouter } from 'next/router'
import React from 'react'
import { CreateConfigurationFields } from '../../../pages/logstash-configuration/create'
import { useLogstashConfiguration } from '../../../utils/logstashConfiguration'
import FormikField from '../../common/FormikField'
import FormikSelect from '../../common/FormikSelect'
import StepHeading from '../../common/StepHeading'
import { MyTextArea } from '../../common/Textarea'
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
    formGroup: {
        marginBottom: theme.spacing(2),
    },
    apiKeyWrapper: {
        display: "flex",
        alignItems: "center",
        "& > div": {
            width: "calc(100% - 200px)",
        }
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
    saveButton: {
        width: 115,
        marginLeft: 0,
    },
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
    checkboxWrapper: {
        "& .MuiCheckbox-colorSecondary.Mui-checked": {
            color: "#09b2fb",
        }
    }
}))

const Attribute = () => {
    const classes = useStyles()
    const router = useRouter()
    const { idConfiguration } = router.query
    const logstashCtx = useLogstashConfiguration()
    const { typeEnv, configurationLogstash } = logstashCtx.state.configuration
    const { values, setFieldValue } = useFormikContext<CreateConfigurationFields>()
    const { dispatch, generateApiKey, editConfig, createConfig } = logstashCtx
    console.log("Attributes GAURAV VALUES", values)

    React.useEffect(() => {
        handleGenerateApiKey()
    }, [])


    const handleGenerateApiKey = () => {
        const apiKey = generateApiKey()
        setFieldValue("confData.apiKey", apiKey)
    }

    const handleSave = () => {
        if (idConfiguration) {
            editConfig(values)
        } else {
            createConfig(values)
        }
    }

    return (
        <Box component="div">
            <StepHeading name={"Describe your attributes"} />
            <FormGroup className={classes.formGroup}>
                <FormikSelect name="confData.env" label="Name of your environment" items={typeEnv} required />
            </FormGroup>
            <FormikField name={"confData.category"} label={"Category of your logs"} />
            <Box className={classes.apiKeyWrapper}>
                <FormikField name={"confData.apiKey"} label={"The unique API Key"} />
                <Button onClick={handleGenerateApiKey} className={classes.button}>Generate an APIKey</Button>
            </Box>
            <Box component="div" className={classes.checkboxWrapper}>
                <FormControlLabel
                    control={<Field name="statusCustomConfiguration" type="checkbox" component={Checkbox} />}
                    label={<Typography className={classes.checkboxText}>Custom Configuration</Typography>}
                />
                {values.statusCustomConfiguration && <MyTextArea label={"Configuration"} placeholder={""} name={"customConfiguration"} />}
            </Box>
            {values.statusCustomConfiguration && <Box component="div" textAlign="right">
                <Button onClick={handleSave} className={`${classes.button} ${classes.saveButton}`}>Save</Button>
            </Box>}
        </Box>
    )
}

export default Attribute
