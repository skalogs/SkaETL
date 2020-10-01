import React from 'react';
import { useFormikContext } from 'formik';
import OutputStep from '../../common/OutputStep';

const Output = () => {

    const { values, setFieldValue } = useFormikContext<any>()
    console.log("CONSUMER PROCESS OUTPUT STEP VALUES", values)
    const handleSave = updatedOutputs => {
        setFieldValue("processOutput", updatedOutputs)
    }

    const handleDeleteAction = item => {
        const updatedOutputs = values.processOutput.filter(e => e !== item)
        setFieldValue("processOutput", updatedOutputs)
    }

    return <OutputStep processOutputs={values.processOutput} onSave={handleSave} onDelete={handleDeleteAction} />
    
}

export default Output
