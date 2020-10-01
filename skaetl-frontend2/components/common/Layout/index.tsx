/*eslint-disable*/
import { Box, Container, makeStyles } from "@material-ui/core"
import React from "react"
import { useApplicationState, useDispatchApplication } from "../../../utils/application"
import Header from "../Header"
import Sidebar from "../Sidebar"

const useStyles = makeStyles(theme => ({
  "@global": {
    "*::-webkit-scrollbar": {
      width: "2px",
      height: "2px",
    },
    "*::-webkit-scrollbar-track": {
      "-webkit-box-shadow": "inset 0 0 6px rgba(0,0,0,0.00)",
    },
    "*::-webkit-scrollbar-thumb": {
      backgroundColor: "rgba(79,79,79,0.3)",
      outline: "1px solid slategrey",
      borderRadius: 3,
    },
  },
  container: {
    padding: theme.spacing(0),
    "& .MuiGrid-item": {
      padding: theme.spacing(0, 1),
    }
  },
  rootWrapper: {
    marginTop: 55,
    width: "auto",
    minHeight: "calc(100vh - 104px)",
    padding: theme.spacing(3),
    position: "relative",
    overflow: "auto",
    marginLeft: 226,
    backgroundColor: "#fff",
    transition: "all 0.3s ease-in-out",
  },
  collapsibleRootWrapper: {
    marginTop: 55,
    width: "auto",
    minHeight: "calc(100vh - 140px)",
    padding: theme.spacing(3),
    position: "relative",
    overflow: "auto",
    marginLeft: 50,
    backgroundColor: "#fff",
    transition: "all 0.3s ease-in-out",
    paddingBottom: 60,
  },
}))

const Layout = props => {
  const { isSidebarOpen } = useApplicationState()
  const dispatch = useDispatchApplication()
  const classes = useStyles()

  const handleSidebarToggle = () => {
    dispatch({
      type: 'TOGGLE_SIDEBAR'
    })
  }

  return (
    <>
      <Header isSidebarOpen={isSidebarOpen} />
      <Sidebar isSidebarOpen={isSidebarOpen} toggleSidebar={handleSidebarToggle} />
      <Box component="div" className={isSidebarOpen ? classes.collapsibleRootWrapper : classes.rootWrapper}>
        <Container className={classes.container} maxWidth="lg">{props.children}</Container>
      </Box>
    </>
  )
}

export default Layout
