package io.skalogs.skaetl.generator.secuRules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
@Slf4j
public class UtilsDatabase {

    public static String[] databaseTab = new String[]{
            "REF_CLIENT_PRODUCT",
            "REF_CLIENT_PRODUCT",
            "REF_CLIENT_PRODUCT",
            "REF_CLIENT_CONTRACT",
            "REF_CLIENT_CONTRACT",
            "REF_CLIENT_CONTRACT",
            "PRODUCTION_USER",
            "PRODUCTION_USER",
            "PRODUCTION_USER",
            "PRICING",
            "PRICING",
            "PRICING",
            "REPORTING",
            "REPORTING",
            "REPORTING"
    };
    public static String[] requestTab = new String[]{
            "SELECT * from Product p where p.id='****';",
            "SELECT * from Product p where p.id='****' and p.product_name='****';",
            "SELECT * from Product p, Client c where p.id='****' and p.client_id=c.id;",
            "SELECT contract.contract_name, contract.status from Contract c where c.current_state ='****';",
            "SELECT * from Contract c",
            "SELECT * from Contract c, Product p where c.id=p.contract_id",
            "SELECT contract.contract_name, contract.status from Contract c where c.current_state ='****';",
            "SELECT * from User;",
            "SELECT user.name from User user where user.admin='true';",
            "SELECT user.* from User user where user.status='active';",
            "SELECT * from Pricing p, User user where p.user_id=user.id and user.admin='true';",
            "SELECT * from Pricing p, Product product where p.product_id=p.id and product.price > '1000'",
            "SELECT * from Pricing p;",
            "SELECT * from Reporting from Reporting r where r.rating='bad';",
            "SELECT * from Reporting from Reporting r, User user where r.rating='bad' and user.id=r.user_id and user.id ='***';",
            "SELECT * from Reporting from Reporting r where r.rating='unknown';"
    };
    private final UtilsSecu utilsSecu;
    private Random RANDOM = new Random();

    public UtilsDatabase(UtilsSecu utilsSecu) {
        this.utilsSecu = utilsSecu;
    }

    public void generateData(int minute) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = utilsSecu.addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        ClientData client = utilsSecu.getClient();
        int rand = RANDOM.nextInt(10);
        String status = "OK";
        if(rand == 9){
            status = "KO";
        }
        utilsSecu.sendToKafka("database", Database.builder()
                .databaseIp("10.10.8."+(RANDOM.nextInt(5)+10))
                .user(client.username)
                .databaseName(databaseTab[RANDOM.nextInt(databaseTab.length)])
                .message("log database")
                .portDatabase(7878)
                .request(requestTab[RANDOM.nextInt(requestTab.length)])
                .statusAccess(status)
                .remoteIp(client.ipClient)
                .typeDatabase("ORACLE")
                .versionDatabase("12")
                .timestamp(df.format(newDate))
                .build());

    }
}
