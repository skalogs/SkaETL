import React, { useEffect, useReducer, useContext, createContext } from "react";
import { requestHeaders } from "./config";
import { useRouter } from "next/router";
import { url } from "../config"

const initialState = {
  search: '',
  list: {
    isLoading: false,
    isError: false,
    error: null,
    data: []
  },
  configuration: {
    isError: false,
    error: null,
    confWizardStep: '',
    configurationLogstash: { 
      idConfiguration: "", 
      name: "", 
      input: [], 
      output: [], 
      confData: { 
        env: "", 
        apiKey: "", 
        category: "" 
      }, 
      statusCustomConfiguration: false,
      customConfiguration: "" ,
      tag: "",
      hostOutput: 'kafka.kafka',
      portOutput: '9092',
      topicOutput: 'processtopic',
      codecOutput: '',
      typeForced: '',
      path: '',
      typeInput: '',
      host: '',
      port: '',
      codec: '',
      topic: '',
      listTag: [],

    },
    typeDataIn: [{ label: "KAFKA", value: "KAFKA" }, { label: "UDP", value: "UDP" }, { label: "TCP", value: "TCP" }, { label: "FILE", value: "FILE" }, { label: "BEATS", value: "BEATS" }, { label: "User Advanced", value: "User Advanced" }],
    typeEnv: [{ label: "dev", value: "dev" }, { label: "uat", value: "uat" }, { label: "int", value: "int" }, { label: "prod", value: "prod" }],
  }
};

const reducer = (state, action) => {
  switch (action.type) {
    case "CONFIG_LIST_REQUEST":
      return {
        ...state,
        list: {
          ...state.list,
          isLoading: true,
          isError: false,
          error: null,
          data: [],
        },
      };
    case "CONFIG_LIST_SUCCESS":
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
    case "CONFIG_LIST_FAILURE":
      return {
        ...state,
        list: {
          ...state.list,
          isLoading: false,
          isError: true,
          error: action.payload,
        },
      };
    case "UPDATE_CONFIGURATION":
      return {
        ...state,
        configuration: {
          ...state.configuration,
          configurationLogstash: {
            ...state.configuration.configurationLogstash,
            ...action.payload
            }
          } 
        }
      case "SET_API_KEY":
        console.log("SET API KEY BEFORE", state.configuration)
        const updateState = {
          ...state,
          configuration: {
            ...state.configuration,
            configurationLogstash: {
              ...state.configuration.configurationLogstash,
              confData: {
                ...state.configuration.configurationLogstash.confData,
                apiKey: action.payload
              }
            }
          }
        }
        console.log("SET API KEY AFTER", updateState.configuration)
        return updateState
    default:
      throw new Error(`Unknown action: ${action.type}`)
  }
}

const defaultValue = {
  state: initialState,
}

// const LogstashConfigurationDispatchContext = createContext(null);
const LogstashConfigurationContext = createContext(null);


export function LogstashConfigurationProvider({ children }) {
  const logstashConfiguration = useLogstashConfigurationProvider();
  return (
    <LogstashConfigurationContext.Provider value={logstashConfiguration}>
      {children}
    </LogstashConfigurationContext.Provider>
  );
}
export const useLogstashConfiguration = () => {
  return useContext(LogstashConfigurationContext);
};

const useLogstashConfigurationProvider = () => {
  const [state, dispatch] = useReducer(reducer, initialState);
  const router = useRouter()
  
  const generateApiKey = () => {
    const apiKey =  'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16)
    })
    return apiKey
    // dispatch({ type: "SET_API_KEY", payload: apiKey})
  }

  const active = (idConfigSelect) => {
    try {
      return fetch(`${url.API_URL}/configuration/activeConfiguration?idConfiguration=${idConfigSelect}`, {
        method: "GET",
        headers: requestHeaders,
      });
    } catch (error) {

    }
  };

  const deactive = (idConfigSelect) => {
    try {
      return fetch(`${url.API_URL}/configuration/deactiveConfiguration?idConfiguration=${idConfigSelect}`, {
        method: "GET",
        headers: requestHeaders,
      });
    } catch (error) {

    }
  };

  const deleteConfig = (idConfigSelect) => {
    try {
      return fetch(`${url.API_URL}/configuration/deleteConfiguration?idConfiguration=${idConfigSelect}`, {
        method: "GET",
        headers: requestHeaders,
      });
    } catch (error) {
    }
  };

  const createConfig = async config => {
    console.log("CREATE CONFIG", config)
    try {
      const _ = await fetch(`${url.API_URL}/configuration/createConfiguration`, {
        method: "POST",
        headers: { ...requestHeaders, "Content-Type": "application/json" },
        body: JSON.stringify(config)
    }, );
      router.push("/logstash-configuration")
    } catch (error) {
      console.log("Failure during 'configuration/createConfiguration'")
    }
  };

  const editConfig = async config => {
    try {
      const _ = await fetch(`${url.API_URL}/configuration/editConfiguration`, {
        method: "POST",
        headers: { ...requestHeaders, "Content-Type": "application/json" },
        body: JSON.stringify(config)
    }, );
      router.push("/logstash-configuration")
    } catch (error) {
      console.log("Failure during 'configuration/editConfiguration'")
    }
  };


  const getConfig = async (idConfigSelect) => {
    try {
      const res = await fetch(`${url.API_URL}/configuration/getConfiguration`, {
        method: "GET",
        body: JSON.stringify({ idConfiguration: idConfigSelect }),
        headers: requestHeaders,
      });
      const data = await res.json();
    } catch (error) {
    }
  };


  const fetchList = async () => {
    dispatch({ type: "CONFIG_LIST_REQUEST" });
    try {
      const res = await fetch(`${url.API_URL}/configuration/findAll`, {
        method: "GET",
        headers: requestHeaders,
      });
      const data = await res.json();
      dispatch({ type: "CONFIG_LIST_SUCCESS", payload: data });
      console.log("LOGSTASH CONFIG API RESPONSE", data)
    } catch (error) {
      dispatch({ type: "CONFIG_LIST_FAILURE", payload: error });
    }
  };

  const fetchConfigurationbyId = async idConfiguration => {
    try {
        const res = await fetch(`${url.API_URL}/configuration/getConfiguration?idConfiguration=${idConfiguration}`, {
            method: "GET",
            headers: requestHeaders,
        });
        const data = await res.json();
        if (data.status === 404) {
            console.log("Error while fetching '/configuration/getConfiguration'");
            return
        } 
        dispatch({ type: "UPDATE_CONFIGURATION", payload: data });
    } catch (error) {
      console.log("Error while fetching '/configuration/getConfiguration'");
    }
}

  // useEffect(() => {
  //   fetchList();
  // }, []);

  return {
    state,
    dispatch,
    fetchList,
    active,
    deactive,
    deleteConfig,
    createConfig,
    editConfig,
    getConfig,
    generateApiKey,
    fetchConfigurationbyId
  };
};
