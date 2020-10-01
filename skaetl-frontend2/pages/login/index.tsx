import {
  Box,
  Button,
  Grid,
  makeStyles,
  Snackbar,
  TextField,
  Typography,
} from "@material-ui/core";
import { Form, Formik } from "formik";
import Link from "next/link";
import React, { useReducer } from "react";
import { useRouter } from 'next/router'
import * as Yup from "yup";
import Alert from "../../components/common/Alert";
import { MyTextField } from "../../components/common/Textfield";
import { url } from "../../config"

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    height: "calc(100vh - 16px)",
  },
  loginWrapper: {
    position: "relative",
    width: 365,
    maxWidth: "100%",
    padding: theme.spacing(6.25, 5),
    boxShadow: "0px 2px 8px #51637529",
    backgroundColor: "#fff",
    borderRadius: 8,
  },
  loginText: {
    fontWeight: 700,
    fontSize: 25,
    lineHeight: "28px",
    fontFamily: "'Open Sans', sans-serif",
    color: "#00274A",
    letterSpacing: "0px",
    marginTop: theme.spacing(2.75),
    textAlign: "center",
  },
  formWrapper: {
    marginTop: theme.spacing(6),
    "& .MuiInputAdornment-positionEnd": {
      marginLeft: theme.spacing(0),
    },
    "& .MuiOutlinedInput-adornedEnd": {
      paddingRight: theme.spacing(1.75),
    },
  },
  loginLeftSeperator: {
    background:
      "transparent linear-gradient(142deg, #00A2E7 0%, #006FA1 100%) 0% 0% no-repeat padding-box",
    width: "100%",
    height: "100vh",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    flexDirection: "column",
    position: "relative",
  },
  alignCenter: {
    textAlign: "center",
    width: 485,
    margin: theme.spacing(0, "auto"),
  },
  title: {
    color: "#fff",
    fontFamily: "'Open Sans', sans-serif",
    fontSize: 80,
    lineHeight: "86px",
    fontWeight: 600,
    letterSpacing: "1.6px",
    marginBottom: theme.spacing(4),
  },
  subtitle: {
    color: "#fff",
    fontFamily: "'Open Sans', sans-serif",
    fontSize: 20,
    lineHeight: "32px",
    fontWeight: 400,
    letterSpacing: "0.6px",
    opacity: 0.5,
  },
  seperator: {
    position: "absolute",
    top: "50%",
    left: "100%",
    transform: "translate(-50%)",
    backgroundColor: "#fff",
    borderRadius: "50%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    padding: theme.spacing(1.5),
  },
  logo: {
    width: 45,
  },
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
    },
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
  },
}));

const loginSchema = Yup.object().shape({
  user: Yup.string().required("Username/Email can not be empty."),
  password: Yup.string()
    .min(2, "Password too short.")
    .required("Password can not be empty."),
});

const initialState = {
  isLoading: false,
  isError: false,
  error: null,
  isPasswordVisible: false,
};

const reducer = (state, action) => {
  console.log(
    `BEFORE UPDATE: action ---> ${JSON.stringify(
      action
    )} \n state ---> ${JSON.stringify(state)}`
  );
  switch (action.type) {
    case "LOGIN_REQUEST":
      return { ...state, isLoading: true, error: null, isError: false };
    case "LOGIN_SUCCESS":
      return { ...state, isLoading: false, error: null, isError: false };
    case "LOGIN_FAILURE":
      return {
        ...state,
        isLoading: false,
        error: action.payload,
        isError: true,
      };
    case "TOGGLE_PASSWORD_VISIBILITY":
      return { ...state, isPasswordVisible: !state.isPasswordVisible };
    case "CLOSE_ALERT_SNACKBAR":
      return { ...state, isError: false };
    case "RESET_ERROR":
      return { ...state, error: null };
    default:
      throw new Error(`Unknow action: ${action.type}`);
  }
};

