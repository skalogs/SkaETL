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

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProcessReferential extends ProcessDefinition {
    private Date timestamp;
    @Builder.Default
    private String idProcess = "";
    @Builder.Default
    private String name = "";
    @Builder.Default
    private String referentialKey = "";
    @Builder.Default
    private List<String> listAssociatedKeys = new ArrayList<>();
    @Builder.Default
    private List<String> listIdProcessConsumer = new ArrayList<>();
    @Builder.Default
    private List<String> listMetadata = new ArrayList<>();
    @Builder.Default
    private Boolean isNotificationChange = false;
    @Builder.Default
    private String fieldChangeNotification = "";
    @Builder.Default
    private Boolean isValidationTimeAllField = false;
    @Builder.Default
    private Integer timeValidationAllFieldInSec = 0;
    @Builder.Default
    private Boolean isValidationTimeField = false;
    @Builder.Default
    private Integer timeValidationFieldInSec = 0;
    @Builder.Default
    private String fieldChangeValidation = "";

    @Builder.Default
    private List<ProcessOutput> processOutputs = new ArrayList<>();

    @Builder.Default
    private List<ProcessOutput> trackingOuputs = new ArrayList<>();

    @Builder.Default
    private List<ProcessOutput> validationOutputs = new ArrayList<>();

}
