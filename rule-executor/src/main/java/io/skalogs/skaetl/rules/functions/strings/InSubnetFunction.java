package io.skalogs.skaetl.rules.functions.strings;

import io.skalogs.skaetl.rules.functions.VarArgFilterFunction;
import io.skalogs.skaetl.utils.IPUtils;

import java.util.List;

public class InSubnetFunction extends VarArgFilterFunction<String> {
    public InSubnetFunction() {
        super("evaluates whether the string match an ip range or subnet", "myfield IN_SUBNET(\"10.2.0.0/24\")");
    }

    @Override
    public Boolean evaluateVarArgs(String ip, List<String> values) {
        if (ip == null) {
            return false;
        }
        for (String subnet : values) {
            if (IPUtils.isInSubnet(ip, subnet)) {
                return true;
            }
        }

        return false;
    }
}