const Login = () => {
  const classes = useStyles()
  const router = useRouter()
  const [loginState, loginDispatch] = useReducer(reducer, initialState)

  const onSubmit = async (values, actions) => {
    loginDispatch({ type: "LOGIN_REQUEST" })
    try {
      const { user, password } = values;

      const response = await fetch(`${url.API_URL}/home/login`,{
        method: "POST",
        headers: { Accept: "application/json, text/plain, */*", "Content-Type": "application/json" },
        body: JSON.stringify({ login: user, password: password }),
      })
      const result = await response.text()
      console.log("LOGIN RESPONSE", result)
      if (result === "OK") {
        window.localStorage.setItem('userDefine', user);
        loginDispatch({ type: "LOGIN_SUCCESS" });
        router.push("/")
      } else {
        loginDispatch({ type: "LOGIN_FAILURE", payload: result });
      }
      
    } catch (err) {
      console.log(err);
      loginDispatch({ type: "LOGIN_FAILURE", payload: "Error during call service" });
    }
  };

  const togglePasswordVisibility = () => {
    loginDispatch({ type: "TOGGLE_PASSWORD_VISIBILITY" });
  };

  const closeAlertSnackbar = () => {
    loginDispatch({ type: "CLOSE_ALERT_SNACKBAR" });
  };

  const onSnackbarExit = () => {
    loginDispatch({ type: "RESET_ERROR" });
  };

  const { isPasswordVisible, isError, isLoading, error } = loginState;
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
          {error || JSON.stringify(error)}
        </Alert>
      </Snackbar>
      <Box component="section">
        <Grid container>
          <Grid item xs={6}>
            <Box component="div" className={classes.loginLeftSeperator}>
              <Box component="div" className={classes.alignCenter}>
                <Typography
                  variant="h1"
                  component="h1"
                  className={classes.title}
                >
                  SkaETL
                </Typography>
                <Typography
                  variant="subtitle1"
                  component="p"
                  className={classes.subtitle}
                >
                  SkaETL provides multiple Log processing features which
                  simplify the difficult work of log analytics
                </Typography>
              </Box>
              <Box className={classes.seperator}>
                <img
                  src="/static/images/logo.png"
                  alt="SkaETL"
                  className={classes.logo}
                />
              </Box>
            </Box>
          </Grid>
          <Grid item xs={6}>
            <Box component="section" className={classes.root}>
              <Box component="div" className={classes.loginWrapper}>
                <Box>
                  <Typography
                    variant="h2"
                    component="h2"
                    className={classes.loginText}
                  >
                    Login
                  </Typography>
                </Box>
                <Box component="div" className={classes.formWrapper}>
                  <Formik
                    initialValues={{ user: "", password: "" }}
                    onSubmit={onSubmit}
                    validationSchema={loginSchema}
                  >
                    {() => (
                      <Form>
                        <MyTextField
                          labelName={"Username or Email"}
                          type={"text"}
                          name="user"
                          as={TextField}
                        />
                        <MyTextField
                          labelName={"Password"}
                          type={isPasswordVisible ? "text" : "password"}
                          name="password"
                          as={TextField}
                          visibility={isPasswordVisible}
                          onVisibility={togglePasswordVisibility}
                        />
                        <Box component="div">
                          <Button
                            type="submit"
                            variant="contained"
                            className={classes.loginButton}
                          >
                            {isLoading ? "Logging In..." : "Log In"}{" "}
                          </Button>
                        </Box>
                        <Box component="div" className={classes.linkWrapper}>
                          <Typography
                            variant="subtitle1"
                            component="p"
                            className={classes.issueText}
                          >
                            Got any issue?
                            <Typography variant="subtitle2" component="span">
                              <Link href="/">
                                <a className={classes.link}>
                                  Contact Administrator
                                </a>
                              </Link>
                            </Typography>
                          </Typography>
                        </Box>
                      </Form>
                    )}
                  </Formik>
                </Box>
              </Box>
            </Box>
          </Grid>
        </Grid>
      </Box>
    </>
  );
};

export default Login;
