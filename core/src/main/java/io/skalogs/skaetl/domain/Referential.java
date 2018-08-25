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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Wither;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Wither
public class Referential {
    private String idProcessReferential;
    private String nameProcessReferential;
    private String key;
    private String value;
    private String timestamp;
    private String project;
    private String type;
    @Builder.Default
    private RetentionLevel retentionLevel = RetentionLevel.year;
    @JsonProperty("metadataItemSet_nested")
    private Set<MetadataItem> metadataItemSet = new HashSet<MetadataItem>();
    private String timestampETL;
    private String creationDate;

    //tracking & validation
    private TypeReferential typeReferential;
    private String newTimestamp;

    //tracking
    @Builder.Default
    private Long nbChange = 0L;
    private String keyMetadataModified;
    private String newMetadataValue;
    private Long timeBetweenEventSec;


    //validation
    private Long timeExceeded;
    private Integer timeValidationAllFieldInSec;
    private String fieldChangeValidation;
    private Integer timeValidationFieldInSec;
}
