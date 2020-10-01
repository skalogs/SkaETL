import React, { useContext, createContext } from "react"
import { requestHeaders } from "./config";
import { url } from "../config"

const initialState = {
    viewMessageCapture: false,
    loadingValue: false,
    messageCapture: '',
    msgError: '',
    viewError: false,
    hostInput: '',
    portInput: '',
    topicInput: '',
    offsetInput: '',
    windowTimeInput: '2000',
    maxRecordsInput: '3',
    deserializer : 'org.apache.kafka.common.serialization.StringDeserializer',
    listCapture: [],
    typeOffset: ["latest", "earliest","last"],
    consumerState: {"statusProcess":"",
                           "processDefinition":{"idProcess":"","name":"","timestamp":""},
                           "registryWorkers":[]
                          },
}

const reducer = (state, action) => {
    switch (action.type) {
        case "LIST_CAPTURE":
            return {
                ...state,
                listCapture: action.payload
            }
        case "RESET_LIST_CAPTURE":
            return {
                ...state,
                listCapture: []
            }
        case "SET_LOADING_VALUE":
            return {
                ...state,
                loadingValue: action.payload
            }
        case "SET_CONSUMER_STATE":
            return {
                ...state,
                consumerState: action.payload
            }
        default:
            throw new Error(`Unknown action: ${action.type}`)
    }
}

const ProcessContext = createContext(null);

export function ProcessProvider({ children }) {
    const processProvider = useProcessProvider();
    return <ProcessContext.Provider value={processProvider}>{children}</ProcessContext.Provider>
}
export const useProcess = () => {
    return useContext(ProcessContext)
}

const useProcessProvider = () => {
    const [state, dispatch] = React.useReducer(reducer, initialState)


    const launchCaptureKafka = async (values, actions) => {
        dispatch({ type: "SET_LOADING_VALUE", payload: true})
        const { topic, host, port, offset, window_time, max_records, deserializer_value } = values
        var urlencoded = new URLSearchParams();
        urlencoded.append("topic", topic);
        urlencoded.append("bootStrapServers", host+':'+port);
        urlencoded.append("maxRecords", topic);
        urlencoded.append("windowTime", window_time);
        urlencoded.append("offset", offset);
        urlencoded.append("deserializer", deserializer_value);
        try {
            const res = await fetch(`${url.API_URL}/simulate/raw/captureRaw`, {
                method: "POST",
                headers: { ...requestHeaders, "Content-Type": "application/x-www-form-urlencoded" },
                body: urlencoded
            }, );
            dispatch({ type: "SET_LOADING_VALUE", payload: false})
            const data = await res.json();
            const listCapture = []
            for (let i=0;i<data.length;i++){
                const itemString = res[i];
                const itemJson = JSON.parse(itemString);
                listCapture.push(itemJson);
            }
            dispatch({ type: "LIST_CAPTURE", payload: listCapture})
        } catch (error) {
            dispatch({ type: "SET_LOADING_VALUE", payload: false})
        }
    }

    const findConusmerState = async idProcess => {
        try {
            const res = await fetch(`${url.API_URL}/process/findConsumerState?idProcess=${idProcess}`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await res.json();
            if (data.status === 404) {
                console.log("Error while fetching '/process/findConsumerState'");
                return
            }
            dispatch({ type: "SET_CONSUMER_STATE", payload: data });
        } catch (error) {
            console.log("Error while fetching '/dsl/transformators'");
        }
    }

    const scaleUp = async idProcess => {
        try {
            const res = await fetch(`${url.API_URL}/process/scaleup?idProcess=${idProcess}`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await res.json();
            if (data.status === 404) {
                console.log("Error while fetching '/process/scaleup'");
                return
            }
            dispatch({ type: "SET_CONSUMER_STATE", payload: data });
        } catch (error) {
            console.log("Error while fetching '/process/scaleup'");
        }
    }

    const scaleDown = async idProcess => {
        try {
            const res = await fetch(`${url.API_URL}/process/scaledown?idProcess=${idProcess}`, {
                method: "GET",
                headers: requestHeaders,
            })
            const data = await res.json();
            if (data.status === 404) {
                console.log("Error while fetching '/process/scaledown'");
                return
            }
            dispatch({ type: "SET_CONSUMER_STATE", payload: data });
        } catch (error) {
            console.log("Error while fetching '/process/scaledown'");
        }
    }




    return {
        state,
        dispatch,
        findConusmerState,
        launchCaptureKafka,
        scaleDown,
        scaleUp
    }
}
