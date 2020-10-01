import { makeStyles } from '@material-ui/core';
import ErrorIcon from '@material-ui/icons/Error';
import FiberManualRecordIcon from '@material-ui/icons/FiberManualRecord';
import PauseCircleFilledIcon from '@material-ui/icons/PauseCircleFilled';
import PlayCircleFilledIcon from '@material-ui/icons/PlayCircleFilled';
import PowerSettingsNewIcon from '@material-ui/icons/PowerSettingsNew';


const useStyles = makeStyles(theme => ({
    enable: {
        color: "#067c30",
    },
    error: {
        color: "red"
    },
    power: {
        color: "blue"
    },
    disable: {
        color: "orange"
    },
    delete: {
        color: "#FE6847",
    },
    icon: {
        width: 18,
        marginRight: theme.spacing(1),
    }
}))

export default ({status}) => {
    const classes = useStyles()
    switch (status) {
        case "ENABLE":
            return <PlayCircleFilledIcon className={`${classes.icon} ${classes.enable}`} />
        case "ERROR":
            return <ErrorIcon className={`${classes.icon} ${classes.error}`} />
        case "DEGRADED":
            return <ErrorIcon className={`${classes.icon} ${classes.error}`} />
        case "INIT":
            return <PowerSettingsNewIcon className={`${classes.icon} ${classes.power}`} />
        case "CREATION":
            return <PowerSettingsNewIcon className={`${classes.icon} ${classes.power}`} />
        case "DISABLE":
            return <PauseCircleFilledIcon className={`${classes.icon} ${classes.disable}`} />
        default:
            return <FiberManualRecordIcon className={`${classes.icon} ${classes.enable}`} />
    }
}