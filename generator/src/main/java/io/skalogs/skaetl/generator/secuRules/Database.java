package io.skalogs.skaetl.generator.secuRules;

/*-
 * #%L
 * generator
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
