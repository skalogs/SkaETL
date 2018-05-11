package io.skalogs.skaetl.utils;

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
