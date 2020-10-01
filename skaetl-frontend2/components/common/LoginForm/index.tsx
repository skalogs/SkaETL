import { Box, Button, makeStyles, Snackbar, TextField, Typography } from '@material-ui/core';
import { Form, Formik } from 'formik';
import Link from 'next/link';
import React, { useReducer } from 'react';
import * as Yup from "yup";
import Alert from '../Alert';
import { MyTextField } from '../Textfield';

const useStyles = makeStyles(theme => ({
    loginButton: {
        color: "#fff",
        backgroundColor: "#01B3FF",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        width: "100%",
        height: 45,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
        marginTop: theme.spacing(2),
        "&:hover": {
            color: "#fff",
            backgroundColor: "#01B3FF",
            boxShadow: "none",
        }
    },
    linkWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "flex-start",
        marginTop: theme.spacing(3),
    },
    issueText: {
        fontWeight: 400,
        fontSize: 14,
        lineHeight: "17px",
        fontFamily: "'Open Sans', sans-serif",
        color: "#657E88",
        letterSpacing: "0.28px",
    },
    link: {
        fontWeight: 400,
        fontSize: 14,
        lineHeight: "19px",
        fontFamily: "'Open Sans', sans-serif",
        color: "#01B3FF",
        letterSpacing: "0.28px",
        marginLeft: theme.spacing(1),
    }
}))

const loginSchema = Yup.object().shape({
    user: Yup.string()
        .required("Username/Email can not be empty."),
    password: Yup.string()
        .min(2, "Password too short.")
        .required("Password can not be empty."),
})

const initialState = {
    isLoading: false,
    isError: false,
    error: null,
    isPasswordVisible: false
}

const reducer = (state, action) => {
    console.log(`BEFORE UPDATE: action ---> ${JSON.stringify(action)} \n state ---> ${JSON.stringify(state)}` )
    switch (action.type) {
        case 'LOGIN_REQUEST':
            return { ...state, isLoading: true, error: null, isError: false }
        case 'LOGIN_SUCCESS':
            return { ...state, isLoading: false, error: null, isError: false }
        case 'LOGIN_FAILURE':
            return { ...state, isLoading: false, error: action.payload, isError: true }
        case 'TOGGLE_PASSWORD_VISIBILITY':
            return {...state, isPasswordVisible: !state.isPasswordVisible }
        case 'CLOSE_ALERT_SNACKBAR':
            return {...state, isError: false}
        case 'RESET_ERROR':
            return {...state, error: null}
        default:
            throw new Error(`Unknow action: ${action.type}`)
    }
}

const LoginForm = () => {
    const classes = useStyles()
    const [ loginState, loginDispatch ] = useReducer( reducer, initialState)
    console.log("LOGIN STATE", loginState);
    const onSubmit = async (values, actions) => {
        loginDispatch({ type: 'LOGIN_REQUEST'})
        console.log("VAlues", values);
        let result = null
        try {
            const { user, password } = values

            const requestOptions = {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json",
                },
                body: JSON.stringify({ login: user, password }),
            }

            const response = await fetch(`/auth/restore`, requestOptions);
            result = await response.json();
            loginDispatch({ type: 'LOGIN_SUCCESS'})

        } catch (err) {
            console.log(err);
            loginDispatch({ type: 'LOGIN_FAILURE', payload: err })
            result = null;
        }
    }

    const togglePasswordVisibility = () => {
        loginDispatch({ type: 'TOGGLE_PASSWORD_VISIBILITY' })
    }

    const closeAlertSnackbar = () => {
        loginDispatch({ type: 'CLOSE_ALERT_SNACKBAR'})
    }

    const onSnackbarExit = () => {
        loginDispatch({ type: 'RESET_ERROR'})
    }

    const { isPasswordVisible, isError, isLoading, error } = loginState
    return (
        <>
        <Snackbar
            anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
            open={isError}
            autoHideDuration={5000}
            onClose={closeAlertSnackbar}
            onExited={onSnackbarExit}
        >
            <Alert onClose={closeAlertSnackbar} severity="error">
            {JSON.stringify(error)}
            </Alert>
        </Snackbar>
        <Formik
            initialValues={{ user: '', password: '' }}
            onSubmit={onSubmit}
            validationSchema={loginSchema}
        >
            {() => (
                <Form>
                    <MyTextField labelName={"Username or Email"} type={"text"} name="user" as={TextField} />
                    <MyTextField labelName={"Password"} type={isPasswordVisible ? "text" : "password"} name="password" as={TextField} visibility={isPasswordVisible} onVisibility={togglePasswordVisibility} />
                    <Box component="div">
                        <Button type="submit" variant="contained" className={classes.loginButton}>{isLoading ? "Logging In..." : "Log In"} </Button>
                    </Box>
                    <Box component="div" className={classes.linkWrapper}>
                        <Typography variant="subtitle1" component="p" className={classes.issueText}>Got any issue?
                            <Typography variant="subtitle2" component="span">
                                <Link href="/">
                                    <a className={classes.link}>Contact Administrator</a>
                                </Link>
                            </Typography>
                        </Typography>
                    </Box>
                </Form>
            )}
        </Formik>
        </>
    )
}

export default LoginForm
