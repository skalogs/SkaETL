import { makeStyles, Typography } from '@material-ui/core';
import React from 'react';

interface Props {
    name: string
    optional?: string
}

const useStyles = makeStyles(theme => ({
    stepHeading: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 18,
        fontWeight: 600,
        lineHeight: "22px",
        letterSpacing: 0,
        marginBottom: theme.spacing(4),
    },
    stepOptional: {
        color: "#6282A3",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 400,
        lineHeight: "14px",
        letterSpacing: 0,
        marginLeft: theme.spacing(1),
    }
}))

const StepHeading: React.FC<Props> = props => {
    const classes = useStyles()
    const { name, optional } = props
    return (
        <>
            <Typography variant="h2" component="h2" className={classes.stepHeading}>{name}
                <Typography variant="subtitle2" component="span" className={classes.stepOptional}>{optional}</Typography>
            </Typography>

        </>
    )
}

export default StepHeading
