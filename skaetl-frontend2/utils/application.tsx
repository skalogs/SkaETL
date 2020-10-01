import React, { useReducer, useContext, createContext } from "react"
import queryString from 'query-string';

const ApplicationDispatchContext = createContext(null);
const ApplicationStateContext = createContext(null);

const reducer = (state, action) => {
  switch (action.type) {
    case 'TOGGLE_SIDEBAR':
      return { ...state, isSidebarOpen: !state.isSidebarOpen }
    default:
      throw new Error(`Unknown action: ${action.type}`)
  }
}

const initialState = {
  isSidebarOpen: true
}

export const ApplicationProvider = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, initialState)
  return (
    <ApplicationDispatchContext.Provider value={dispatch}>
      <ApplicationStateContext.Provider value={state}>
        {children}
      </ApplicationStateContext.Provider>
    </ApplicationDispatchContext.Provider>
  )
}

const getFromQueryString = (key) => {
    return queryString.parse(window.location.search)[key];
};

export const useApplicationState = () => useContext(ApplicationStateContext)
export const useDispatchApplication = () => useContext(ApplicationDispatchContext)
