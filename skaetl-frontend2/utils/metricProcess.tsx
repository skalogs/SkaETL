import React, { useEffect, useState, useContext, createContext } from "react"
import queryString from 'query-string';
import { requestHeaders } from "./config";
import { url } from "../config"

const initialState = {
    list: {
        data: [],
        isLoading: false,
        isError: false,
        error: null
    },
    process: {
        idProcess: "",
        name: "",
        selectedProcess: [],
        sourceProcessConsumers: [],
        selectedProcessB: [],
        sourceProcessConsumersB: [],
        joinType: "NONE",
        joinKeyFromA: "",
        joinKeyFromB: "",
        joinWhere: "",
        joinWindowSize: 5,
        joinWindowUnit: "MINUTES",
        functionName: "COUNT",
        functionField: "*",
        windowType: "TUMBLING",
        size: 5,
        sizeUnit: "MINUTES",
        advanceBy: 1,
        advanceByUnit: "MINUTES",
        where: "",
        groupBy: "",
        having: "",
        processOutputs: []
    },
    functions: [{ label: "COUNT", value: "COUNT" }, { label: "COUNT-DISTINCT", value: "COUNT-DISTINCT" }, { label: "SUM", value: "SUM" }, { label: "AVG", value: "AVG" }, { label: "MIN", value: "MIN" }, { label: "MAX", value: "MAX" }, { label: "STDDEV", value: "STDDEV" }, { label: "MEAN", value: "MEAN" }],
    windowTypes: [{ label: "TUMBLING", value: "TUMBLING" }, { label: "HOPPING", value: "HOPPING" }, { label: "SESSION", value: "SESSION" }],
    // windowTypes: ["TUMBLING", "HOPPING", "SESSION"],
    timeunits: [{ label: "SECONDS", value: "SECONDS" }, { label: "MINUTES", value: "MINUTES" }, { label: "HOURS", value: "HOURS" }, { label: "DAYS", value: "DAYS" }],
    // timeunits: ["SECONDS", "MINUTES", "HOURS", "DAYS"],
    joinTypes: [{ label: "NONE", value: "NONE" }, { label: "INNER", value: "INNER" }, { label: "OUTER", value: "OUTER" }, { label: "LEFT", value: "LEFT" }],
    filterFunctions: [],
    sourceProcesses: [],
    listProcess: [],
}

const reducer = (state, action) => {
    switch (action.type) {
        case "LIST_REQUEST":
            return {
                ...state,
                list: {
                    ...state.list,
                    isLoading: true,
                    isError: false,
                    error: null,
                },
            };
        case "LIST_SUCCESS":
            return {
                ...state,
                list: {
                    ...state.list,
                    isLoading: false,
                    isError: false,
                    error: null,
                    data: action.payload,
                },
            };
        case "FETCH_PARSERS_SUCCESS":
            return {
                ...state,
                typeParser: action.payload
            }
        case "FETCH_TRANSFORMATORS_SUCCESS":
            return {
                ...state,
                transformators: action.payload
            }
        case "FETCH_VALIDATORS_SUCCESS":
            return {
                ...state,
                validators: action.payload
            }
        case "FETCH_FILTERS_SUCCESS":
            return {
                ...state,
                validators: action.payload
            }
        case "LIST_FAILURE":
            return {
                ...state,
                list: {
                    ...state.list,
                    isLoading: false,
                    isError: true,
                    error: action.payload,
                },
            };
        case "EDIT_PROCESS":
            return {
                ...state,
                process: action.payload.processDefinition,
            }
        case "UPDATE_PROCESS":
            return {
                ...state,
                process: action.payload
            }
        case "SET_SOURCE_PROCESSES":
            return {
                ...state,
                sourceProcesses: action.payload
            }
        case "FILTER_FUNCTIONS":
            return {
                ...state,
                filterFunctions: action.payload
            }
        case "RESET_PROCESS":
            return {
                ...state, 
                process: initialState.process
            }
        case "SET_LIST_PROCESS":
            return {
                ...state,
                listProcess: action.payload

            }
        default:
            throw new Error(`Unknown action: ${action.type}`)
    }
}

const MetricProcessContext = createContext(null);

export function MetricProcessProvider({ children }) {
    const metricProcess = useMetricProcessProvider();
    return <MetricProcessContext.Provider value={metricProcess}>{children}</MetricProcessContext.Provider>
}
export const useMetricProcess = () => {
    return useContext(MetricProcessContext)
}

