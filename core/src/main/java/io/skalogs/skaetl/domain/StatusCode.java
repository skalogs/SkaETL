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

public enum StatusCode {
    success,
    event_size,
    invalid_json,
    max_fields,
    missing_mandatory_field,
    timestamp_too_much_in_future,
    timestamp_too_much_in_past,
    missing_timestamp,
    parsing_invalid_after_send,
    error_after_send_es,
    send_error,
    blacklist,
    not_in_whitelist,
    rename_error,
    add_error,
    delete_error,
    filter_drop_message,
    field_not_exist,
    invalid_format_timestamp,
    missing_mandatory_field_project,
    missing_mandatory_field_type;
}

