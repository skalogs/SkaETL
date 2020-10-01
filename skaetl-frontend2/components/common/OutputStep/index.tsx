import { Box, Button, Grid, makeStyles, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { Form, Formik } from 'formik';
import React from 'react';
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
        width: 160,
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
        justifyContent: "flex-end",
        marginTop: theme.spacing(2),
    },
    spacingLeft: {
        paddingLeft: "0 !important",
    },
    spacingRight: {
        paddingRight: "0 !important",
    },
    templateWrapper: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        backgroundColor: "#fff",
        boxShadow: "rgba(0, 0, 0, 0.12) 0px 0px 5px 0",
        padding: theme.spacing(1, 2),
        cursor: "pointer",
    },
    templateInfo: {
        backgroundColor: "rgb(238, 238, 238)",
        padding: theme.spacing(2),
        boxShadow: "rgba(0, 0, 0, 0.12) 1px 0px 2px 0",
    },
    templateInfoText: {
        // fontFamily: "'Open Sans', sans-serif",
        fontSize: 16,
        fontWeight: 500,
        lineHeight: "19px",
        letterSpacing: "0.35px",
        fontStyle: "normal",
    },
    rotateDownIcon: {
        transform: "rotate(180deg)",
        transition: "all 0.3s ease-in-out",
    },
    downIcon: {
        transition: "all 0.3s ease-in-out",
    },
}))

const outputList = [
    { label: "KAFKA", optionalLabel: "processing dedicated", value: "KAFKA" },
    { label: "ELASTIC SEARCH", optionalLabel: "long term storage dedicated", value: "ELASTICSEARCH" },
    { label: "EMAIL", optionalLabel: "notification dedicated", value: "EMAIL" },
    { label: "SLACK", optionalLabel: "notification dedicated", value: "SLACK" },
    { label: "SNMP", optionalLabel: "notification dedicated", value: "SNMP" },
    { label: "SYSTEM_OUT", optionalLabel: "testing dedicated", value: "SYSTEM_OUT" },
]

const typeRetention =
    [{ label: "week", value: "week" }, { label: "month", value: "month" }, { label: "quarter", value: "quarter" }, { label: "year", value: "year" }, { label: "two_years", value: "two_years" }, { label: "five_years", value: "five_years" }]

const indexShapes = [
    { label: "daily", value: "daily" },
    { label: "monthly", value: "monthly" }
]

