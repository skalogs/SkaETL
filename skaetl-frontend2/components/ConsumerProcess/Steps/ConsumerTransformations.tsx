import { Box, Button, FormControlLabel, Grid, makeStyles, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import CloseIcon from '@material-ui/icons/Close';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import InfoIcon from '@material-ui/icons/Info';
import { Field, Form, Formik, useFormikContext } from 'formik';
import { Checkbox } from 'formik-material-ui';
import React from 'react';
import { useConsumerProcess } from '../../../utils/consumerProcess';
import FormikField from '../../common/FormikField';
import FormikSelect from '../../common/FormikSelect';
import Modal from '../../common/Modal';
import StepHeading from '../../common/StepHeading';
import { MyTextArea } from '../../common/Textarea';



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
        width: 220,
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
    footerWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
    },
    spacingLeft: {
        paddingLeft: "0 !important",
    },
    spacingRight: {
        paddingRight: "0 !important",
    },
    dateFormat: {
        padding: theme.spacing(2),
        border: "1px solid #01B3FF",
        marginBottom: theme.spacing(2),
    },
    dateFormatLink: {
        color: "#01B3FF",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 500,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        display: "flex",
        alignItems: "center",
    },
    linkIcon: {
        marginRight: theme.spacing(1),
    },
    linkText: {
        marginLeft: theme.spacing(1),
        color: "#01B3FF",
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
    checkboxSubText: {
        color: "#00274A",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 11,
        fontWeight: 400,
        lineHeight: "14px",
        letterSpacing: "0.35px",
        fontStyle: "normal",
        // maxWidth: 350,
    },
    addValue: {
        display: "inline-block",
        margin: theme.spacing(2, 0),
        width: "100%",
    },
    valueCard: {
        display: "inline-block",
        backgroundColor: "rgb(255, 152, 0)",
        borderRadius: 28,
        minWidth: 50,
        padding: theme.spacing(1, 1, 1, 2),
        margin: theme.spacing(0.75),
    },
    closeIcon: {
        fontSize: 14,
    },
    closeButton: {
        color: "rgb(255, 152, 0)",
        minWidth: 15,
        width: 20,
        height: 20,
        borderRadius: 25,
        marginLeft: theme.spacing(0.5),
        padding: theme.spacing(0.5),
        backgroundColor: "rgba(255, 255, 255, 0.4)",
        "&:hover": {
            color: "#fff",
        }
    },
    addValueText: {
        color: "#fff",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 400,
        lineHeight: "14px",
    },
    innerWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
    }
}))

const transformators = [
    { label: 'ADD_CSV_LOOKUP', value: 'ADD_CSV_LOOKUP' },
    { label: 'ADD_FIELD', value: 'ADD_FIELD' },
    { label: 'ADD_GEO_LOCALISATION', value: 'ADD_GEO_LOCALISATION' },
    { label: 'CAPITALIZE', value: 'CAPITALIZE' },
    { label: 'DATE_EXTRACTOR', value: 'DATE_EXTRACTOR' },
    { label: 'DELETE_FIELD', value: 'DELETE_FIELD' },
    { label: 'FORMAT_BOOLEAN', value: 'FORMAT_BOOLEAN' },
    { label: 'FORMAT_DATE', value: 'FORMAT_DATE' },
    { label: 'FORMAT_DOUBLE', value: 'FORMAT_DOUBLE' },
    { label: 'FORMAT_EMAIL', value: 'FORMAT_EMAIL' },
    { label: 'FORMAT_GEOPOINT', value: 'FORMAT_GEOPOINT' },
    { label: 'FORMAT_IP', value: 'FORMAT_IP' },
    { label: 'FORMAT_KEYWORD', value: 'FORMAT_KEYWORD' },
    { label: 'FORMAT_LONG', value: 'FORMAT_LONG' },
    { label: 'FORMAT_TEXT', value: 'FORMAT_TEXT' },
    { label: 'HASH', value: 'HASH' },
    { label: 'LOOKUP_EXTERNAL', value: 'LOOKUP_EXTERNAL' },
    { label: 'LOOKUP_LIST', value: 'LOOKUP_LIST' },
    { label: 'LOWER_CASE', value: 'LOWER_CASE' },
    { label: 'RENAME_FIELD', value: 'RENAME_FIELD' },
    { label: 'SWAP_CASE', value: 'SWAP_CASE' },
    { label: 'TRANSLATE_ARRAY', value: 'TRANSLATE_ARRAY' },
    { label: 'TRIM', value: 'TRIM' },
    { label: 'UNCAPITALIZE', value: 'UNCAPITALIZE' },
    { label: 'UPPER_CASE', value: 'UPPER_CASE' },
];

