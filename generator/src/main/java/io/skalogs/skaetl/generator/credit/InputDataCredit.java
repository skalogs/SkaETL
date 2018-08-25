package io.skalogs.skaetl.generator.credit;

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

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InputDataCredit {
    private String type;
    private String apiName;
    private String uri;
    private String requestId;
    private String typeRequest;
    private String codeResponse;
    private String codeResponseWS;
    private String uriWS;
    private String typeRequestWS;
    private String nameWS;
    private String serviceBL;
    private String database;
    private String typeDB;
    private String requestDB;
    private Integer timeBL;
    private Integer timeDB;
    private Integer timeWS;
    private Integer timeGlobal;
    private String email;
    private String firstName;
    private String lastName;
    private String productName;
    private String provider;
    private Integer amount;
    private Integer creditDuration;
    private String topic;
    private StatusCredit statusCredit;
    private String user;
}
