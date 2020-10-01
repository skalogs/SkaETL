import { Box, makeStyles, Typography } from "@material-ui/core";
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowDropUpIcon from '@material-ui/icons/ArrowDropUp';
import React from "react";

interface Props {
    defaultText?: string
    labelName?: string
    optionsList?: any
    onSelect?: (value: string) => void
}

const useStyles = makeStyles(theme => ({
    selectWrapper: {
        display: "inline-block",
        minWidth: 250,
        width: "100%",
        position: "relative",
        marginBottom: theme.spacing(2),
    },
    selectedText: {
        backgroundColor: "#fff",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        height: 45,
        padding: theme.spacing(0, 2),
        color: "#0D0D0D",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        lineHeight: "19px",
        fontWeight: 600,
        border: "1px solid #AABCC480",
        borderRadius: 4,
        "&:hover": {
            border: "1px solid #01B3FF",
        }
    },
    selectOptions: {
        position: "absolute",
        width: "100%",
        maxHeight: 150,
        overflow: "auto",
        margin: 0,
        padding: 0,
        boxShadow: "0px 8px 15px #2E3B4029",
        backgroundColor: "#fff",
        zIndex: 9,
        "& li": {
            listStyleType: "none",
            padding: theme.spacing(1, 2),
            marginBottom: theme.spacing(0.5),
            cursor: "pointer",
            fontFamily: "'Open Sans', sans-serif",
            color: "#0D0D0D",
            fontSize: 14,
            lineHeight: "19px",
            fontWeight: 600,
            textTransform: "uppercase",
            userSelect: "none",
            "&:hover": {
                backgroundColor: "#F3F4F5",
            },
            "&:last-child": {
                marginBottom: 0,
            }
        }
    },
    active: {
        border: "1px solid #01B3FF",
    },
    label: {
        color: "#00274ADE",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 13,
        lineHeight: "18px",
        fontWeight: 600,
        marginBottom: theme.spacing(1),
    },
    selectArrow: {
        position: "absolute",
        right: 16,
        paddingLeft: theme.spacing(2),
    }
}))

const CustomSelect: React.FC<Props> = props => {
    const classes = useStyles()
    const { defaultText, optionsList, labelName, onSelect } = props
    const [defaultSelectText, setDefaultSelectText] = React.useState("")
    const [showOptionList, setShowOptionList] = React.useState(false)

    console.log("option list", optionsList)

    React.useEffect(() => {
        document.addEventListener("mousedown", handleClickOutside);
        setDefaultSelectText(defaultText)

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        }
    }, [])

    const handleClickOutside = e => {
        if (
            !e.target.classList.contains("custom-select-option") &&
            !e.target.classList.contains("selected-text")
        ) {
            setShowOptionList(false)
        }
    };


    const handleListDisplay = () => {
        setShowOptionList(!showOptionList)
    };

    const handleOptionClick = e => {
        console.log(e.target.getAttribute("data-name"))
        setDefaultSelectText(e.target.getAttribute("data-name"))
        onSelect(e.target.getAttribute("data-name"))
        setShowOptionList(false)
    };

    return (
        <Box className={classes.selectWrapper}>
            <Typography variant="subtitle1" component="p" className={classes.label}>{labelName}</Typography>
            <Box
                className={showOptionList ? `${classes.selectedText} ${classes.active}` : classes.selectedText}
                onClick={handleListDisplay}
            >
                {defaultSelectText}
                {showOptionList ? <ArrowDropUpIcon className={classes.selectArrow} /> : <ArrowDropDownIcon className={classes.selectArrow} />}
            </Box>
            {showOptionList && (
                <ul className={classes.selectOptions}>
                    {optionsList.map(option => {
                        return (
                            <li
                                className="custom-select-option"
                                data-name={option.name}
                                key={option.id}
                                onClick={handleOptionClick}
                            >
                                {option.name}
                            </li>
                        );
                    })}
                </ul>
            )}
        </Box>
    );
}

export default CustomSelect;
