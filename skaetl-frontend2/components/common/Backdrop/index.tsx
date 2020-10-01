import { Box, makeStyles } from '@material-ui/core';
import React from 'react';

const useStyles = makeStyles({
    backdropWrapper: {
        width: "100%",
        height: "100%",
        position: "fixed",
        zIndex: 999,
        left: 0,
        top: 0,
        backgroundColor: "rgba(0, 0, 0, 0.5)",
    }
})


const Backdrop = props => {
    const classes = useStyles()
    return (
        props.show ? <Box className={classes.backdropWrapper} onClick={props.clicked} style={{
            display: props.show ? 'block' : 'none'
        }} /> : null
    )
}
export default Backdrop;
