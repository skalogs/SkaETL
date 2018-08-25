package io.skalogs.skaetl.rules.metrics.serdes;

/*-
 * #%L
 * metric-api
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.HdrHistogram.DoubleHistogram;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DoubleHistogramDeserializer extends JsonDeserializer<DoubleHistogram> {

    @Override
    public DoubleHistogram deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(jsonParser.getBinaryValue());
        DoubleHistogram doubleHistogram = DoubleHistogram.decodeFromByteBuffer(byteBuffer, 0);
        return doubleHistogram;
    }
}
