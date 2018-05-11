package io.skalogs.skaetl.domain;

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

