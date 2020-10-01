import { Box, Button, FormGroup, Grid, makeStyles, Typography } from '@material-ui/core'
import AddIcon from '@material-ui/icons/Add'
import CloseIcon from '@material-ui/icons/Close'
import { ErrorMessage, useFormikContext } from 'formik'
import React from 'react'
import { CreateConfigurationFields } from '../../../pages/logstash-configuration/create'
import { useLogstashConfiguration } from '../../../utils/logstashConfiguration'
import FormikField from '../../common/FormikField'
import FormikSelect from '../../common/FormikSelect'
import StepHeading from '../../common/StepHeading'

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
            width: "calc(100% - 100px)",
            minHeight: 0,
        },
        "& .MuiButton-root.Mui-disabled": {
            color: "rgba(255, 255, 255, 0.46)",
            backgroundColor: "rgba(138, 141, 142, 0.5)",
        }
    },
    button: {
        backgroundColor: "#01B3FF",
        color: "#fff",
        borderRadius: 2,
        textTransform: "capitalize",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        width: 84,
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
    buttonIcon: {
        fontSize: 19,
        marginLeft: theme.spacing(1),
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
    spacingLeft: {
        paddingLeft: "0 !important",
    },
    spacingRight: {
        paddingRight: "0 !important",
    },
    helperText: {
        color: "red",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "18px",
        fontWeight: 400,
        margin: theme.spacing(0, 0, 0, 0),
    },
    spacingTop: {
        marginTop: theme.spacing(2.5),
    },
    innerFormGroupWrapper: {
        minHeight: 95,
    }
}))

const Input = () => {
    const classes = useStyles()
    const logstashCtx = useLogstashConfiguration()
    const { typeDataIn } = logstashCtx.state.configuration
    const { values, setFieldValue } = useFormikContext<CreateConfigurationFields>()
    console.log("GAURAV VALUES", values)
    const generateApiKey = () => {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16)
        })
    }
    const isViewHost = () => {
        const value = values.typeInput
        if (value == "User Advanced" || value == "FILE") {
            return false
        } else if (value === "") {
            return false
        } else {
            return true
        }
    }

    const isViewType = () => {
        const value = values.typeInput
        if (value == "User Advanced" || value == "KAFKA") {
            return false
        } else if (value === "") {
            return false
        } else {
            return true
        }
    }

    const isViewTopic = () => {
        const value = values.typeInput
        if (value == "KAFKA") {
            return true
        } else if (value === "") {
            return false
        } else {
            return false
        }
    }

    const isViewPath = () => {
        const value = values.typeInput
        if (value == "FILE") {
            return true
        } else if (value === "") {
            return false
        } else {
            return false
        }
    }

    const handleAddClick = (event) => {
        event.stopPropagation()
        const { tag, host, port, topic, codec, typeForced, path, typeInput } = values
        if (tag && tag != '') {
            const anInput = {
                "idCli": generateApiKey(),
                "host": host,
                "port": port,
                "topic": topic,
                "codec": codec,
                "typeForced": typeForced,
                "path": path,
                "typeInput": typeInput,
                "tag": tag
            }
            setFieldValue("input", [...values.input, { ...anInput }])
        }
    }


    return (
        <Box component="div">
            <StepHeading name={"Select your inputs"} />
            <FormGroup className={classes.formGroup}>
                <FormikSelect name="typeInput" label="Input Type" items={typeDataIn} required />
            </FormGroup>
            <Box className={classes.innerFormGroupWrapper}>
                <Box className={classes.apiKeyWrapper}>
                    <FormikField name={"tag"} label={"Tag Label"} />
                    <Button disabled={!(values.tag && values.tag != '')} onClick={handleAddClick} className={`${classes.button} ${classes.spacingTop}`}>Add <AddIcon className={classes.buttonIcon} /></Button>
                </Box>
                <Box className={classes.helperText}><ErrorMessage name="input" /></Box>
            </Box>
            {isViewHost() &&
                <Box>
                    <Grid container>
                        <Grid item xs={6} className={classes.spacingLeft}>
                            <FormikField name={"host"} label={"Host Name"} />
                        </Grid>
                        <Grid item xs={6} className={classes.spacingRight}>
                            <FormikField name={"port"} label={"Host Port"} />
                        </Grid>
                    </Grid>
                </Box>
            }
            {isViewType() &&
                <Box>
                    <Grid container>
                        <Grid item xs={6} className={classes.spacingLeft}>
                            <FormikField name={"typeForced"} label={"Type"} />

                        </Grid>
                        <Grid item xs={6} className={classes.spacingRight}>
                            <FormikField name={"codec"} label={"Codec"} />
                        </Grid>
                    </Grid>
                </Box>
            }
            {isViewTopic() && <FormikField name={"topic"} label={"Topic name"} />}
            {isViewPath() && <FormikField name={"path"} label={"File Path"} />}



            <Box className={classes.addValue}>
                {values.input.map((item) =>
                    (
                        <Box key={item.tag} className={classes.valueCard}>
                            <Box className={classes.innerWrapper}>
                                <Typography className={classes.addValueText}>{item.typeInput}--{item.tag}</Typography>
                                <Button onClick={() => setFieldValue("input", values.input.filter(e => e !== item))} className={classes.closeButton}><CloseIcon className={classes.closeIcon} /></Button>
                            </Box>
                        </Box>
                    ))}
            </Box>
        </Box>
    )
}

export default Input
