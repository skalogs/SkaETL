package io.skalogs.skaetl.utils;

/*-
 * #%L
 * rule-executor
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

import org.apache.commons.net.util.SubnetUtils;

public class IPUtils {

    public static Boolean isInSubnet(String ip, String subnet) {
        if (subnet.contains("*")) {
            String[] subnetGroup = subnet.split("\\.");
            String[] ipGroup = ip.split("\\.");
            for (int i = 0; i < ipGroup.length; i++) {
                if ("*".equals(subnetGroup[i])) {
                    continue;
                }
                if (!subnetGroup[i].equals(ipGroup[i])) {
                    return false;
                }
            }
            return true;
        }

        SubnetUtils utils = new SubnetUtils(subnet);
        return utils.getInfo().isInRange(ip);
    }

}
