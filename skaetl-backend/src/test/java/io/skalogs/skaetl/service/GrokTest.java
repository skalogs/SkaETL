package io.skalogs.skaetl.service;

/*-
 * #%L
 * skaetl-backend
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

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class GrokTest {
    @Test
    public void test() {
        String value = "my line of log and metrics \n an other line \n and final log";
        String[] tabLine = value.split("\n");

        String value2 = "my line of log and metrics  an other line  and final log";
        String[] tabLine2 = value2.split("\n");

        log.error("" + tabLine.length);
        log.error("" + tabLine2.length);
    }
}
