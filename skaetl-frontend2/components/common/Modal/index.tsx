import { Box, Button, makeStyles, Typography } from '@material-ui/core'
import CloseIcon from '@material-ui/icons/Close'
import React from 'react'
import Backdrop from '../Backdrop'

interface Props {
    title: string,
    show?: boolean
    children?: any
    modalClosed?: () => void
    isClose?: boolean
}

const useStyles = makeStyles(theme => ({
    modalWrapper: {
        position: "fixed",
        top: 0,
        left: 0,
        width: "100%",
        height: "100%",
        zIndex: 1000,
    },
    modal: {
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%) !important",
        backgroundColor: "#fff",
        zIndex: 999,
        width: 550,
        minHeight: 100,
        borderRadius: 4,
    },
    header: {
        minHeight: 25,
        padding: theme.spacing(1, 2),
        borderBottom: "1px solid #99AFC73D",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
    },
    title: {
        color: "#00274A",
        fontSize: 16,
        lineHeight: "22px",
        fontFamily: "'Open Sans', sans-serif",
        fontWeight: 600,
        letterSpacing: "0.26px",
    },
    modalBody: {
        padding: theme.spacing(2),
        maxHeight: 550,
        overflowY: "auto",
    },
    closeButton: {
        minWidth: 25,
        borderRadius: 0,
    },
    closeIcon: {
        fontSize: 16,
    },
}))

const Modal: React.FC<Props> = props => {
    const classes = useStyles()
    const { title, children, show, modalClosed, isClose = false } = props
    return (
        <Box component="div" className={classes.modalWrapper} style={{
            display: show ? 'block' : 'none'
        }}>
            <Backdrop show={show} clicked={modalClosed} />
            <Box component="div" className={classes.modal} style={{
                transform: show ? 'translateY(0)' : 'translateY(-100vh)',
                display: show ? 'block' : 'none'
            }}>
                <Box component="div" className={classes.header}>
                    <Typography variant="h1" component="h1" className={classes.title}>{title}</Typography>
                    {isClose && <Button className={classes.closeButton} onClick={modalClosed}><CloseIcon className={classes.closeIcon} /></Button>}
                </Box>
                <Box className={classes.modalBody}>{children}</Box>
            </Box>
        </Box>
    )
}

export default React.memo(
    Modal,
    (prevProps, nextProps) => nextProps.show === prevProps.show && nextProps.children === prevProps.children
);
