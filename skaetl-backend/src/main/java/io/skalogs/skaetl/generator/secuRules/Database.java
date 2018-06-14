package io.skalogs.skaetl.generator.secuRules;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    public Date timestamp;
    // Status access or request
    public String statusAccess;
    // OPTION:  request
    public String request;
    // OPTION: type of request (select, update, delete, managing role)
    public String typeRequest;
    // Message error or acceptance
    public String message;
    // hostname database
    public String hostname;
    public String osHost;
    public String osVersion;
    public String osPatch;

}
