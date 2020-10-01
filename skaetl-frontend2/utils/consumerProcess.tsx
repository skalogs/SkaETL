import React, { useEffect, useState, useContext, createContext } from "react"
import queryString from 'query-string';
import { requestHeaders } from "./config";
import { useRouter } from "next/router";
import { url } from "../config"

// const LogstashConfigurationDispatchContext = createContext(null);

const initialState = {
    list: {
        data: [],
        isLoading: false,
        isError: false,
        error: null
    },
    process: {
        name: "",
        idProcess: "",
        processInput: {
            host: "kafka.kafka",
            port: "9092",
            topicInput: "processtopic"
        },
        processParser: [],
        processTransformation: [],
        processValidation: [],
        processFilter: [],
        processOutput: []
    },
    filterFunctions: [],
    typeParser: [],
    transformators: [],
    validators: [],
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
        case "FILTER_FUNCTIONS":
            return {
                ...state,
                filterFunctions: action.payload
            }
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

        case "RESET_PROCESS":
            return {
                ...state, 
                process: initialState.process
            }
        default:
            throw new Error(`Unknown action: ${action.type}`)
    }
}

const ConsumerProcessContext = createContext(null);

export function ConsumerProcessProvider({ children }) {
    const consumerProcess = useConsumerProcessProvider();
    return <ConsumerProcessContext.Provider value={consumerProcess}>{children}</ConsumerProcessContext.Provider>
}
export const useConsumerProcess = () => {
    return useContext(ConsumerProcessContext)
}

const useConsumerProcessProvider = () => {
    const router = useRouter()
    const [state, dispatch] = React.useReducer(reducer, initialState)

    const deactivateProcess = idProcess => {
        try {
            return fetch(`${url.API_URL}/process/deactivate?idProcess=${idProcess}`,{
                method: "GET",
                headers: requestHeaders,
            })
            // return res.json()
            // fetchList()
        }
        catch (error) {
        }
    }

    const activateProcess = idProcess => {
        try {
            return fetch(`${url.API_URL}/process/activate?idProcess=${idProcess}`, {
                method: "GET",
                headers: requestHeaders,
            })
            // return res.json()
            // fetchList()
        }
        catch (error) {
        }
    }

    const deleteProcess = async idProcess => {
        try {
            return fetch(`${url.API_URL}/process/deleteProcess?idProcess=${idProcess}`, {
                method: "DELETE",
                headers: requestHeaders,
            })
        }
        catch (error) {
        }
    }

    const saveProcess = async processId => {
        try {
            const res = await fetch(`${url.API_URL}/process/saveProcess?idProcess=${processId}`)
            const data = await res.json()
        }
        catch (error) {
        }
    }

    const saveProcessPost = async values => {
        try {
            const _ = await fetch(`${url.API_URL}/process/save`, {
              method: "POST",
              headers: { ...requestHeaders, "Content-Type": "application/json" },
              body: JSON.stringify(values)
          }, );
            // fetchList()
            router.push("/consumer-process")
          } catch (error) {
            console.log("Failure during 'process/save'")
          }
    }

    const initProcess = async () => {
        try {
            const response = await fetch(`${url.API_URL}/process/init`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await response.json()
            console.log("INIT CONSUMER PROCESS", data)
            dispatch({ type: "UPDATE_PROCESS", payload: data });
        }
        catch (error) {
        }
    }

    const fetchProcessbyId = async processId => {
        try {
            const res = await fetch(`${url.API_URL}/process/findProcess?idProcess=${processId}`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await res.json();
            if (data.status === 404) {
                console.log("Error while fetching '/findProcess?idProcess'");
                return
            } 
            dispatch({ type: "UPDATE_PROCESS", payload: data });
            console.log("PROCESS API RESPONSE", data)
        } catch (error) {
            dispatch({ type: "LIST_FAILURE", payload: error });
        }
    }

    const fetchList = async () => {
        dispatch({ type: "LIST_REQUEST" });
        try {
            const res = await fetch(`${url.API_URL}/process/findAll`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await res.json();
            dispatch({ type: "LIST_SUCCESS", payload: data.reverse() });
        } catch (error) {
            dispatch({ type: "LIST_FAILURE", payload: error });
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

    const getFromQueryString = (key) => {
        return queryString.parse(window.location.search)[key];
    };

    // useEffect(() => {
    //     fetchList()
    // }, [])


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
        fetchFilterFunctions,
        fetchProcessbyId,
        getFromQueryString
    }
}
