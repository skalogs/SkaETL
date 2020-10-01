/*eslint-disable*/
import { Box, Button, List, ListItem, makeStyles, Typography } from "@material-ui/core";
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import Link from "next/link";
import { useRouter } from "next/router";
import React from "react";

const useStyles = makeStyles(theme => ({
  sidebar: {
    position: "fixed",
    top: 0,
    width: 226,
    height: "100%",
    left: 0,
    zIndex: 999,
    backgroundColor: "#FAFBFB",
    // backgroundColor: "#fff",
    borderRight: theme.palette.type == "dark" ? "1px solid #0D0D0F" : "1px solid #F2F4F7",
    transition: "all 0.3s ease-in-out",
    [theme.breakpoints.down("sm")]: {
      display: "none",
    },
  },
  collapsibleSidebar: {
    position: "fixed",
    top: 0,
    width: 50,
    height: "100%",
    left: 0,
    zIndex: 999,
    backgroundColor: "#FAFBFB",
    // backgroundColor: "#fff",
    borderRight: theme.palette.type == "dark" ? "1px solid #0D0D0F" : "1px solid #F2F4F7",
    transition: "all 0.3s ease-in-out",
  },
  navWrapper: {
    padding: theme.spacing(1),
  },
  logoText: {
    fontSize: 16,
    color: "#01B3FF",
    lineHeight: "19px",
    letterSpacing: 0,
    fontFamily: "'Open Sans', sans-serif",
    fontWeight: 500,
    opacity: 1,
    width: "100%",
    transition: "opacity 0.3s ease-in-out",
  },
  collapseLogoText: {
    fontSize: 0,
    lineHeight: 0,
    letterSpacing: 0,
    fontFamily: "'Open Sans', sans-serif",
    fontWeight: 500,
    opacity: 0,
    overflow: "hidden",
    width: 0,
    transition: "opacity 0.3s ease-in-out",
  },
  logoWrapper: {
    marginBottom: theme.spacing(3),
    padding: theme.spacing(2),
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-start",
    transition: "all 0.3s ease-in-out",
  },
  collapsibleLogoWrapper: {
    marginBottom: theme.spacing(3),
    padding: theme.spacing(2, 0),
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    transition: "all 0.3s ease-in-out",
  },
  logo: {
    width: 30,
    marginRight: theme.spacing(1.5),
  },
  collapsibleLogo: {
    width: 30,
    marginRight: theme.spacing(0),
  },
  collapseSidebar: {
    position: "absolute",
    top: 44,
    right: -10,
  },
  collapseButtonWrapper: {
    width: 20,
    height: 20,
    overflow: "hidden",
    borderRadius: "50%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#01B3FF"
  },
  arrowIcon: {
    width: 12,
    height: 12,
    color: "#fff",
    paddingLeft: "5px",
  },
  spacingLeftZero: {
    paddingLeft: 0,
  },
  collapseButton: {
    minWidth: 20,
    height: 20,
    padding: 0,
  },
}))

const sideBarNavButtons = [
  {
    label: "Dashboard",
    path: "/",
    classNameIcon: "icon icon-Dashboard",
  },
  {
    label: "Logstash Configuration",
    path: "/logstash-configuration",
    classNameIcon: "icon icon-Logstash_Configuration",
  },
  {
    label: "Consumer Process",
    path: "/consumer-process",
    classNameIcon: "icon icon-Consumer_process",
  },
  {
    label: "Metric Process",
    path: "/metric-process",
    classNameIcon: "icon icon-Metric_Process",
  },
  {
    label: "Referential",
    path: "/referential",
    classNameIcon: "icon icon-Referential",
  },
  {
    label: "Grok Pattern Simulation",
    path: "/simulate/grok",
    classNameIcon: "icon icon-Grok_pattern_simulation",
  },
  {
    label: "Kafka Live",
    path: "/process/live",
    classNameIcon: "icon icon-Kafka_Live",
  },
]

