import { Box, Button, FormGroup, Grid, makeStyles, TextField, Typography } from '@material-ui/core'
import FileCopyIcon from '@material-ui/icons/FileCopy'
import { Field, Form, Formik } from 'formik'
import React from 'react'
import Layout from '../../../components/common/Layout'
import FormikField from '../../../components/common/FormikField'

const useStyles = makeStyles(theme => ({
    buttonsWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginTop: theme.spacing(4),
    },
    button: {
        color: "#fff",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        height: 40,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
        backgroundColor: "#01B3FF",
        width: 250,
        "&:hover": {
            backgroundColor: "transparent",
            border: "1px solid #01B3FF",
            color: "#01B3FF",
            boxShadow: "none",
        },
    },
    selectTextarea: {
        minHeight: 45,
        padding: theme.spacing(2),
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 500,
        backgroundColor: "#fff",
        border: "1px solid #AABCC480",
        borderRadius: 4,
        "&:hover": {
            border: "1px solid #01B3FF",
        },
        "&:focus": {
            border: "1px solid #01B3FF",
            outline: "none",
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
    wrapper: {
        position: "relative",
    },
    copyButton: {
        position: "absolute",
        right: 15,
        top: 35,
        minWidth: 25,
        padding: 0,
        borderRadius: 0,
        "&:hover": {
            backgroundColor: "transparent",
        }
    }
}))

const GenerateLogStashProcess = () => {
    const classes = useStyles()
    return (
        <Layout>
            <Grid container>
                <Grid item xs={12}>
                    <Formik
                        initialValues={{}}
                        onSubmit={() => { }}
                    >
                        {({ }) => (
                            <Form>
                                <Box component="div">
                                    <Box component="div">
                                        <Box className={classes.wrapper}>
                                            <FormikField label={"Command"} name={"command"} />
                                            <Button className={classes.copyButton}><FileCopyIcon /></Button>
                                        </Box>
                                        <FormGroup className={classes.wrapper}>
                                            <Typography variant="subtitle1" component="p" className={classes.label}>{"Configuration"}</Typography>
                                            <Field as="textarea" className={classes.selectTextarea} placeholder={""} name={"configuration"} />
                                            <Button className={classes.copyButton}><FileCopyIcon /></Button>
                                        </FormGroup>
                                    </Box>
                                </Box>
                                <Box component="div" className={classes.buttonsWrapper}>
                                    <Box>
                                        <Button type="submit" variant="contained" className={classes.button}>Return to Configuration List</Button>
                                    </Box>
                                </Box>
                            </Form>
                        )}
                    </Formik>
                </Grid>
            </Grid>
        </Layout>
    )
}

export default GenerateLogStashProcess
