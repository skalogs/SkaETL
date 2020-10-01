
import { useFormikContext } from 'formik';
import React from 'react';
import OutputStep from '../../common/OutputStep';


const Output = () => {
    const { values, setFieldValue } = useFormikContext<any>()
    console.log("Metric PROCESS OUTPUT STEP VALUES", values)
    const handleSave = updatedOutputs => {
        setFieldValue("processOutputs", updatedOutputs)
    }

    const handleDeleteAction = item => {
        const updatedOutputs = values.processOutputs.filter(e => e !== item)
        setFieldValue("processOutputs", updatedOutputs)
    }
    return <OutputStep processOutputs={values.processOutputs} onSave={handleSave} onDelete={handleDeleteAction}/>
}

export default Output
