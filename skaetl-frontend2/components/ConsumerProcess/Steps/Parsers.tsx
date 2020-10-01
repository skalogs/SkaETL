import { Box, Button, FormControlLabel, makeStyles, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import { Field, Form, Formik, useFormikContext } from 'formik';
import { Checkbox } from 'formik-material-ui';
import React from 'react';
import FormikField from '../../common/FormikField';
import FormikSelect from '../../common/FormikSelect';
import Modal from '../../common/Modal';
import StepHeading from '../../common/StepHeading';


const useStyles = makeStyles(theme => ({
    table: {
        minWidth: 400,
        border: "1px solid #99AFC73D",
    },
    tableHeadCell: {
        color: "#6282A3",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 400,
        lineHeight: "18px",
        letterSpacing: "0.33px",
        fontStyle: "normal",
        padding: theme.spacing(1, 1.5),
    },
    tableBodyCell: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        fontStyle: "normal",
        backgroundColor: "#fff",
        padding: theme.spacing(1.25, 1),
    },
    actionButton: {
        padding: 0,
        minWidth: 25,
        borderRadius: 2,
        marginRight: theme.spacing(1.5),
        "&:last-child": {
            marginRight: 0,
        }
    },
    tableHead: {
        backgroundColor: "#F5F7F8"
    },
    wrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
    },
    btn: {
        backgroundColor: "#18C151",
        color: "#fff",
        borderRadius: 2,
        textTransform: "capitalize",
        boxShadow: "none",
        fontFamily: "'Open Sans', sans-serif",
        width: 145,
        height: 40,
        fontSize: 14,
        lineHeight: "18px",
        fontWeight: 600,
        marginBottom: theme.spacing(4),
        "&:hover": {
            backgroundColor: "#18C151",
            color: "#fff",
            boxShadow: "none",
        }
    },
    buttonIcon: {
        fontSize: 19,
        marginRight: theme.spacing(1),
    },
    modalButton: {
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
    outlineButton: {
        backgroundColor: "transparent",
        color: "#00274A",
        "&:hover": {
            backgroundColor: "transparent",
            color: "#00274A",
        }
    },
    checkboxText: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        fontWeight: 500,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        fontStyle: "normal",
    },
    root: {
        "&$checked": {
            color: "#01B3FF"
        }
    },
    checked: {},
    checkboxWrapper: {
        "& .MuiCheckbox-colorSecondary.Mui-checked": {
            color: "#09b2fb",
        }
    }
}))

const parserList = [
    { label: "GROK", value: "GROK" },
    { label: "NITRO", value: "NITRO" },
    { label: "JSON_AS_STRING", value: "JSON_AS_STRING" },
    { label: "CEF", value: "CEF" },
    { label: "CSV", value: "CSV" },

]

const INITIAL_STATE = {
    typeParser: "",
    grokPattern: "",
    schemaCSV: "",
    activeFailForward: false,
    failForwardTopic: ""
}

const Parsers = () => {
    const classes = useStyles()
    const [isEditing, setIsEditing] = React.useState(false)
    const [selectedParser, setSelectedParser] = React.useState(INITIAL_STATE)
    const [isShow, setIsShow] = React.useState(false)
    const { values, setFieldValue } = useFormikContext<any>()
     
    console.log("CONSUMER PARSER VALUES", values)
    const handleAddParser = e => {
        e.preventDefault()
        setIsEditing(false)
        setIsShow(true)
    }

    const handleSave = (processParserObjValues, actions) => {
        let updatedProcessParsers = []
        if (isEditing) {
            updatedProcessParsers = values.processParser.map(p => {
                if (p === selectedParser) {
                    return processParserObjValues
                }
                return p
            })
        } else {
            updatedProcessParsers = [...values.processParser, processParserObjValues]
        }
        setFieldValue("processParser", updatedProcessParsers)
        setIsEditing(false)
        setIsShow(false)

    }

    const handleDeleteAction = parser => {
        const updatedProcessParsers = values.processParser.filter(element => element !== parser)
        setFieldValue("processParser", updatedProcessParsers)
        
    }

    const handleEditClick = parser => {
        setIsShow(true)
        setIsEditing(true)
        setSelectedParser(parser)
    }

    const handleModalClose = () => {
        setIsShow(false)
        setSelectedParser(INITIAL_STATE)
    }

    // React.useEffect(() => {
    //     consumerProcess.fetchParsers()
    // }, [])

    return (
        <>
            <Modal title={isEditing ? "Edit Parser" : "Add New Parser"} show={isShow} modalClosed={handleModalClose}>
                <Formik
                    enableReinitialize
                    initialValues={selectedParser}
                    onSubmit={handleSave}
                >
                    {({ values, handleSubmit }) => (
                        <Form>
                            <Box>
                                <FormikSelect name="typeParser" label="Choose parser" items={parserList} />
                                {values.typeParser === "GROK" && <FormikField label={"Grok Pattern"} name={"grokPattern"} />}
                                {values.typeParser === "CSV" && <FormikField label={"CSV (separated by ;)"} name={"schemaCSV"} />}
                                {values.activeFailForward && <FormikField label={"Topic Fail Parser"} name={"failForwardTopic"} />}
                                <Box className={classes.wrapper}>
                                    <Box className={classes.checkboxWrapper}>
                                        <FormControlLabel
                                            control={<Field name="activeFailForward" type="checkbox" component={Checkbox} />}
                                            label={<Typography className={classes.checkboxText}>Active fail parser forward</Typography>}
                                        />
                                    </Box>
                                    <Box>
                                        <Button className={`${classes.modalButton} ${classes.outlineButton}`} onClick={handleModalClose}>cancel</Button>
                                        <Button onClick={(e) => {
                                            e.stopPropagation()
                                            handleSubmit()
                                        }} className={classes.modalButton}>save</Button>
                                    </Box>
                                </Box>

                            </Box>
                        </Form>
                    )}
                </Formik>
            </Modal>

            <Box component="div">
                <Box component="div" className={classes.wrapper}>
                    <StepHeading name={"Select Parser"} optional={"(Optional)"} />
                    <Button variant="contained" className={classes.btn} onClick={handleAddParser}><AddIcon className={classes.buttonIcon} /> Add Parser</Button>
                </Box>
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Type</TableCell>
                                <TableCell align="right" className={classes.tableHeadCell}>Action</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {values.processParser.map((row, i) => (
                                <TableRow key={i}>
                                    <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                        {row.typeParser}
                                    </TableCell>
                                    <TableCell align="right" className={classes.tableBodyCell}>
                                        <Button onClick={() => handleEditClick(row)} className={classes.actionButton}>
                                            <EditIcon />
                                        </Button>
                                        <Button onClick={() => handleDeleteAction(row)} className={classes.actionButton}>
                                            <DeleteIcon />
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Box>

        </>
    )
}

export default Parsers
