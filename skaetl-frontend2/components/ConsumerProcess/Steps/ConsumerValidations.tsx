// import DateFnsUtils from '@date-io/date-fns';
import DateFnsUtils from "@date-io/date-fns";
import { Box, Button, FormControlLabel, Grid, makeStyles, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import CloseIcon from '@material-ui/icons/Close';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import { KeyboardDatePicker, MuiPickersUtilsProvider } from '@material-ui/pickers';
import { Field, Form, Formik, useFormikContext } from 'formik';
import { Checkbox } from 'formik-material-ui';
import React from 'react';
import { useConsumerProcess } from '../../../utils/consumerProcess';
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
        width: 185,
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
        width: 215,
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
        width: 115,
        backgroundColor: "transparent",
        color: "#00274A",
        "&:hover": {
            backgroundColor: "transparent",
            color: "#00274A",
        }
    },
    saveButton: {
        width: 115,
    },
    footerWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "flex-end",
    },
    spacingLeft: {
        paddingLeft: "0 !important",
    },
    spacingRight: {
        paddingRight: "0 !important",
    },
    addValue: {
        display: "flex",
        margin: theme.spacing(2, 0),
    },
    valueCard: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        backgroundColor: "rgb(186, 104, 200)",
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
    },
    dateWrapper: {
        margin: theme.spacing(1, 0),
        "& .MuiFormControl-root": {
            width: "100%",
            margin: 0,
            "& button": {
                padding: 0,
            },
            "& .MuiInputBase-root": {
                padding: theme.spacing(0, 2),
                border: "1px solid #AABCC480",
                borderRadius: 4,
                "& input": {
                    height: 45,
                    padding: 0,
                },
                "&:hover": {
                    border: "1px solid #01B3FF",
                },
                "&:before": {
                    borderBottom: 0,
                },
                "&:after": {
                    borderBottom: 0,
                }
            }
        }
    },
    label: {
        color: "#00274ADE !important",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "18px",
        fontWeight: 600,
        marginBottom: theme.spacing(1),
    }
}))

const unitList = [
    { label: "MILLIS", value: "MILLIS" },
    { label: "SECONDS", value: "SECONDS" },
    { label: "MINUTES", value: "MINUTES" },
    { label: "HOURS", value: "HOURS" },
    { label: "DAYS", value: "DAYS" },
]

const validators = [
    { label: "MANDATORY_FIELD", value: "MANDATORY_FIELD" },
    { label: "BLACK_LIST_FIELD", value: "BLACK_LIST_FIELD" },
    { label: "MAX_FIELD", value: "MAX_FIELD" },
    { label: "MAX_MESSAGE_SIZE", value: "MAX_MESSAGE_SIZE" },
    { label: "FIELD_EXIST", value: "FIELD_EXIST" },
    { label: "TIMESTAMP_VALIDATION", value: "TIMESTAMP_VALIDATION" }
]

