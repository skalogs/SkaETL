package io.skalogs.skaetl.domain;

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

public enum TypeValidation {
    FORMAT_JSON,
    MANDATORY_FIELD,
    BLACK_LIST_FIELD,
    MAX_FIELD,
    MAX_MESSAGE_SIZE,
    ADD_FIELD,
    DELETE_FIELD,
    RENAME_FIELD,
    FORMAT_DATE,
    FORMAT_BOOLEAN,
    FORMAT_GEOPOINT,
    FORMAT_DOUBLE,
    FORMAT_LONG,
    FORMAT_IP,
    FORMAT_KEYWORD,
    FORMAT_TEXT,
    FIELD_EXIST,
    TIMESTAMP_VALIDATION,
    LOOKUP_LIST,
    LOOKUP_EXTERNAL,
    HASH,
    ADD_GEO_LOCALISATION,
    CAPITALIZE,
    UNCAPITALIZE,
    UPPER_CASE,
    LOWER_CASE,
    SWAP_CASE,
    TRIM,
    FORMAT_EMAIL,
    ADD_CSV_LOOKUP,
    DATE_EXTRACTOR,
    TRANSLATE_ARRAY
}
