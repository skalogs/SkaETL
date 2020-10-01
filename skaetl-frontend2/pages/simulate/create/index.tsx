import { Box, Button, Grid, makeStyles, TextField } from '@material-ui/core'
import { Form, Formik } from 'formik'
import React from 'react'
import Card from '../../../components/common/Card'
import Layout from '../../../components/common/Layout'
import { MyTextArea } from '../../../components/common/Textarea'
import FormikField from '../../../components/common/FormikField'

const useStyles = makeStyles(theme => ({
    buttonsWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        marginTop: theme.spacing(2),
    },
    button: {
        color: "#fff",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
    },
    createButton: {
        backgroundColor: "#01B3FF",
        width: 120,
        height: 36,
        marginRight: theme.spacing(2),
        "&:hover": {
            backgroundColor: "#01B3FF",
            boxShadow: "none",
        }
    },
}))

const CreateGrokPattern = () => {
    const classes = useStyles()

    const onSubmit = () => { };
    return (
        <Layout>
            <Grid container>
                <Grid item xs={12}>
                    <Card title={"Create your own grok pattern"} link={""} path={""} isLink={false}>
                        <Formik
                            initialValues={{ name: "", value: "" }}
                            onSubmit={onSubmit}
                        >
                            {() => (
                                <Form>
                                    <Box component="div">
                                        <Box component="div">
                                            <FormikField label={"Name"} name={"Name"} />
                                            <MyTextArea label={"Value"} name={"Value"} />
                                        </Box>
                                        <Box component="div" className={classes.buttonsWrapper}>
                                            <Box>
                                                <Button variant="contained" className={`${classes.button} ${classes.createButton}`}>Create</Button>
                                            </Box>
                                        </Box>
                                    </Box>
                                </Form>
                            )}
                        </Formik>
                    </Card>
                </Grid>
            </Grid>
        </Layout>
    )
}

export default CreateGrokPattern
