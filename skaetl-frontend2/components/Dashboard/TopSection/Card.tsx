import { Box, Grid, List, ListItem, Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core"
import { makeStyles } from "@material-ui/core/styles"
import Skeleton from '@material-ui/lab/Skeleton'
import React, { useState, useEffect } from "react"
import { useApplicationState } from "../../../utils/application"
import DetailModal from '../../common/DetailModal';

interface Props {
    title: string
    value: number
    isHoverData?: boolean
}

const useStyles = makeStyles(theme => ({
    boxStyle: {
        background: "#fff",
        height: 120,
        textAlign: "center",
        padding: theme.spacing(2),
        position: "relative",
        borderRadius: 2,
        border: "1px solid transparent",
        display: "flex",
        alignItems: "center",
        flexDirection: "column",
        marginBottom: theme.spacing(4),
        justifyContent: "center",
        boxShadow: "rgba(0, 0, 0, 0.12) 0px 0px 10px 0",
        cursor: "pointer",
        transition: "all 0.3s ease-in-out",
        "&:hover": {
            boxShadow: "none",
            border: "1px solid #01B3FF",
        },
        "& .hoverData": {
            display: "none",
            padding: theme.spacing(0, 1),
            transition: "all 0.3s ease-in-out",
        },
        "&:hover > .hoverData": {
            position: "absolute",
            top: 0,
            right: 0,
            left: "110%",
            width: 200,
            height: "100%",
            zIndex: 9,
            display: "block",
            backgroundColor: "#00274A",
            borderRadius: 4,
            transition: "all 0.3s ease-in-out",
            "&:before": {
                content: "''",
                position: "absolute",
                top: "50%",
                left: -5,
                transform: "translate(-50%, -50%)",
                borderStyle: "solid",
                borderWidth: "10px 10px 10px 0",
                borderColor: "transparent #002749 transparent transparent",
            },
        }
    },
    title: {
        color: "#6282A3",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 600,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        fontStyle: "normal",
    },
    value: {
        color: "#00274A",
        fontFamily: "'Montserrat', sans-serif",
        fontSize: 25,
        fontWeight: 600,
        lineHeight: "26px",
        fontStyle: "normal",
        marginBottom: theme.spacing(2),
    },
    listItem: {
        padding: 0,
        marginBottom: theme.spacing(3),
        alignItems: "center",
        justifyContent: "flex-start",
        "&:first-child": {
            marginTop: theme.spacing(2),
        },
        "&:last-child": {
            marginBottom: theme.spacing(0),
        }
    },
    number: {
        color: "#fff",
        fontFamily: "'Montserrat', sans-serif",
        fontSize: 12,
        fontWeight: 600,
        lineHeight: "16px",
        fontStyle: "normal",
        marginRight: theme.spacing(1),
    },
    text: {
        color: "#6282A3",
        fontFamily: "'Montserrat', sans-serif",
        fontSize: 12,
        fontWeight: 400,
        lineHeight: "16px",
        fontStyle: "normal",
    },
}))

const Card = props => {
    const classes = useStyles()
    const [loading, setLoading] = useState(true)
    const [isShow, setIsShow] = useState(false)
    const { isSidebarOpen } = useApplicationState()
    const { title, total, active, inActive, error, degraded, init, creation, process, metric, referential, all, production } = props

    useEffect(
        () => {
            let timer = setTimeout(() => setLoading(false), 2000)
            return () => {
                clearTimeout(timer)
            }
        }, [])

    return (
        <Box className={`${classes.boxStyle} box`} component="div">
            <Typography className={classes.value} variant="h5" component="h5">
                {total}
            </Typography>
            <Typography className={classes.title} variant="subtitle1" component="h5">
                {title}
            </Typography>
            <Box className="hoverData">
                {(title == "Consumer" || title === "Metric" || title === "Referential") &&
                    <Grid container>
                        <Grid item xs={6}>
                            <List>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{active}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Active</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{error}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Error</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{creation}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Creation</Typography>
                                </ListItem>
                            </List>
                        </Grid>
                        <Grid item xs={6}>
                            <List>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{inActive}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Inactive</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{degraded}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Degrated</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{init}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Init</Typography>
                                </ListItem>
                            </List>
                        </Grid>
                    </Grid>
                }
                {title === "Configuration" &&
                    <Grid container>
                        <Grid item xs={6}>
                            <List>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{active}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Active</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{inActive}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">InActive</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{error}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Error</Typography>
                                </ListItem>
                            </List>
                        </Grid>
                        <Grid item xs={6}>
                            <List>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{init}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Init</Typography>
                                </ListItem>
                            </List>
                        </Grid>
                    </Grid>
                }
                {title === "Worker" &&
                    <Grid container>
                        <Grid item xs={6}>
                            <List>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{process}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Process</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{metric}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Metric</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{referential}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Referential</Typography>
                                </ListItem>
                            </List>
                        </Grid>
                    </Grid>
                }
                {title === "Client Logstash" &&
                    <Grid container>
                        <Grid item xs={6}>
                            <List>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{all}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">All</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{production}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Production</Typography>
                                </ListItem>
                                <ListItem className={classes.listItem}>
                                    <Typography className={classes.number} variant="h6" component="h6">{error}</Typography>
                                    <Typography className={classes.text} variant="subtitle1" component="p">Error</Typography>
                                </ListItem>
                            </List>
                        </Grid>
                    </Grid>
                }
            </Box>
        </Box>
    )
}

export default Card
