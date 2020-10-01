import { useMetricProcess } from "../../../utils/metricProcess"
import * as React from "react"

const ProcessName = ({id}) => {
    const [name, setName] = React.useState("")
    const metricProcess = useMetricProcess()
    React.useEffect(() => {
        (async () => {
            const {  fetchReferentialByIdPromise } = metricProcess
            try {
                const res = await fetchReferentialByIdPromise(id)
                const data = await res.json()
                console.log("PROCESS NAMEE", data)

                setName(data.name)
            } catch (error) {
                console.log("ERROR", error)
            }})()
    }, [])

    return (
        <li>{name}</li>
    )
}

export default ProcessName