const ConsumerValidations = () => {
    const classes = useStyles()
    const [isShow, setIsShow] = React.useState(false)
    const consumerProcess = useConsumerProcess()
    const { values, setFieldValue } = useFormikContext<any>()
    const [isEditing, setIsEditing] = React.useState(false)
    const [selectedValidator, setSelectedValidator] = React.useState({
        typeValidation: "",
        parameterValidation: {
            "mandatory": '',
            "blackList": [],
            "maxFields": "",
            "maxMessageSize": "",
            "fieldExist": "",
            "validateInThePast": false,
            "unitInThePast": 1,
            "chronoUnitInThePast": "DAYS",
            "validateInFuture": false,
            "unitInFuture": 1,
            "chronoUnitInFuture": "DAYS",
            "validateAfterFixedDate": false
        },
        keyBlackList: '',
        valueBlackList: '',
    })
    const { fetchValidators } = consumerProcess


    // React.useEffect(() => {
    //     fetchValidators()
    // }, [])

    const handleModal = e => {
        e.preventDefault()
        setIsShow(!isShow)
    }

    const handleEditClick = validator => {
        setIsShow(true)
        setIsEditing(true)
        setSelectedValidator(validator)
    }

    const handleDeleteAction = transformation => {
        const updatedProcessValidations = values.processValidation.filter(t => t !== transformation)
        setFieldValue("processValidation", updatedProcessValidations)
    }

    const handleSave = (subFormikValues, actions) => {
        setIsShow(false)
    
        let updatedProcessValidations = []
        if (isEditing) {
            updatedProcessValidations = values.processValidation.map(p => {
                if (p === selectedValidator) {
                    return subFormikValues
                }
                return p
            })
        } else {
            updatedProcessValidations = [...values.processValidation, subFormikValues]
        }
        setFieldValue("processValidation", updatedProcessValidations)
        setIsEditing(false)
        setIsShow(false)
    }

    const isMandatory = subFormikValues => {
        return subFormikValues.typeValidation == "MANDATORY_FIELD";
    }

    const isBlackList = subFormikValues => {
        return subFormikValues.typeValidation == "BLACK_LIST_FIELD";
    }

    const isMaxField = subFormikValues => {
        return subFormikValues.typeValidation == "MAX_FIELD";
    }

    const isMaxMessageSize = subFormikValues => {
        return subFormikValues.typeValidation == "MAX_MESSAGE_SIZE";
    }

    const isFieldExist = subFormikValues => {
        return subFormikValues.typeValidation == "FIELD_EXIST";
    }

    const isTimestampValidation = subFormikValues => {
        return subFormikValues.typeValidation == "TIMESTAMP_VALIDATION";
    }


    return (
        <>
            <Modal title={"New Item"} show={isShow} modalClosed={() => setIsShow(false)}>
                <Formik
                    enableReinitialize
                    initialValues={selectedValidator}
                    onSubmit={handleSave}
                >
                    {(subFormik) => (
                        <Form>
                            <Box>
                                <FormikSelect name="typeValidation" label="Type Validation" items={validators} />
                                {isMandatory(subFormik.values) && <FormikField label={"Mandatory (separated by ;)"} name={"parameterValidation.mandatory"} />}
                                {isMaxField(subFormik.values) && <FormikField label={"Maximum field"} name={"parameterValidation.maxFields"} />}
                                {isMaxMessageSize(subFormik.values) && <FormikField label={"Maximum Size message"} name={"parameterValidation.maxMessageSize"} />}
                                {isFieldExist(subFormik.values) && <FormikField label={"Field exist"} name={"parameterValidation.fieldExist"} />}
                                {isBlackList(subFormik.values) && (
                                    <>
                                        <Box>
                                            <Grid container>
                                                <Grid item xs={6} className={classes.spacingLeft}>
                                                    <FormikField label={"Key"} name={"keyBlackList"} />
                                                </Grid>
                                                <Grid item xs={6} className={classes.spacingRight}>
                                                    <FormikField label={"Value"} name={"valueBlackList"} />
                                                </Grid>
                                            </Grid>
                                        </Box>
                                        <Box>
                                            <Box>
                                                <Button className={classes.modalButton}>Add Blacklist Item</Button>
                                                <Box className={classes.addValue}>
                                                    {subFormik.values.parameterValidation.blackList.map(item =>
                                                        (<Box className={classes.valueCard}>
                                                            <Typography className={classes.addValueText}>--</Typography>
                                                            <Button className={classes.closeButton}><CloseIcon className={classes.closeIcon} /></Button>
                                                        </Box>)
                                                    )}
                                                </Box>
                                            </Box>
                                        </Box>
                                    </>
                                )}
                                {isTimestampValidation(subFormik.values) && (
                                    <Box>
                                        <Box className={classes.checkboxWrapper}>
                                            <FormControlLabel
                                                control={<Field name="parameterValidation.validateInThePast" type="checkbox" component={Checkbox} />}
                                                label={<Typography className={classes.checkboxText}>Validate in past</Typography>}
                                            />
                                            {subFormik.values.parameterValidation.validateInThePast && <>
                                                <FormikField type={"number"} label={"Unit in past"} name={"parameterValidation.unitInThePast"} />
                                                <FormikSelect name="parameterValidation.chronoUnitInFuture" label="Chrono unit in the past" items={unitList} />
                                            </>}
                                        </Box>
                                        <Box className={classes.checkboxWrapper}>
                                            <FormControlLabel
                                                control={<Field name="parameterValidation.validateAfterFixedDate" type="checkbox" component={Checkbox} />}
                                                label={<Typography className={classes.checkboxText}>Validate events after fixed date in past</Typography>}
                                            />
                                            {subFormik.values.parameterValidation.validateAfterFixedDate && (
                                                <Box className={classes.dateWrapper}>
                                                    {subFormik.values.parameterValidation.validateAfterFixedDate && <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                                        <Typography variant="subtitle1" component="p" className={classes.label}>Fixed date</Typography>
                                                        <KeyboardDatePicker
                                                            disableToolbar
                                                            variant="inline"
                                                            format="MM/dd/yyyy"
                                                            margin="normal"
                                                            id="date-picker-inline"
                                                            value={subFormik.values.parameterValidation.lowerFixedDate}
                                                            onChange={value => subFormik.setFieldValue("paramterValidation.lowerFixedData", value)}
                                                            KeyboardButtonProps={{
                                                                'aria-label': 'change date',
                                                            }}
                                                        />
                                                    </MuiPickersUtilsProvider>
                                                    }
                                                </Box>
                                            )}

                                        </Box>
                                        <Box className={classes.checkboxWrapper}>
                                            <FormControlLabel
                                                control={<Field name="parameterValidation.validateInFuture" type="checkbox" component={Checkbox} />}
                                                label={<Typography className={classes.checkboxText}>Validate in future</Typography>}
                                            />
                                            {subFormik.values.parameterValidation.validateInFuture &&
                                                <>
                                                    <FormikField type={"number"} label={"Unit in future"} name={"parameterValidation.unitInFuture"} />
                                                    <FormikSelect name="parameterValidation.chronoUnitInFuture" label="Chrono unit in the future" items={unitList} />
                                                </>
                                            }

                                        </Box>
                                    </Box>
                                )}
                                <Box className={classes.footerWrapper}>
                                    <Box>
                                        <Button className={`${classes.modalButton} ${classes.outlineButton}`} onClick={() => setIsShow(false)}>cancel</Button>
                                        <Button onClick={(e) => {
                                             e.stopPropagation()
                                             subFormik.handleSubmit()
                                        }} className={`${classes.modalButton} ${classes.saveButton}`}>save</Button>
                                    </Box>
                                </Box>
                            </Box>

                        </Form>
                    )}
                </Formik>
            </Modal>
            <Box component="div">
                <Box component="div" className={classes.wrapper}>
                    <StepHeading name={"Select Validations"} optional={"(Optional)"} />
                    <Button variant="contained" className={classes.btn} onClick={handleModal}><AddIcon className={classes.buttonIcon} /> Add Validations</Button>
                </Box>
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Type</TableCell>
                                {/* <TableCell className={classes.tableHeadCell}>Field</TableCell> */}
                                <TableCell align="right" className={classes.tableHeadCell}>Action</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {values.processValidation.map((item, i) => (
                                <TableRow key={i}>
                                    <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                        {item.typeValidation}
                                    </TableCell>
                                    {/* <TableCell className={classes.tableBodyCell}>
                                        {row.field}
                                    </TableCell> */}
                                    <TableCell align="right" className={classes.tableBodyCell}>
                                        <Button onClick={() => handleEditClick(item)} className={classes.actionButton}>
                                            <EditIcon />
                                        </Button>
                                        <Button onClick={() => handleDeleteAction(item)} className={classes.actionButton}>
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

export default ConsumerValidations
