
import { useFormikContext } from 'formik';
import React from 'react';
import { CreateReferentialProcessFields } from '../../../pages/referential/create';
import OutputStep from '../../common/OutputStep';

const Output = () => {
    const { values, setFieldValue } = useFormikContext<CreateReferentialProcessFields>()

    const handleSave = updatedOutputs => {
        setFieldValue("processOutputs", updatedOutputs)
    }

    const handleDeleteAction = item => {
        const updatedOutputs = values.processOutputs.filter(e => e !== item)
        setFieldValue("processOutputs", updatedOutputs)
    }

    console.log("VALUES ON REFRENTIALS OUTPUT STEP", values)
    return <OutputStep processOutputs={values.processOutputs} onDelete={handleDeleteAction} onSave={handleSave}/>
}

export default Output
