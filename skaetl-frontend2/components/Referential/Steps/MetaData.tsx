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

const MetaData = () => {
    const classes = useStyles()
    const { values, setFieldValue } = useFormikContext<CreateReferentialProcessFields>()

    const handleAddClick = () => {
        const updatedListMetaData = [...values.listMetadata, values.newMetadata]
        setFieldValue("newMetadata", "")
        setFieldValue("listMetadata", updatedListMetaData)
    }

    const handleRemoveAction = item => {
        const updatedListMetaData = values.listMetadata.filter(element => element !== item )
        setFieldValue("listMetadata", updatedListMetaData)
    }
    return (
        <Box component="div">
            <StepHeading name={"Select the meta-data to extract"} />
            <FormikField label={"Name of your Metadata"} name={"newMetadata"} />
            <Button disabled={!values.newMetadata} onClick={handleAddClick} className={classes.button}>Add</Button>

            <Box className={classes.addValue}>
                {values.listMetadata.map(item => (<Box key={item} className={classes.valueCard}>
                    <Typography className={classes.addValueText}>{item}</Typography>
                    <Button onClick={() => handleRemoveAction(item)} className={classes.closeButton}><CloseIcon className={classes.closeIcon} /></Button>
                </Box>))}
            </Box>
        </Box>
    )
}

export default MetaData
