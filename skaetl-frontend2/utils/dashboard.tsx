import React, { useEffect, useReducer, useContext, createContext } from "react";
import queryString from "query-string";
import { requestHeaders } from "./config";
import { url } from "../config"

const DashboardDispatchContext = createContext(null);
const DashboardStateContext = createContext(null);

const initialState = {
  home: {
    isLoading: false,
    isError: false,
    error: null,
    data: null,
  },
  consumerProcessList: {
    isLoading: false,
    isError: false,
    error: null,
    data: [],
  },
  metricProcessList: {
    isLoading: false,
    isError: false,
    error: null,
    data: [],
  },
  dataCapture: {
    isLoading: false,
    isError: false,
    error: null,
    data: null,
  },
};

const reducer = (state, action) => {
  switch (action.type) {
    case "HOME_REQUEST":
      return {
        ...state,
        home: {
          ...state.home,
          isLoading: true,
          isError: false,
          error: null,
          data: null,
        },
      };
    case "HOME_SUCCESS":
      return {
        ...state,
        home: {
          ...state.home,
          isLoading: false,
          isError: false,
          error: null,
          data: action.payload,
        },
      };
    case "HOME_FAILURE":
      return {
        ...state,
        home: {
          ...state.home,
          isLoading: false,
          isError: true,
          error: action.payload,
        },
      };
    case "CONSUMER_PROCESS_LIST_REQUEST":
      return {
        ...state,
        consumerProcessList: {
          ...state.consumerProcessList,
          isLoading: true,
          isError: false,
          error: null,
          data: [],
        },
      };
    case "CONSUMER_PROCESS_LIST_SUCCESS":
      return {
        ...state,
        consumerProcessList: {
          ...state.consumerProcessList,
          isLoading: false,
          isError: false,
          error: null,
          data: action.payload,
        },
      };
    case "CONSUMER_PROCESS_LIST_FAILURE":
      return {
        ...state,
        consumerProcessList: {
          ...state.consumerProcessList,
          isLoading: false,
          isError: true,
          error: action.payload,
        },
      };
    case "METRIC_PROCESS_LIST_REQUEST":
      return {
        ...state,
        metricProcessList: {
          ...state.metricProcessList,
          isLoading: true,
          isError: false,
          error: null,
          data: [],
        },
      };
    case "METRIC_PROCESS_LIST_SUCCESS":
      return {
        ...state,
        metricProcessList: {
          ...state.metricProcessList,
          isLoading: false,
          isError: false,
          error: null,
          data: action.payload,
        },
      };
    case "METRIC_PROCESS_LIST_FAILURE":
      return {
        ...state,
        metricProcessList: {
          ...state.metricProcessList,
          isLoading: false,
          isError: true,
          error: action.payload,
        },
      };
    case "DATA_CAPTURE_REQUEST":
      return {
        ...state,
        dataCapture: {
          ...state.dataCapture,
          isLoading: true,
          isError: false,
          error: null,
          data: null,
        },
      };
    case "DATA_CAPTURE_SUCCESS":
      return {
        ...state,
        dataCapture: {
          ...state.dataCapture,
          isLoading: false,
          isError: false,
          error: null,
          data: action.payload,
        },
      };
    case "DATA_CAPTURE_FAILURE":
      return {
        ...state,
        dataCapture: {
          ...state.dataCapture,
          isLoading: false,
          isError: true,
          error: action.payload,
        },
      };
    default:
      throw new Error(`Unknown action: ${action.type}`)
  }
};

export const DashboardProvider = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, initialState);
  

  const fetchListProcess = async () => {
    dispatch({ type: "CONSUMER_PROCESS_LIST_REQUEST" });
    try {
      const res = await fetch(`${url.API_URL}/process/findAll`, {
          method: "GET",
          headers: requestHeaders,
      });
      const data = await res.json();
      dispatch({ type: "CONSUMER_PROCESS_LIST_SUCCESS", payload: data });
    } catch (error) {
      dispatch({ type: "CONSUMER_PROCESS_LIST_FAILURE", payload: error });
    }
  };

  const fetchMetricListProcess = async () => {
    dispatch({ type: "METRIC_PROCESS_LIST_REQUEST" });
    try {
      const res = await fetch(`${url.API_URL}/metric/listProcess`, {
          method: "GET",
          headers: requestHeaders,
      });
      const data = await res.json();
      dispatch({ type: "METRIC_PROCESS_LIST_SUCCESS", payload: data });
    } catch (error) {
      dispatch({ type: "METRIC_PROCESS_LIST_FAILURE", payload: error });
    }
  };
  
  useEffect(() => {
    const fetchHome = async () => {
      dispatch({ type: "HOME_REQUEST" });
      try {
        const res = await fetch(`${url.API_URL}/home/fetch`, {
            method: "GET",
            headers: requestHeaders,
        });
        const data = await res.json();
        dispatch({ type: "HOME_SUCCESS", payload: data });
      } catch (error) {
        dispatch({ type: "HOME_FAILURE", payload: error });
      }
    };
    fetchHome();
  }, []);

  useEffect(() => {
    const fetchDataCapture = async () => {
      dispatch({ type: "DATA_CAPTURE_REQUEST" });
      try {
        const res = await fetch(`${url.API_URL}/home/dataCapture`, {
            method: "GET",
            headers: requestHeaders,
        });
        const data = await res.json();
        dispatch({ type: "DATA_CAPTURE_SUCCESS", payload: data });
      } catch (error) {
        dispatch({ type: "DATA_CAPTURE_FAILURE", payload: error });
      }
    };
    fetchDataCapture();
  }, []);

  useEffect(() => {
    fetchMetricListProcess();
  }, []);

  useEffect(() => {
    fetchListProcess();
  }, []);

  return (
    <DashboardDispatchContext.Provider value={dispatch}>
      <DashboardStateContext.Provider value={{state, fetchListProcess, fetchMetricListProcess}}>
        {children}
      </DashboardStateContext.Provider>
    </DashboardDispatchContext.Provider>
  );
};

const getFromQueryString = (key) => {
  return queryString.parse(window.location.search)[key];
};

export const useDashboardState = () => useContext(DashboardStateContext);
export const useDashboardDispatch = () => useContext(DashboardDispatchContext);
