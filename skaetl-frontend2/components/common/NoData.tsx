import { Box, makeStyles, TableBody, TableRow, Typography } from '@material-ui/core'
import React from 'react'

interface Props {
    cols?: number
    text?: string
}

const useStyles = makeStyles({
    noDataWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        height: 185,
    },
    noDataText: {
        fontSize: 14,
        lineHeight: "19px",
        fontWeight: 600,
        letterSpacing: "0.42px",
        color: "#636568",
        fontFamily: "'Open Sans', sans-serif",
    },
})

const NoData: React.FC<Props> = props => {
    const classes = useStyles()
    const { text, cols } = props
    return (
        <TableBody>
            <TableRow>
                <td colSpan={cols}>
                    <Box className={classes.noDataWrapper}>
                        <Typography className={classes.noDataText}>{text}</Typography>
                    </Box>
                </td>
            </TableRow>
        </TableBody>
    )
}

export default NoData
