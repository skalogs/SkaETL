import { Box, Button, makeStyles, Typography } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';
import React from 'react';
import Backdrop from './Backdrop';

interface Props {
    title: string,
    show?: boolean
    children?: any
    modalClosed?: () => void
    isSidebarOpen: boolean
}

const useStyles = makeStyles(theme => ({
    modalWrapper: {
        position: "fixed",
        top: 0,
        left: 0,
        width: "100%",
        height: "100%",
        zIndex: 999,
    },
    modal: {
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-40%, -50%) !important",
        backgroundColor: "#fff",
        zIndex: 999,
        width: "calc(100% - 214px)",
        height: "100%",
        borderRadius: 0,
        boxShadow: "0px 20px 40px #A3B4B829",
    },
    modalFullWidth: {
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-48%, -50%) !important",
        backgroundColor: "#fff",
        zIndex: 999,
        width: "calc(100% - 50px)",
        height: "100%",
        borderRadius: 0,
        boxShadow: "0px 20px 40px #A3B4B829",
    },
    header: {
        minHeight: 25,
        padding: theme.spacing(1, 2),
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
    },
    title: {
        color: "#00274A",
        fontSize: 22,
        lineHeight: "30px",
        fontFamily: "'Open Sans', sans-serif",
        fontWeight: 700,
        letterSpacing: "0.44px",
    },
    modalBody: {
        padding: theme.spacing(1, 2),
    }
}))

const DetailModal: React.FC<Props> = props => {
    const classes = useStyles()
    const { title, children, show, modalClosed, isSidebarOpen } = props
    return (
        <Box component="div" className={classes.modalWrapper} style={{
            display: show ? 'block' : 'none'
        }}>
            <Backdrop show={show} clicked={modalClosed} />
            <Box component="div" className={isSidebarOpen ? classes.modalFullWidth : classes.modal} style={{
                transform: show ? 'translateY(0)' : 'translateY(-100vh)',
                display: show ? 'block' : 'none'
            }}>
                <Box component="div" className={classes.header}>
                    <Typography variant="h1" component="h1" className={classes.title}>{title}</Typography>
                    <Button onClick={modalClosed}><CloseIcon /></Button>
                </Box>
                <Box className={classes.modalBody}>{children}</Box>
            </Box>
        </Box>
    )
}

export default React.memo(
    DetailModal,
    (prevProps, nextProps) => nextProps.show === prevProps.show && nextProps.children === prevProps.children
);
