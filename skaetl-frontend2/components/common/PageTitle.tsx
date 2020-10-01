import { Box, Button, makeStyles, Typography } from '@material-ui/core';
// import RefreshIcon from '@material-ui/icons/Refresh';
import React from 'react';

interface Props {
    title: string
}

const useStyles = makeStyles(theme => ({
    wrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "flex-start",
        marginBottom: theme.spacing(4),
        paddingBottom: theme.spacing(2),
        borderBottom: "1px solid #AABCC480",
    },
    title: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 22,
        fontWeight: 600,
        lineHeight: "30px",
        letterSpacing: "0.44px",
    },
    // icon: {
    //     width: 22,
    //     height: 22,
    //     color: "#01B3FF",
    // },
    // iconButton: {
    //     width: 22,
    //     minWidth: 22,
    //     padding: 0,
    //     borderRadius: 0,
    //     marginLeft: theme.spacing(4),
    // }
}))

const PageTitle: React.FC<Props> = props => {
    const classes = useStyles()
    const { title } = props
    return (
        <Box component="div" className={classes.wrapper}>
            <Typography variant="h1" component="h1" className={classes.title}>
                {title}
            </Typography>
            {/* <Button className={classes.iconButton}><RefreshIcon className={classes.icon} /></Button> */}
        </Box>
    )
}

export default PageTitle
