import { Box, Grid, Typography } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import Skeleton from '@material-ui/lab/Skeleton';
import Link from "next/link";
import React from "react";

interface Props {
    title: string
    link?: string
    path?: string
    isLink: boolean
    isParagraph?: boolean
    loading?: boolean
}

const useStyles = makeStyles(theme => ({
    cardWrapper: {
        background: "#fff",
        position: "relative",
        // border: "1px solid #F2F4F7",
        borderRadius: 2,
        marginBottom: theme.spacing(4),
        boxShadow: "rgba(0, 0, 0, 0.12) 0px 0px 10px 0",
    },
    cardHeader: {
        height: 45,
        display: "flex",
        alignItems: "center",
        padding: theme.spacing(0, 2),
        justifyContent: "space-between",
        borderBottom: "1px solid #F2F4F7",
    },
    title: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 16,
        fontWeight: 600,
        lineHeight: "22px",
        letterSpacing: "0.32px",
        fontStyle: "normal",
    },
    seeMoreLink: {
        color: "#01B3FF",
        fontFamily: "'Montserrat', sans-serif",
        fontSize: 14,
        fontWeight: 400,
        lineHeight: "19px",
        letterSpacing: "0.32px",
        cursor: "pointer",
    },
    cardContentsWrapper: {
        padding: theme.spacing(2),
    },
}))

const Card: React.FC<Props> = props => {
    const classes = useStyles()
    const { title, link, children, path, isLink, isParagraph, loading = true } = props
    // const [loading, setLoading] = React.useState(loading)

    // React.useEffect(
    //     () => {
    //         let timer = setTimeout(() => setLoading(false), 2000)
    //         return () => {
    //             clearTimeout(timer)
    //         }
    //     }, [])

    return (
        <Box component="div" className={classes.cardWrapper}>
            <Box className={classes.cardHeader} component="div">
                <Typography className={classes.title} variant="subtitle1" component="h5">
                    {title}
                </Typography>
                <Box>
                    {isLink && (
                        <Link href={path}>
                            <Typography className={classes.seeMoreLink} variant="h5" component="h5">
                                <a>{link}</a>
                            </Typography>
                        </Link>
                    )}
                </Box>
            </Box>
            <Box component="div" className={classes.cardContentsWrapper}>
                {loading ? (
                    <>
                        {isParagraph ? (
                            <>
                                <Skeleton animation="wave" width={150} style={{ marginBottom: 8 }} />
                                <Skeleton animation="wave" width={200} style={{ marginBottom: 8 }} />
                                <Skeleton animation="wave" width={500} style={{ marginBottom: 8 }} />
                                <Skeleton animation="wave" width={500} style={{ marginBottom: 8 }} />
                                <Skeleton animation="wave" width={500} style={{ marginBottom: 8 }} />
                            </>
                        ) : (
                                <>
                                    <SkeletonGrid number={2} />
                                    <SkeletonGrid number={2} />
                                    <SkeletonGrid number={2} />
                                    <SkeletonGrid number={2} />
                                    <SkeletonGrid number={2} />
                                    <SkeletonGrid number={2} />
                                </>
                            )}
                    </>
                ) : children}
            </Box>
        </Box>
    )
}

export default Card

const SkeletonGrid = props => {
    return (
        <Grid container>
            <Grid item xs={props.number}>
                <Skeleton animation="wave" style={{ marginBottom: 8 }} />
            </Grid>
            <Grid item xs={props.number}>
                <Skeleton animation="wave" style={{ marginBottom: 8 }} />
            </Grid>
            <Grid item xs={props.number}>
                <Skeleton animation="wave" style={{ marginBottom: 8 }} />
            </Grid>
            <Grid item xs={props.number}>
                <Skeleton animation="wave" style={{ marginBottom: 8 }} />
            </Grid>
            <Grid item xs={props.number}>
                <Skeleton animation="wave" style={{ marginBottom: 8 }} />
            </Grid>
            <Grid item xs={props.number}>
                <Skeleton animation="wave" style={{ marginBottom: 8 }} />
            </Grid>
        </Grid>
    )
}