const Sidebar = ({ isSidebarOpen, toggleSidebar }) => {
  const classes = useStyles()
  const router = useRouter()
  return (
    <Box component="div" className={isSidebarOpen ? classes.collapsibleSidebar : classes.sidebar}>
      <Box component="div" className={classes.navWrapper}>
        <Box component="div" className={isSidebarOpen ? classes.collapsibleLogoWrapper : classes.logoWrapper}>
          <Box component="div">
            <img className={isSidebarOpen ? classes.collapsibleLogo : classes.logo} src="/static/images/logo.png" alt="Skaetl" />
          </Box>
          <Typography variant="h2" component="h2" className={isSidebarOpen ? classes.collapseLogoText : classes.logoText}>SkaETL</Typography>
        </Box>
        <List component="nav">
          {sideBarNavButtons.map(button => (
            <SideBarNavButton
              key={button.path}
              path={button.path}
              classNameIcon={button.classNameIcon}
              label={button.label}
              isCollapse={isSidebarOpen}
            />
          ))}
        </List>
      </Box>
      <Box component="div" className={classes.collapseSidebar}>
        <Box component="div" className={classes.collapseButtonWrapper}>
          <Button onClick={toggleSidebar} className={classes.collapseButton}>
            {isSidebarOpen ? <ArrowForwardIosIcon className={`${classes.arrowIcon} ${classes.spacingLeftZero}`} /> : <ArrowBackIosIcon className={classes.arrowIcon} />}
          </Button>
        </Box>
      </Box>
    </Box>
  )
}

export default React.memo(Sidebar)

const useSidebarNavStyles = makeStyles(theme => ({
  listItem: {
    marginBottom: theme.spacing(2),
    padding: 0,
    borderRadius: 4,
    color: "#6282A3",
    backgroundColor: "transparent",
    height: 34,
    "&:hover": {
      color: "#fff",
      backgroundColor: "#01B3FF",
      boxShadow: "0px 3px 6px #40B4D829",
      "& .icon": {
        "&:before": {
          color: "#fff",
        }
      }
    },
  },
  listItemLink: {
    fontSize: 13,
    lineHeight: "18px",
    fontFamily: "'Open Sans', sans-serif",
    fontWeight: 400,
    letterSpacing: "0.26px",
    padding: theme.spacing(0, 1.125),
    display: "flex",
    alignItems: "center",
  },
  navtext: {
    fontSize: 13,
    lineHeight: "18px",
    fontFamily: "'Open Sans', sans-serif",
    fontWeight: 400,
    letterSpacing: "0.26px",
    opacity: 1,
    visibility: "visible",
    whiteSpace: "nowrap",
    width: "100%",
    transition: "opacity 0.3s ease-in-out",
  },
  collapseNavtext: {
    fontSize: 0,
    lineHeight: 0,
    fontFamily: "'Open Sans', sans-serif",
    fontWeight: 400,
    letterSpacing: "0.26px",
    width: 0,
    overflow: "hidden",
    visibility: "hidden",
    opacity: 0,
    transition: "opacity 0.3s ease-in-out",
  },
  activeLink: {
    color: "#fff",
    backgroundColor: "#01B3FF",
    boxShadow: "0px 3px 6px #40B4D829",
    "& .icon": {
      "&:before": {
        color: "#fff",
      }
    }
  },
  iconNav: {
    display: "flex",
    marginRight: theme.spacing(1.5),
  },
}))

const SideBarNavButton = props => {
  const classes = useSidebarNavStyles()
  const router = useRouter()

  const shouldBeActive = () => {
    if (props.path === router.pathname) {
      return true
    } 
    if (props.path.length > 1) {
      return router.pathname.includes(props.path)
    }
    return false
  }

  return (
    <Link href={props.path}>
      <ListItem button className={`${classes.listItem} ${shouldBeActive() && classes.activeLink}`}>
        <Box
          className={classes.listItemLink}
        >
          <Typography component="span" className={classes.iconNav}>
            <Typography component="span" title={props.label} className={props.classNameIcon} />
          </Typography>
          <Typography component="p" className={props.isCollapse ? classes.collapseNavtext : classes.navtext}>{props.label}</Typography>
        </Box>
      </ListItem>
    </Link>
  )
}

