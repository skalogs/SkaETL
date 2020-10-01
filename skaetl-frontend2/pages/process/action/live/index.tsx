import { Box, Button, Grid, makeStyles, Typography } from '@material-ui/core'
import Link from 'next/link'
import React from 'react'
import Layout from '../../../../components/common/Layout'
import { useProcess } from '../../../../utils/process'
import { useRouter } from 'next/router'

const useStyles = makeStyles(theme => ({
    title: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 22,
        fontWeight: 700,
        lineHeight: "30px",
        letterSpacing: "0.44px",
        marginBottom: theme.spacing(4),
    },
    subtitle: {
        color: "rgba(0, 0, 0, 0.54)",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 400,
        lineHeight: "19px",
        letterSpacing: "0.44px",
    },
    buttonWrapper: {
        display: "flex",
        alignItems: "flex-start",
        flexDirection: "column",
        margin: theme.spacing(2, 0),
    },
    workerButton: {
        color: "#fff",
        backgroundColor: "rgb(0, 150, 136)",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        width: "auto",
        height: 40,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
        marginBottom: theme.spacing(2),
        "&:hover": {
            color: "rgb(0, 150, 136)",
            backgroundColor: "#fff",
            border: "1px solid rgb(0, 150, 136)",
            boxShadow: "none",
        },
    },
    button: {
        color: "#fff",
        backgroundColor: "#01B3FF",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        width: 120,
        height: 40,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 2,
        textTransform: "capitalize",
        margin: theme.spacing(0, 1, 4, 0),
        "&:hover": {
            color: "#01B3FF",
            backgroundColor: "#fff",
            border: "1px solid #01B3FF",
            boxShadow: "none",
        },
    },
    outlinedButton: {
        color: "#01B3FF",
        backgroundColor: "#fff",
        border: "1px solid #01B3FF",
        "&:hover": {
            color: "#fff",
            backgroundColor: "#01B3FF",
            boxShadow: "none",
        },
    },
    cardWrapper: {
        width: 256,
        maxWidth: "100%",
        padding: theme.spacing(2.5),
        border: "1px solid #F2F4F7",
        borderRadius: 4,
        margin: theme.spacing(8, "auto"),
    },
    cardImage: {
        width: "100%",
        height: 200,
        marginBottom: theme.spacing(2.5),
    },
    noSpacing: {
        padding: "0 !important",
    },
    cardButton: {
        color: "#01B3FF",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        borderRadius: 0,
        textTransform: "capitalize",
        padding: 0,
        minWidth: 0,
        marginTop: theme.spacing(1.5),
        "&:hover": {
            backgroundColor: "transparent",
        }
    },
    wrapper: {
        display: "flex",
    },
    twoBtnWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
    },
    link: {
        "&:hover": {
            textDecoration: "none",
        }
    },
}))

const ActionLive = () => {
    const router = useRouter()
    const processState = useProcess()
    const { consumerState } = processState.state
    const classes = useStyles()
    const { processId } = router.query


    React.useEffect(() => {
        if (processId) {
            processState.findConusmerState(processId)
        }
    }, [processId])

    const handleScaleUp = () => {
        processState.scaleUp(processId)
    }

    const handleScaleDown = () => {
        processState.scaleDown(processId)
    }


    return (
        <Layout>
            <Grid container>
                <Grid item xs={12}>
                    <Box component="div">
                        <Typography variant="h1" component="h1" className={classes.title}>Your Process is {consumerState.processDefinition.name} with status {consumerState.statusProcess}</Typography>
                    </Box>
                    <Box component="div" >
                        <Button onClick={handleScaleUp} className={classes.button}>Scale Up</Button>
                        <Button onClick={handleScaleDown} className={classes.button}>Scale Down</Button>
                        <Link href={`/simulate/view?processId=${processId}`}>
                            <Button className={`${classes.button} ${classes.outlinedButton}`}><a className={classes.link}>Simulation</a></Button>
                        </Link>
                    </Box>
                    <Box component="div">
                        <Typography variant="subtitle1" component="p" className={classes.subtitle}>{consumerState.registryWorkers.length ? "Worker Running" : "No Worker Running"}</Typography>
                        <Box component="div" className={classes.buttonWrapper}>
                            {consumerState.registryWorkers.map(item => {
                                return (
                                <Button className={classes.workerButton}>{item}</Button>
                                )
                            })}
                            {/* <Button className={classes.workerButton}>importer-process-importer-2-PROCESS_CONSUMER</Button>
                            <Button className={classes.workerButton}>importer-process-importer-1-PROCESS_CONSUMER</Button> */}
                        </Box>
                    </Box>
                    <Box component="div">
                        <Grid container>
                            <Grid item lg={4} md={4} sm={6} xs={12} className={classes.noSpacing}>
                                <Card src={"/static/images/kibana.png"} title={"Explore your data with Kibana"} />
                            </Grid>
                            <Grid item lg={4} md={4} sm={6} xs={12} className={classes.noSpacing}>
                                <Card src={"/static/images/grafana.png"} title={"Status Process with Grafana"} />
                            </Grid>
                            <Grid item lg={4} md={4} sm={6} xs={12} className={classes.noSpacing}>
                                <Card src={"/static/images/referential.png"} title={"Referential"} isManage={true} />
                            </Grid>
                        </Grid>
                    </Box>
                </Grid>
            </Grid>
        </Layout>
    )
}

const Card = ({ src, title, isManage = false }) => {
    const classes = useStyles()
    return (
        <Box component="div" className={classes.cardWrapper}>
            <img className={classes.cardImage} src={src} alt={title} title={title} />
            <Box>
                <Typography variant="subtitle1" component="p">{title}</Typography>
                <Box className={!isManage ? classes.wrapper : classes.twoBtnWrapper}>
                    {isManage && <Button className={classes.cardButton}>{"Manage"}</Button>}
                    <Button className={classes.cardButton}>{"Explore"}</Button>
                </Box>
            </Box>
        </Box>
    )
}

export default ActionLive
