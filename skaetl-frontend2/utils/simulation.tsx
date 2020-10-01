import React, { useEffect, useState, useContext, createContext } from "react"
import { requestHeaders } from "./config";
import { url } from "../config"

const initialState = {
        patternChoice: "",
        listGrok: {
            data: [],
            isLoading: false,
            isError: false,
            error: null
        },
        listCapture: [],
        listCaptureAllPattern: [],
        grokSelect: '',
        textRawData: '',
        textSingleData: '',
        viewError: false,
        dialogAllPattern: false,
        msgError: '',
        viewMessageCapture: false,
        messageCapture: '',
        headers: [
                   { text: 'Pattern', align: 'left', value: 'pattern', width: '25%'},
                   { text: 'Value', align: 'left', value: 'value', width: '75%' }
                 ],
        simulateView: {
            hostInput: '',
            portInput: '',
            topicInput: '',
            pollingTimeInput: '2000',
            maxRecordsInput: '3',
            idProcess: '',
            process: '',
            msgError: '',
            viewMessageCapture: false,
            messageCapture: '',
            viewError: false,
            listCapture: [],
            messageActive: 'Simulation not active',
            simulationActive: false,
            choiceSimulation: '',
            typeSimulation: [{label: "Kafka", value: "Kafka"},{label: "Text", value:"Text"}],
            viewKafka: false,
            textRawData : '',
            viewText: false,
            itemText: null
        }
}

const reducer = (state, action) => {
    switch (action.type) {
        case "GROK_LIST_REQUEST":
            return {
                ...state,
                listGrok: {
                    ...state.listGrok,
                    isLoading: true,
                    isError: false,
                    error: null,
                },
            };
        case "GROK_LIST_SUCCESS":
            return {
                ...state,
                listGrok: {
                    ...state.listGrok,
                    isLoading: false,
                    isError: false,
                    error: null,
                    data: action.payload,
                },
            };
        case "GROK_LIST_FAILURE":
            return {
                ...state,
                listGrok: {
                    ...state.listGrok,
                    isLoading: false,
                    isError: true,
                    error: action.payload,
                },
            };
        case "CAPTURED_DATA":
            return {
                ...state,
                listCapture: action.payload
            }
        case "CAPTURED_DATA_ALL_PATTERN":
            return {
                ...state,
                listCaptureAllPattern: action.payload
            }
        case "RESET_TEXT_SINGLE_DATA":
            return {
                ...state,
                textSingleData: ""
            }
        case "UPDATE_SIMULATE_VIEW_PROCESS_AND_FIELDS":
            const { processInput: { port, host, topicInput } } = action.payload
            return {
                ...state,
                simulateView: {
                    ...state.simulateView,
                    process: action.payload,
                    portInput: port,
                    hostInput: host,
                    topicInput
                }
            }
        case "SET_AFTER_LAUNCH_SIMULATE":
            return {
                ...state,
                simulateView: {
                    ...state.simulateView,
                    simulationActive: true,
                    messageActive: "Simulation Running.."
                }
            }
        case "SET_LIST_CAPTURE": {
            return {
                ...state,
                simulateView: {
                    ...state.simulateView,
                    listCapture: action.payload,
                }
            }
        }
        case "SET_ITEM_TEXT":
            return {
                ...state,
                simulateView: {
                    ...state.simulateView,
                    itemText: action.payload
                }
            }
        default:
            throw new Error(`Unknown action: ${action.type}`)
    }
}

const SimulationContext = createContext(null);

export function SimulationProvider({ children }) {
    const simulation = useSimulationProvider();
    return <SimulationContext.Provider value={simulation}>{children}</SimulationContext.Provider>
}
export const useSimulation = () => {
    return useContext(SimulationContext)
}

