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
public class Firewall {
    public Long id;
    // Timestamp Request
    public String timestamp;
    // Login or uuid
    public String user;

    // Source
    public String srcIp;
    // port src
    @JsonProperty("src_port_l")
    public Integer srcPort;
    // Dest
    public String destIp;
    // port Dest
    @JsonProperty("dest_port_l")
    public Integer destPort;

    // Status Blocked, Warning, Accept
    public String status;
    public String typeConnexion;
    // Name of equipment
    public String equipment;
    // Version of equipment
    public String equipmentVersion;
    // Ip of equipment
    public String equipmentIp;


}