const OutputStep = ({ processOutputs, onSave, onDelete }) => {
    const classes = useStyles()
    const [isShow, setIsShow] = React.useState(false)
    const [isEditing, setIsEditing] = React.useState(false)
    const [isTemplateLanguage, setIsTemplateLanguage] = React.useState(false)
    const [selectedOutput, setSelectedOutput] = React.useState({
        typeOutput: "ELASTICSEARCH",
        parameterOutput: {
            "email": "",
            "topicOut": "output-topic",
            "elasticsearchRetentionLevel": "week",
            "indexShape": "daily",
            "webHookURL": "",
            "template": "",
        }
    })

    const handleTemplateLanguage = () => {
        setIsTemplateLanguage(prev => !prev);
    }

    const isKafkaTopic = (values) => {
        return values.typeOutput == "KAFKA";
    }
    const isElasticsearch = (values) => {
        return values.typeOutput == "ELASTICSEARCH";
    }
    const isSlack = (values) => {
        return values.typeOutput == "SLACK";
    }
    const isEmail = (values) => {
        return values.typeOutput == "EMAIL";
    }

    const handleModal = e => {
        e.preventDefault()
        setIsShow(!isShow)
    }

    const handleEditClick = output => {
        setIsShow(true)
        setIsEditing(true)
        setSelectedOutput(output)
    }

    const handleSave = values => {
        setIsShow(false)
        let modifiedOutputList = []
        if (isEditing) {
            modifiedOutputList = processOutputs.map(p => {
                if (p === selectedOutput) {
                    return values
                }
                return p
            })
        } else {
            modifiedOutputList = [...processOutputs, values]
        }
        onSave(modifiedOutputList)
    }

    return (
        <>
            <Modal title={"New Item"} show={isShow} modalClosed={() => setIsShow(false)}>
                <Formik
                    enableReinitialize
                    initialValues={selectedOutput}
                    onSubmit={handleSave}
                >
                    {({ values, handleSubmit }) => (
                        <Form>
                            <Box>
                                <FormikSelect name="typeOutput" label="Output type" items={outputList} />
                                {isElasticsearch(values) && (
                                    <Box>
                                        <Grid container>
                                            <Grid item xs={6} className={classes.spacingLeft}>
                                                <FormikSelect name="parameterOutput.elasticsearchRetentionLevel" label="Retention" optionalLabel={"How many time the data will be preserved"} items={typeRetention} />
                                            </Grid>
                                            <Grid item xs={6} className={classes.spacingRight}>
                                                <FormikSelect name="parameterOutput.indexShape" label="Index shape" optionalLabel={"Use monthly based index for low traffic indexes"} items={indexShapes} />
                                            </Grid>
                                        </Grid>
                                    </Box>
                                )}
                                {isKafkaTopic(values) && <FormikField name={"parameterOutput.topicOut"} label={"Topic out"} />}
                                {isEmail(values) && <FormikField name={"parameterOutput.email"} label={"Destination Email"} />}
                                {isSlack(values) && <FormikField name={"parameterOutput.webHookURL"} label={"Webhook URL"} />}
                                {(isEmail(values) || isSlack(values)) && (
                                    <>
                                        <MyTextArea name={"parameterOutput.template"} type={"textarea"} label={"Template"} />
                                        <Box component="div">
                                            <Box component="div" className={classes.templateWrapper} onClick={handleTemplateLanguage}>
                                                <Typography variant="subtitle1" component="p">Template language short description</Typography>
                                                <ExpandMoreIcon className={isTemplateLanguage ? classes.rotateDownIcon : classes.downIcon} />
                                            </Box>
                                            {isTemplateLanguage ? (
                                                <Box className={classes.templateInfo}>
                                                    <Typography className={classes.templateInfoText}>
                                                        If you want to insert a variable, you have to respect the following<br /> syntax: [[${"variable_name"}]]<br />
                                                    For instance: "The customer IP is [[${"client_ip"}]] for ..."<br /><br />

                                                    On Metric template, the specific variables are:<br />
                                                    [[${"result"}]] : The value of the Metric result<br />
                                                    [[${"rule_dsl"}]] : The request<br />
                                                    [[${"rule_name"}]] : The rule name<br />
                                                    [[${"project"}]] : The project name
                                                                                                        <br /><br />
                                                    On Consumer template, all the variables, present in the record, are authorised.
                                                </Typography>
                                                </Box>
                                            ) : null}
                                        </Box>
                                    </>
                                )}
                                <Box className={classes.footerWrapper}>
                                    <Box>
                                        <Button className={`${classes.modalButton} ${classes.outlineButton}`} onClick={() => setIsShow(false)}>cancel</Button>
                                        <Button onClick={(e) => {
                                            e.stopPropagation()
                                            handleSubmit()
                                        }} className={classes.modalButton}>Save</Button>
                                    </Box>
                                </Box>
                            </Box>
                        </Form>
                    )}
                </Formik>
            </Modal>
            <Box component="div">
                <Box component="div" className={classes.wrapper}>
                    <StepHeading name={"Select Destination"} />
                    <Button variant="contained" className={classes.btn} onClick={handleModal}><AddIcon className={classes.buttonIcon} /> Add Output</Button>
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
                            {processOutputs.map((row, index) => (
                                <TableRow key={index}>
                                    <TableCell component="th" scope="row" className={classes.tableBodyCell}>
                                        {row.typeOutput}
                                    </TableCell>
                                    {/* <TableCell className={classes.tableBodyCell}>
                                        {row.field}
                                    </TableCell> */}
                                    <TableCell align="right" className={classes.tableBodyCell}>
                                        <Button className={classes.actionButton}>
                                            <EditIcon onClick={() => handleEditClick(row)} />
                                        </Button>
                                        <Button onClick={() => onDelete(row)} className={classes.actionButton}>
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

export default OutputStep
