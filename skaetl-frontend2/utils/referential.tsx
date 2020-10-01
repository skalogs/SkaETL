import React, { useContext, createContext } from "react"
import { requestHeaders } from "./config";
import { useRouter } from "next/router";
import { url } from "../config"

const initialState = {
    list: {
        data: [],
        isLoading: false,
        isError: false,
        error: null
    },
    consumerProcess: new Map(),
    referential: {
        idReferential:"",
        listAssociatedKeys:[],
        name:"",
        referentialKey:"",
        listIdProcessConsumer:[],
        listMetadata:[],
        isNotificationChange: false,
        fieldChangeNotification:"",
        timeValidationInSec: 0,
        isValidationTimeAllField: false,
        isValidationTimeField: false,
        fieldChangeValidation: "",
        processOutputs: [],
        trackingOuputs: [],
        validationOutputs: [],
        listSelected: [],
        newEntry: "",
        newMetadata: ""
   },
   consumerProcesses: []
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
        case "CONSUMER_PROCESSES_LIST":
            return {
                ...state,
                consumerProcesses: action.payload
            }
        case "INIT_REFERENTIAL":
            return {
                ...state,
                referential: action.payload
            }
        default:
            throw new Error(`Unknown action: ${action.type}`)
    }
}

const ReferentialProcessContext = createContext(null);

export function ReferentialProcessProvider({ children }) {
    const referentialProcess = useReferentialProcessProvider();
    return <ReferentialProcessContext.Provider value={referentialProcess}>{children}</ReferentialProcessContext.Provider>
}
export const useReferentialProcess = () => {
    return useContext(ReferentialProcessContext)
}

const useReferentialProcessProvider = () => {
    const [state, dispatch] = React.useReducer(reducer, initialState)
    const router = useRouter()

    const initReferential = async () => {
        try {
            const response = await fetch(`${url.API_URL}/referential/init`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await response.json()
            dispatch({ type: "INIT_REFERENTIAL", payload: data });
        }
        catch (error) {
        }
    }

    const deactivateReferential = async id => {
        try {
            return fetch(`${url.API_URL}/referential/deactivate?idReferential=${id}`, {
                method: "GET",
                headers: requestHeaders,
            })
        }
        catch (error) {
        }
    }

    const activateReferential = async id => {
        try {
            return fetch(`${url.API_URL}/referential/activate?idReferential=${id}`, {
                method: "GET",
                headers: requestHeaders,
            })
        }
        catch (error) {
        }
    }

    const deleteReferential = async id => {
        try {
            return fetch(`${url.API_URL}/referential/delete?idReferential=${id}`, {
                method: "GET",
                headers: requestHeaders,
            })
        }
        catch (error) {
        }
    }

    const updateReferential = async values => {
        try {
            const _ = await fetch(`${url.API_URL}/referential/update`, {
              method: "POST",
              headers: { ...requestHeaders, "Content-Type": "application/json" },
              body: JSON.stringify(values)
          }, );
            // fetchList()
            router.push("/referential")
          } catch (error) {
            console.log("Failure during 'configuration/editConfiguration'")
          }
    }

    const fetchReferentialById = async referentialId => {
        try {
            const res = await fetch(`${url.API_URL}/referential/find?idReferential=${referentialId}`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await res.json();
            if (data.status === 404) {
                console.log("Error while fetching '/findProcess?idReferential'");
                return
            } 
            console.log("REFERENTIAL BY ID", data)
            dispatch({ type: "INIT_REFERENTIAL", payload: data });
        } catch (error) {
            // dispatch({ type: "SET_SOURCE_PROCESSES_FAILURE", payload: error });
        }
    }

    const fetchList = async () => {
        dispatch({ type: "LIST_REQUEST" });
        try {
            const res = await fetch(`${url.API_URL}/referential/findAll/`, {
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
            const res = await fetch(`${url.API_URL}/process/findAll`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await res.json();
            dispatch({ type: "CONSUMER_PROCESSES_LIST", payload: data });
        } catch (error) {
            console.log("Failure on /process/findAll")
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

    return {
        state,
        dispatch,
        fetchList,
        deactivateReferential,
        activateReferential,
        deleteReferential,
        updateReferential,
        initReferential,
        fetchReferentialById,
        fetchConsumerProcessList,
        fetchFilterFunctions,
    }
}
