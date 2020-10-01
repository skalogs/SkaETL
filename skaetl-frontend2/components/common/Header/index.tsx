/*eslint-disable*/
import { AppBar, Box, Drawer, Hidden, List, ListItem, Toolbar, Typography } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import CloseIcon from '@material-ui/icons/Close';
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import MenuIcon from '@material-ui/icons/Menu';
import Link from "next/link";
import { useRouter, withRouter } from "next/router";
import React from "react";

const drawerWidth = 216

const useStyles = makeStyles(theme => ({
  header: {
    backgroundColor: theme.palette.type == "dark" ? "#1F1F1F" : "#ffffff",
    boxShadow: "none",
    zIndex: 999,
    width: "calc(100% - 226px)",
    borderBottom: theme.palette.type == "dark" ? "1px solid #0D0D0F" : "1px solid #F2F4F7",
    transition: "all 0.3s ease-in-out",
  },
  logoContainer: {
    // flexGrow: 1,
    display: "flex",
    flexFlow: "column",
  },
  drawerLogoContainer: {
    display: "flex",
    cursor: "pointer",
    justifyContent: "center",
    marginBottom: theme.spacing(5),
  },
  logoStyle: {
    width: 140,
    height: "auto",
    maxWidth: "100%",
  },
  toolbar: {
    minHeight: "55px",
    height: 55,
    display: "flex",
    padding: theme.spacing(0, 4),
    justifyContent: "space-between",
    [theme.breakpoints.down("xs")]: {
      // flexDirection: "row-reverse",
      padding: "10px 16px",
    },
  },
  list: {
    padding: theme.spacing(0),
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  listItem: {
    fontFamily: "'Open Sans', sans-serif",
    padding: theme.spacing(0.5),
    position: "relative",
    justifyContent: "center",
  },
  navItemText: {
    fontSize: 14,
    padding: theme.spacing(0, 0.5),
    color: theme.palette.type == "dark" ? "#ffffff" : "#1F1F1F",
    fontFamily: "'Open Sans', sans-serif",
  },
  drawerPaper: {
    width: drawerWidth,
    backgroundColor: "#122F50",
    color: "#fff",
    alignItems: "center",
    [theme.breakpoints.up("md")]: {
      position: "relative",
    },
  },
  iconNav: {
    width: theme.spacing(2.5),
    height: theme.spacing(2.5),
    margin: theme.spacing(0, 2),
  },
  navWrapper: {
    margin: theme.spacing(6, 0),
    width: "100%",
  },
  menuIcon: {
    marginRight: theme.spacing(1),
    color: "#1f1f1f",
  },
  iconStyle: {
    color: "#2164E8",
    fontSize: 32,
  },
  pageHeading: {
    fontSize: 16,
    fontWeight: 600,
    lineHeight: "22px",
    color: "#00274A",
    letterSpacing: "0.32px",
    fontFamily: "'Open Sans', sans-serif",
  },
  headerCollapse: {
    width: "calc(100% - 50px)",
    backgroundColor: theme.palette.type == "dark" ? "#1F1F1F" : "#ffffff",
    boxShadow: "none",
    zIndex: 999,
    borderBottom: theme.palette.type == "dark" ? "1px solid #0D0D0F" : "1px solid #F2F4F7",
    transition: "all 0.3s ease-in-out",
  }
}))

const subHeaderNavButtons = [
  {
    label: "Dashboard",
    path: "/",
  },
  {
    label: "Logstash Configuration",
    path: "/logstash-configuration",
  },
  {
    label: "Consumer Process",
    path: "/consumer-process",
  },
  {
    label: "Metric Process",
    path: "/metric-process",
  },
  {
    label: "Referential",
    path: "/referential",
  },
  {
    label: "Grok Pattern Simulation",
    path: "/simulate/grok",
  },
  {
    label: "Kafka Live",
    path: "/process/live",
  },
]

const Header = ({ isSidebarOpen }) => {
  const classes = useStyles()
  const router = useRouter()
  const [isDrawer, setIsDrawer] = React.useState(false)
  const handleDrawer = () => {
    setIsDrawer(prev => !prev)
  }

  const handleLogout = () => {
    localStorage.removeItem("userDefine");
    router.push("/login")
  }

  return (
    <>
      <AppBar className={isSidebarOpen ? classes.headerCollapse : classes.header} position="fixed">
        <Toolbar className={classes.toolbar}>
          <Box component="div" className={classes.logoContainer}>
            <Typography variant="h1" component="h1" className={classes.pageHeading}>{getTitle(router.pathname)}</Typography>
          </Box>
          <Hidden mdUp>
            <Box component="div">
              <ListItem onClick={handleDrawer} button className={classes.listItem}>
                {isDrawer ? <CloseIcon className={classes.iconStyle} /> : <MenuIcon className={classes.iconStyle} />}
              </ListItem>
            </Box>
          </Hidden>
          <Hidden mdDown>
            <List className={classes.list} component="nav">
              <ListItem button className={classes.listItem}>
                <ExitToAppIcon className={classes.menuIcon} />{" "}
                <Typography onClick={handleLogout} className={classes.navItemText}>Log Out</Typography>
              </ListItem>
            </List>
          </Hidden>
          <Hidden mdUp>
            <Drawer
              variant="temporary"
              open={isDrawer}
              onClose={handleDrawer}
              classes={{
                paper: classes.drawerPaper,
              }}
              ModalProps={{
                keepMounted: true,
              }}
            >
              <Box component="div" className={classes.navWrapper}>
                <Box component="div" className={classes.drawerLogoContainer}>
                  <Link href="/">
                    <img
                      className={classes.logoStyle}
                      src={"/static/images/landing/Logo/Bitwyre-beta-light-text.svg"}
                      alt="Bitwyre"
                    />
                  </Link>
                </Box>
                {subHeaderNavButtons.map(button => (
                  <SubHeaderNavButton key={button.path} path={button.path} label={button.label} />
                ))}
              </Box>
            </Drawer>
          </Hidden>
        </Toolbar>
      </AppBar>
    </>
  )
}

function getTitle(pathname) {
  if (pathname.includes('dashboard')) {
    return (
      <span>
        Dashboard
      </span>
    );
  } else if (pathname.includes('logstash-configuration')) {
    return (
      <span>
        Logstash Configuration
      </span>
    );
  } else if (pathname.includes('consumer-process')) {
    return (
      <span>
        Consumer Process
      </span>
    );
  } else if (pathname.includes('metric-process')) {
    return (
      <span>
        Metric Process
      </span>
    );
  } else if (pathname.includes('referential')) {
    return 'Referential';
  } else if (pathname.includes('simulate/grok')) {
    return 'Grok Pattern Simulation';
  } else if (pathname.includes('process/live')) {
    return (
      <span>
        Kafka Live
      </span>
    );
  } else {
    return 'Dashboard';
  }
}

export default Header

const subHeaderNavStyles = makeStyles(theme => ({
  listItem: {
    marginBottom: theme.spacing(1.5),
    padding: 0,
    width: "auto",
    backgroundColor: "#122F50",
    color: "#fff",
    "&:hover": {
      backgroundColor: "rgba(208, 214, 220, 0.2)",
      color: "#fff",
    },
  },
  listItemLink: {
    fontSize: 14,
    lineHeight: "19px",
    fontFamily: "'Open Sans', sans-serif",
    fontWeight: 500,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    padding: theme.spacing(1, 2),
    position: "relative",
  },
  activeLink: {
    backgroundColor: "rgba(208, 214, 220, 0.2)",
    color: "#fff",
  },
}))

const SubHeaderNavButton = props => {
  const router = useRouter()
  const classes = subHeaderNavStyles()
  return (
    <Link href={props.path}>
      <ListItem button className={`${classes.listItem} ${router.pathname === props.path &&
        classes.activeLink}`}>
        <Typography
          className={classes.listItemLink}
        >
          {props.label}
        </Typography>
      </ListItem>
    </Link>
  )
}