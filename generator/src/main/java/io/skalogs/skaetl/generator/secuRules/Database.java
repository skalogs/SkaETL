package io.skalogs.skaetl.generator.secuRules;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Database {
    public String remoteIp;
    // Login
    public String user;
    // Database target by User
    public String databaseName;
    // Ip of Database
    public String databaseIp;
    // Port of Database
    @JsonProperty("database_port_l")
    public Integer portDatabase;
    // Type of database
    public String typeDatabase;
    // Version of database
    public String versionDatabase;
    // Meta of Database (prod, uat, whatever)
    public String metaDatabase;
    // Timestamp access or request execution
    public String timestamp;
    // Status access or request
    public String statusAccess;
    // OPTION:  request
    public String request;
    // Message error or acceptance
    public String message;
    // hostname database

}
