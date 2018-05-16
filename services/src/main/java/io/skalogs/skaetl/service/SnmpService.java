package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.SnmpConfiguration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnmpService {

    @NonNull
    private final SnmpConfiguration snmpConfiguration;
    private Address targetAddress;
    private Snmp snmp = null;

    public void init() {

        try {
            targetAddress = GenericAddress.parse("udp:" + snmpConfiguration.getIpAddress() + "/" + snmpConfiguration.getPort());
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            USM usm = new USM(SecurityProtocols.getInstance()
                    .addDefaultProtocols(), new OctetString(
                    MPv3.createLocalEngineID()), 0);
            SecurityProtocols.getInstance()
                    .addPrivacyProtocol(new PrivAES192());
            SecurityModels.getInstance().addSecurityModel(usm);
            transport.listen();

            snmp.getUSM().addUser(
                    new OctetString("MD5DES"),
                    new UsmUser(new OctetString("MD5DES"), AuthMD5.ID,
                            new OctetString("UserName"), PrivAES128.ID,
                            new OctetString("UserName")));


        } catch (Exception ex) {
            log.error("Error in Sending Trap to ({}:{})", snmpConfiguration.getIpAddress(), snmpConfiguration.getPort());
            ex.printStackTrace();
        }
    }

    private Snmp getSnmp() {
        if (this.snmp == null)
            init();
        return this.snmp;
    }

    public void send(String value) {

        // Create Target
        UserTarget target = new UserTarget();
        target.setAddress(targetAddress);
        target.setRetries(1);
        target.setTimeout(11500);
        target.setVersion(SnmpConstants.version3);
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString("MD5DES"));

        // Create PDU
        ScopedPDU pdu = new ScopedPDU();
        pdu.setType(ScopedPDU.NOTIFICATION);
        pdu.add(new VariableBinding(SnmpConstants.sysUpTime));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, SnmpConstants.linkDown));
        pdu.add(new VariableBinding(new OID(snmpConfiguration.getTrapOid()), new OctetString("Major")));
        pdu.add(new VariableBinding(new OID(snmpConfiguration.getTrapOid()), new OctetString(value)));

        // Send the PDU
        try {
            Snmp s = this.getSnmp();
            if (s != null)
                s.send(pdu, target);

            //this.getSnmp().send(pdu, target);
            log.info("Sending Trap [{}] to ({}:{})", value, snmpConfiguration.getIpAddress(), snmpConfiguration.getPort());
            this.getSnmp().addCommandResponder(new CommandResponder() {
                public void processPdu(CommandResponderEvent arg0) {
                    log.info(arg0.toString());
                }
            });
        } catch (IOException ex) {
            log.error("Exception during SNMP sending", ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void terminate() throws IOException {
        snmp.close();
    }
}
