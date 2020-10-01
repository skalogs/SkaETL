import { Box, Button, makeStyles, Typography } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';
import { useFormikContext } from 'formik';
import React from 'react';
import { CreateReferentialProcessFields } from '../../../pages/referential/create';
import FormikField from '../../common/FormikField';
import StepHeading from '../../common/StepHeading';

const useStyles = makeStyles(theme => ({
    button: {
        backgroundColor: "#01B3FF",
        color: "#fff",
        borderRadius: 2,
        textTransform: "capitalize",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        width: 115,
        height: 40,
        fontSize: 14,
        lineHeight: "18px",
        fontWeight: 600,
        "&:hover": {
            backgroundColor: "#01B3FF",
            color: "#fff",
            boxShadow: "none",
        }
    },
    addValue: {
        display: "flex",
        margin: theme.spacing(2, 0),
    },
    valueCard: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        backgroundColor: "rgb(255, 152, 0)",
        borderRadius: 28,
        minWidth: 50,
        padding: theme.spacing(0.5, 1),
        marginRight: theme.spacing(1.5),
    },
    closeIcon: {
        width: 15,
    },
    closeButton: {
        color: "#fff",
        minWidth: 15,
        minHeight: 15,
        borderRadius: 0,
        marginLeft: theme.spacing(0.5),
        padding: theme.spacing(0.5),
    },
    addValueText: {
        color: "#fff",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 400,
        lineHeight: "14px",
    }
}))

const AddEntry = () => {
    const classes = useStyles()
    const { values, setFieldValue } = useFormikContext<CreateReferentialProcessFields>()

    const handleAddClick = () => {
        const updatedListAssociatedKeys = [...values.listAssociatedKeys, values.newEntry]
        setFieldValue("newEntry", "")
        setFieldValue("listAssociatedKeys", updatedListAssociatedKeys)
    }

    const handleRemoveAction = item => {
        const updatedListAssociatedKeys = values.listAssociatedKeys.filter(element => element !== item )
        setFieldValue("listAssociatedKeys", updatedListAssociatedKeys)
    }

    return (
        <Box component="div">
            <StepHeading name={"Select the entries"} />
            <FormikField label={"Name of your entry"} name={"newEntry"} />
            <Button disabled={!values.newEntry} onClick={handleAddClick} className={classes.button}>Add</Button>

            <Box className={classes.addValue}>
                {values.listAssociatedKeys.map(item => (<Box key={item} className={classes.valueCard}>
                    <Typography className={classes.addValueText}>{item}</Typography>
                    <Button onClick={() => handleRemoveAction(item)} className={classes.closeButton}><CloseIcon className={classes.closeIcon} /></Button>
                </Box>))}
            </Box>
        </Box>
    )
}

export default AddEntry