const useSimulationProvider = () => {
    const [state, dispatch] = React.useReducer(reducer, initialState)

    const captureData = async (values, actions) => {
        const { valueList, grokPattern } = values
        var urlencoded = new URLSearchParams();
        urlencoded.append("grokPattern", grokPattern);
        urlencoded.append("valueList", valueList);
        try {
            const res = await fetch(`${url.API_URL}/admin/grok/simulate`, {
                method: "POST",
                headers: { ...requestHeaders, "Content-Type": "application/x-www-form-urlencoded" },
                body: urlencoded
            }, );
            const data = await res.json();
            dispatch({ type: "CAPTURED_DATA", payload: data})
            console.log("CAPTURE DATA RESPONSE", data)
        } catch (error) {
            // dispatch({ type: "SET_SOURCE_PROCESSES_FAILURE", payload: error });
        }
    }

    const captureDataAllPattern = async (values, actions) => {
        const { textSingleData } = values
        var urlencoded = new URLSearchParams();
        urlencoded.append("value", textSingleData);
        try {
            const res = await fetch(`${url.API_URL}/admin/grok/simulateAllPattern`, {
                method: "POST",
                headers: { ...requestHeaders, "Content-Type": "application/x-www-form-urlencoded" },
                body: urlencoded
            }, );
            const data = await res.json();
            dispatch({ type: "CAPTURED_DATA_ALL_PATTERN", payload: data})
        } catch (error) {
            // dispatch({ type: "SET_SOURCE_PROCESSES_FAILURE", payload: error });
        }
    }

    const fetchGrokList = async () => {
        dispatch({ type: "GROK_LIST_REQUEST" });
        try {
            const res = await fetch(`${url.API_URL}/admin/grok/find`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await res.json();
            dispatch({ type: "GROK_LIST_SUCCESS", payload: data });
        } catch (error) {
            dispatch({ type: "GROK_LIST_FAILURE", payload: error });
        }
    }

    const fetchConsumerProcessList = () => {
        try {
            return fetch(`${url.API_URL}/process/findAll`, {
                method: "GET",
                headers: requestHeaders,
            });
        } catch (error) {
            console.log("ERROR", error)
        }
    }

    const fetchProcessbyId = async id => {
        try {
            const res = await fetch(`${url.API_URL}/process/findProcess?idProcess=${id}`, {
                method: "GET",
                headers: requestHeaders,
            });
            const data = await res.json();
            if (data.status === 404) {
                console.log("Error while fetching '/findProcess?idProcess'");
                return
            } 
            dispatch({ type: "UPDATE_SIMULATE_VIEW_PROCESS_AND_FIELDS", payload: data });
        } catch (error) {
            console.log("ERROR FETCH PROCESS BY ID",error )
        }
    }

    const launchSimulate = async process => {
        try {
            return fetch(`${url.API_URL}/simulate/launchSimulate`, {
                method: "POST",
                headers: { ...requestHeaders, "Content-Type": "application/json" },
                body: JSON.stringify(process)
            }, );
            
        } catch (error) {
            console.log("LAUNCH SIMULATE ERROR", error)
        }
    }

    const launchCaptureKafka = async (values, actions) => {
        const payload = {"bootStrapServers" : values.hostInput+':'+values.portInput, "maxPollRecords" : values.maxRecordsInput, "pollingTime" : values.pollingTimeInput};
        try {
            const res = await fetch(`${url.API_URL}/simulate/capture`, {
                method: "POST",
                headers: { ...requestHeaders, "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            }, );
            const data = await res.json()
            dispatch({ type: "SET_LIST_CAPTURE", payload: data})
            if (data.length === 0) {
                // show ,message to user
            } 
            
        } catch (error) {
            console.log("ERROR : /simulate/capture")
        }
   }

   const launchCaptureText = async (values, actions, process) => {
    const payload = {"textSubmit" : values.textSubmit, "processConsumer": process};
    
    try {
        const res = await fetch(`${url.API_URL}/simulate/captureFromText`, {
            method: "POST",
            headers: { ...requestHeaders, "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        }, );
        const data = await res.json()
        dispatch({ type: "SET_ITEM_TEXT", payload: data})
        
    } catch (error) {
        console.log("ERROR : /simulate/captureFromText")
    }
}
    

    return {
        state,
        dispatch,
        fetchGrokList,
        captureData,
        fetchProcessbyId,
        launchSimulate,
        launchCaptureKafka,
        captureDataAllPattern,
        launchCaptureText,
        fetchConsumerProcessList,
    }
}
