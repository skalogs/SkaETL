package io.skalogs.skaetl.serdes;

/*-
 * #%L
 * core
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

import io.skalogs.skaetl.utils.JSONUtils;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GenericDeserializer<T> implements Deserializer<T> {

    private final Class<T> clazz;

    public GenericDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void configure(Map<String, ?> map, boolean b) {

    }

    public T deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        T objToReturn = null;
        try {
            objToReturn = JSONUtils.getInstance().parse(bytes, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objToReturn;
    }

    public void close() {

    }
}