const ConsumerTransformations = () => {
    const classes = useStyles()
    const { values, setFieldValue } = useFormikContext<any>()
    const [isShow, setIsShow] = React.useState(false)
    const consumerProcess = useConsumerProcess()
    const [isEditing, setIsEditing] = React.useState(false)
    const { fetchTransformators } = consumerProcess
    const [selectedTransformer, setSelectedTransformer] = React.useState({
        typeTransformation: "",
        parameterTransformation: {
            "composeField": { "key": "", "value": "" },
            "formatDateValue": { "keyField": "", "srcFormat": "", "targetFormat": "", "targetField": "" },
            "keyField": "",
            "mapLookup": {},
            "externalHTTPData": { "url": "http://url:port", "refresh": "10", "httpMethod": "GET", "body": "" },
            "processHashData": { "field": "", "typeHash": "SHA256" },
            "csvLookupData": { "field": "", "data": "" }
        },
        replaceValue: "",
        replaceNewValue: "",
        listLookup: []
    })

    // React.useEffect(() => {
    //     fetchTransformators()
    // }, [])


    const handleModal = e => {
        e.preventDefault()
        setIsShow(!isShow)
    }

    const isCsvLookup = (subFormikValues) => {
        console.log("IS KEY FIELLD CSV", subFormikValues.typeTransformation)
        return subFormikValues.typeTransformation == "ADD_CSV_LOOKUP";
    }

    const isComposeField = (subFormikValues) => {
        return subFormikValues.typeTransformation == "ADD_FIELD" || subFormikValues.typeTransformation == "RENAME_FIELD";
    }

    const isDateField = (subFormikValues) => {
        return subFormikValues.typeTransformation == "FORMAT_DATE";
    }

    const isDateExtractor = (subFormikValues) => {
        return subFormikValues.typeTransformation == "DATE_EXTRACTOR";
    }

    const isLookupList = (subFormikValues) => {
        return subFormikValues.typeTransformation == "LOOKUP_LIST";
    }

    const isLookupExternal = (subFormikValues) => {
        return subFormikValues.typeTransformation == "LOOKUP_EXTERNAL";
    }

    const isHash = (subFormikValues) => {
        return subFormikValues.typeTransformation == "HASH";
    }

    const isKeyField = (subFormikValues) => {
        console.log("IS KEY FIELLD", subFormikValues.typeTransformation)
        const value = subFormikValues.typeTransformation;
        return value == "DELETE_FIELD" || value == "FORMAT_BOOLEAN" ||
            value == "FORMAT_GEOPOINT" || value == "FORMAT_DOUBLE" ||
            value == "FORMAT_LONG" || value == "FORMAT_IP" ||
            value == "FORMAT_KEYWORD" || value == "FORMAT_TEXT" ||
            value == "ADD_GEO_LOCALISATION" || value == "CAPITALIZE" ||
            value == "UNCAPITALIZE" || value == "LOWER_CASE" ||
            value == "UPPER_CASE" || value == "SWAP_CASE" ||
            value == "TRIM" || value == "FORMAT_EMAIL" ||
            value == "TRANSLATE_ARRAY";
    }

    const isFormatGeoPoint = (subFormikValues) => {
        return subFormikValues.typeTransformation == "FORMAT_GEOPOINT";

    }

    const formatField = (subFormikValues) => {
        if (subFormikValues.typeTransformation == "ADD_FIELD" || subFormikValues.typeTransformation == "RENAME_FIELD") {
            return subFormikValues.parameterTransformation.composeField.key;
        } else if (subFormikValues.typeTransformation == "HASH") {
            return subFormikValues.parameterTransformation.processHashData.field;
        } else if (subFormikValues.typeTransformation == "ADD_CSV_LOOKUP") {
            return subFormikValues.parameterTransformation.csvLookupData.field;
        } else if (subFormikValues.typeTransformation == "DATE_EXTRACTOR") {
            return subFormikValues.parameterTransformation.formatDateValue.targetField;
        } else {
            return subFormikValues.parameterTransformation.keyField;
        }
    }

    const handleEditClick = transformer => {
        setIsShow(true)
        setIsEditing(true)
        setSelectedTransformer(transformer)
    }


    const handleSave = (subFormikValues, actions) => {
        setIsShow(false)
        if (selectedTransformer.typeTransformation == "LOOKUP_LIST") {
            let result = {};
            for (let i = 0; i < selectedTransformer.listLookup.length; i++) {
                let itemLookup = selectedTransformer.listLookup[i];
                result[itemLookup.oldValue] = itemLookup.newValue;
            }
            selectedTransformer.parameterTransformation.mapLookup = result;
        }

        let updatedProcessTransformaters = []

        if (isEditing) {
            updatedProcessTransformaters = values.processTransformation.map(p => {
                if (p === selectedTransformer) {
                    return subFormikValues
                }
                return p
            })
        } else {
            updatedProcessTransformaters = [...values.processTransformation, subFormikValues]
        }
        setFieldValue("processTransformation", updatedProcessTransformaters)
        setIsEditing(false)
        setIsShow(false)
    }

    const handleDeleteAction = transformation => {
        const updatedProcessTransformaters = values.processTransformation.filter(t => t !== transformation)
        setFieldValue("processTransformation", updatedProcessTransformaters)
    }

    return (
        <>
            <Modal title={"New Item"} show={isShow} modalClosed={() => setIsShow(false)}>
                <Formik
                    enableReinitialize
                    initialValues={selectedTransformer}
                    onSubmit={handleSave}
                >
                    {(subFormik) => (
                        <Form>
                            <Box>
                                <FormikSelect name="typeTransformation" label="Type Transformation" items={transformators} />
                                {isComposeField(subFormik.values) && (
                                    <Grid container>
                                        <Grid item xs={6} className={classes.spacingLeft}>
                                            <FormikField label={"Key Field"} name={"parameterTransformation.composeField.key"} />
                                        </Grid>
                                        <Grid item xs={6} className={classes.spacingRight}>
                                            <FormikField label={"Value Field"} name={"parameterTransformation.composeField.value"} />
                                        </Grid>
                                    </Grid>
                                )}
                                {isCsvLookup(subFormik.values) && (
                                    <>
                                        <FormikField label={"Key Field"} name={"parameterTransformation.csvLookupData.field"} />
                                        <MyTextArea label={"Value Field"} name={"parameterTransformation.csvLookupData.data"} />
                                    </>
                                )}
                                {isDateField(subFormik.values) && (
                                    <>
                                        <Box>
                                            <Grid container>
                                                <Grid item xs={6} className={classes.spacingLeft}>
                                                    <FormikField label={"Key Field"} name={"parameterTransformation.formatDateValue.keyField"} />
                                                </Grid>
                                                <Grid item xs={6} className={classes.spacingLeft}>
                                                    <FormikField label={"Source Format"} name={"parameterTransformation.formatDateValue.srcFormat"} />
                                                </Grid>
                                                <Grid item xs={12} className={classes.spacingLeft}>
                                                    <FormikField label={"Target Format"} name={"parameterTransformation.formatDateValue.targetFormat"} />
                                                </Grid>
                                            </Grid>
                                        </Box>
                                    </>
                                )}
                                {isDateExtractor(subFormik.values) && (
                                    <>
                                        <Box>
                                            <Grid container>
                                                <Grid item xs={6} className={classes.spacingLeft}>
                                                    <FormikField label="Key Field" name={"parameterTransformation.formatDateValue.keyField"} />
                                                </Grid>
                                                <Grid item xs={6} className={classes.spacingRight}>
                                                    <FormikField label="Source Format" name={"parameterTransformation.formatDateValue.srcFormat"} />
                                                </Grid>
                                                <Grid item xs={6} className={classes.spacingLeft}>
                                                    <FormikField label="Target Field" name={"parameterTransformation.formatDateValue.targetField"} />
                                                </Grid>
                                                <Grid item xs={6} className={classes.spacingRight}>
                                                    <FormikField label="Target Format" name={"parameterTransformation.formatDateValue.targetFormat"} />
                                                </Grid>
                                            </Grid>
                                        </Box>
                                        <Box>
                                            <Box className={classes.dateFormat}>
                                                <Typography variant="subtitle1" component="p" className={classes.dateFormatLink}>
                                                    <InfoIcon className={classes.linkIcon} />
                                                    Date format should follow <a  target="_blank" href="https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns" className={classes.linkText}> Java Date Pattern Syntax </a>.
                                                </Typography>
                                            </Box>
                                        </Box>
                                    </>
                                )}

                                {isLookupList(subFormik.values) && (
                                    <>
                                        <Box>
                                            <FormikField label={"Limit on field"} subLabel={"(Optional)"} name={"parameterTransformation.keyField"} />

                                            <Grid container>
                                                <Grid item xs={5} className={classes.spacingLeft}>
                                                    <FormikField label={"Replace Value"} name={"replaceValue"} />
                                                </Grid>
                                                <Grid item xs={5}>
                                                    <FormikField label={"New Value"} name={"replaceNewValue"} />
                                                </Grid>
                                                <Grid item xs={2}>
                                                    <Button onClick={() => {
                                                        subFormik.setFieldValue("listLookup", [...subFormik.values.listLookup, { oldValue: subFormik.values.replaceValue, newValue: subFormik.values.replaceNewValue }])
                                                    }} className={classes.modalButton} style={{ "width": "auto", marginTop: 30 }}>Add</Button>
                                                </Grid>
                                            </Grid>
                                            <Box>
                                                <Box className={classes.addValue}>
                                                    {subFormik.values.listLookup.map(item => {
                                                        return (
                                                            <Box className={classes.valueCard}>
                                                                <Box className={classes.innerWrapper}>
                                                                    <Typography className={classes.addValueText}>{`${item.oldValue} -- ${item.newValue}`}</Typography>
                                                                    <Button onClick={() => {
                                                                        subFormik.setFieldValue("listLookup", subFormik.values.listLookup.filter(e => e !== item))
                                                                    }} className={classes.closeButton}><CloseIcon className={classes.closeIcon} /></Button>
                                                                </Box>
                                                            </Box>
                                                        )
                                                    })}
                                                </Box>
                                            </Box>
                                        </Box>
                                    </>
                                )}

                                {isHash(subFormik.values) && (
                                    <>
                                        <Grid container>
                                            <Grid item xs={6} className={classes.spacingLeft}>
                                                <FormikField label={"Field"} name={"parameterTransformation.processHashData.field"} />

                                            </Grid>
                                            <Grid item xs={6} className={classes.spacingRight}>
                                                <FormikSelect name="parameterTransformation.processHashData.typeHash" label="Type Hash" items={[{ label: "MURMUR3", value: "MURMUR3" }, { label: "SHA256", value: "SHA256" }]} />

                                            </Grid>
                                        </Grid>
                                    </>
                                )}
                                {isKeyField(subFormik.values) && (
                                    <FormikField label={"Field"} name={"parameterTransformation.keyField"} />
                                )}
                                {isLookupExternal(subFormik.values) && (
                                    <>
                                        <Box>
                                            <FormikField label={"URL"} name={"parameterTransformation.externalHTTPData.url"} />
                                            <Grid container>
                                                <Grid item xs={6} className={classes.spacingLeft}>
                                                    <FormikField label={"Method"} name={"parameterTransformation.externalHTTPData.httpMethod"} />
                                                </Grid>
                                                <Grid item xs={6} className={classes.spacingRight}>
                                                    <FormikField type={"number"} label={"Refresh in Seconds"} name={"parameterTransformation.externalHTTPData.refresh"} />
                                                </Grid>
                                            </Grid>
                                            <FormikField label={"Limit on Field"} subLabel={"(Optional)"} name={"parameterTransformation.keyField"} />
                                            <FormikField label={"Body with POST"} name={"parameterTransformation.externalHTTPData.body"} />
                                        </Box>
                                    </>
                                )}
                                {isFormatGeoPoint(subFormik.values) && (
                                    <Box className={classes.footerWrapper}>
                                        <Box>
                                            <FormControlLabel
                                                control={
                                                    <Field name="parameterTransformation.formatGeoJson" type="checkbox" component={Checkbox} />
                                                }
                                                label={<Typography className={classes.checkboxText}>Convert in GeoJSON format</Typography>}
                                            />
                                            <Typography variant="subtitle1" component="p" className={classes.checkboxSubText}>
                                                Not needed if your sending as text, tick this if your sending geopoint as an array with [latitude, longitude].
                                        </Typography>
                                        </Box>
                                    </Box>
                                )}
                                <Box style={{ textAlign: "right", marginTop: 10 }}>
                                    <Button className={`${classes.modalButton} ${classes.outlineButton}`} onClick={() => setIsShow(false)}>cancel</Button>
                                    <Button onClick={(e) => {
                                        e.stopPropagation()
                                        subFormik.handleSubmit()
                                    }} className={classes.modalButton}>save</Button>
                                </Box>
                            </Box>
                        </Form>
                    )}
                </Formik>
            </Modal>
            <Box component="div">
                <Box component="div" className={classes.wrapper}>
                    <StepHeading name={"Select Transformations"} optional={"(Optional)"} />
                    <Button variant="contained" className={classes.btn} onClick={handleModal}><AddIcon className={classes.buttonIcon} /> Add Transformations</Button>
                </Box>
                <TableContainer>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell className={classes.tableHeadCell}>Type</TableCell>
                                <TableCell className={classes.tableHeadCell}>Field</TableCell>
                                <TableCell align="right" className={classes.tableHeadCell}>Action</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {values.processTransformation.map((item, i) => (
                                <TableRow key={i}>
                                    <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                        {item.typeTransformation}
                                    </TableCell>
                                    <TableCell className={classes.tableBodyCell}>
                                        {formatField(item)}
                                    </TableCell>
                                    <TableCell align="right" className={classes.tableBodyCell}>
                                        <Button className={classes.actionButton} onClick={() => handleEditClick(item)}>
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

export default ConsumerTransformations