const useMetricProcessProvider = () => {
    const [state, dispatch] = React.useReducer(reducer, initialState)

    const deactivateProcess = async idProcess => {
        try {
            return fetch(`${url.API_URL}/metric/deactivate?idProcess=${idProcess}`, {
                method: "GET",
                headers: requestHeaders,
            })
        }
        catch (error) {
        }
    }

    const activateProcess = async idProcess => {
        try {
            return fetch(`${url.API_URL}/metric/activate?idProcess=${idProcess}`, {
                method: "GET",
                headers: requestHeaders,
            })
        }
        catch (error) {
        }
    }

    const deleteProcess = async idProcess => {
        try {
            return fetch(`${url.API_URL}/metric/delete?idProcess=${idProcess}`, {
                method: "DELETE",
                headers: requestHeaders,
            })
        }
        catch (error) {
        }
    }

    const saveProcess = async processId => {
        try {
            const res = await fetch(`${url.API_URL}/metric/save?idProcess=${processId}`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await res.json()
        }
        catch (error) {
        }
    }

    const saveProcessPost = async processId => {
        try {
            const res = await fetch(`${url.API_URL}/metric/save`)
            const data = await res.json()
        }
        catch (error) {
        }
    }

    const initProcess = () => {
        try {
            return fetch(`${url.API_URL}/metric/init`, {
                method: "GET",
                headers: requestHeaders,
            })
            // const data = await response.json()
            // console.log("INIT METRIC PROCESS", data)
            // dispatch({ type: "UPDATE_PROCESS", payload: data });
        }
        catch (error) {
        }
    }

    const fetchProcessbyIdPromise = processId => {
        try {
            return fetch(`${url.API_URL}/metric/findById?idProcess=${processId}`, {
                method: "GET",
                headers: requestHeaders,
            });
        } catch (error) {
            console.log("ERRROR WHILE FETCHING PROCESS BY ID")
        }
    }

    const fetchReferentialByIdPromise = processId => {
        try {
            return fetch(`${url.API_URL}/referential/find?idReferential=${processId}`, {
                method: "GET",
                headers: requestHeaders,
            });
        } catch (error) {
            // dispatch({ type: "SET_SOURCE_PROCESSES_FAILURE", payload: error });
        }
    }

    const fetchReferentialById = async processId => {
        try {
            const res = await fetch(`${url.API_URL}/referential/find?idReferential=${processId}`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await res.json();
            if (data.status === 404) {
                console.log("Error while fetching '/findProcess?idProcess'");
                return
            }
            dispatch({ type: "SET_SOURCE_PROCESSES", payload: data });
            fetchList()
        } catch (error) {
            // dispatch({ type: "SET_SOURCE_PROCESSES_FAILURE", payload: error });
        }
    }

    const fetchList = async () => {
        dispatch({ type: "LIST_REQUEST" });
        try {
            const res = await fetch(`${url.API_URL}/metric/listProcess`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await res.json();
            dispatch({ type: "LIST_SUCCESS", payload: data });
            console.log("CONSUMER PROCESSES API RESPONSE", data)
        } catch (error) {
            dispatch({ type: "LIST_FAILURE", payload: error });
        }
    }

    const fetchConsumerProcessList = async () => {
        try {
            const response = await fetch(`${url.API_URL}/process/findAll`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await response.json()
            dispatch({ type: "SET_LIST_PROCESS", payload: data})
        } catch (error) {
            console.log("ERROR", error)
        }
    }

    const fetchFilterFunctions = async () => {
        try {
            const res = await fetch(`${url.API_URL}/dsl/filterFunctions`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await res.json();
            dispatch({ type: "FILTER_FUNCTIONS", payload: data });
        } catch (error) {
        }
    }

    const fetchParsers = async () => {
        try {
            const res = await fetch(`${url.API_URL}/dsl/parsers`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await res.json();
            console.log("SAND SAND", data);
            if (data.status === 404) {
                console.log("Error while fetching '/dsl/parser'");
                return
            }
            dispatch({ type: "FETCH_PARSERS_SUCCESS", payload: data });
        } catch (error) {
            console.log("Error while fetching '/dsl/parser'");
        }
    }

    const fetchTransformators = async () => {
        try {
            const res = await fetch(`${url.API_URL}/dsl/transformators`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await res.json();
            console.log("TYPE TRANSFORMATORS", data);
            if (data.status === 404) {
                console.log("Error while fetching '/dsl/transformators'");
                return
            }
            dispatch({ type: "FETCH_TRANSFORMATORS_SUCCESS", payload: data });
        } catch (error) {
            console.log("Error while fetching '/dsl/transformators'");
        }
    }

    const fetchValidators = async () => {
        try {
            const res = await fetch(`${url.API_URL}/dsl/validators`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await res.json();
            console.log("TYPE fetchValidators", data);
            if (data.status === 404) {
                console.log("Error while fetching '/dsl/validators'");
                return
            }
            dispatch({ type: "FETCH_VALIDATORS_SUCCESS", payload: data });
        } catch (error) {
            console.log("Error while fetching '/dsl/validators'");
        }
    }

    const fetchFilters = async () => {
        try {
            const res = await fetch(`${url.API_URL}/dsl/filters`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await res.json();
            console.log("TYPE FETCH FILTERS", data);
            if (data.status === 404) {
                console.log("Error while fetching '/dsl/filters'");
                return
            }
            dispatch({ type: "FETCH_FILTERS_SUCCESS", payload: data });
        } catch (error) {
            console.log("Error while fetching '/dsl/validators'");
        }
    }

    const updateMetricProcess = async values => {
        // const { name, windowType, size, sizeUnit, functionName, functionField, where, groupBy, having, joinType, joinWindowSize, joinWindowUnit, joinKeyFromA, joinKeyFromB, joinWhere, processOutputs, aggFunction, sourceProcessConsumers, sourceProcessConsumersB } = processValues
        try {
            return fetch(`${url.API_URL}/metric/update`, {
                method: "POST",
                headers: { ...requestHeaders, "Content-Type": "application/json" },
                body: JSON.stringify(values)
            }, );
            }
        catch (error) {
        }
    }

    const getFromQueryString = (key) => {
        return queryString.parse(window.location.search)[key];
    };

    return {
        state,
        dispatch,
        fetchList,
        deactivateProcess,
        activateProcess,
        deleteProcess,
        saveProcess,
        initProcess,
        saveProcessPost,
        fetchParsers,
        fetchTransformators,
        fetchValidators,
        fetchFilters,
        fetchReferentialByIdPromise,
        fetchProcessbyIdPromise,
        getFromQueryString,
        fetchReferentialById,
        fetchConsumerProcessList,
        fetchFilterFunctions,
        updateMetricProcess
    }
}
