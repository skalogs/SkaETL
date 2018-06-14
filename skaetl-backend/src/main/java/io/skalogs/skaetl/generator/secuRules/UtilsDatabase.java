package io.skalogs.skaetl.generator.secuRules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

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

    private static String[] insertTab = new String[]{
            "INSERT INTO Product (\"productRefId\",\"price\") VALUES ('" + randomAlphabetic(10) + "','" + new Random().nextInt(1478) + "€');",
            "INSERT INTO Contract (\"contract_name\",\"status\") VALUES ('" + randomAlphabetic(10) + "','DISABLE');",
            "INSERT INTO User (\"name\",\"password\",\"role\",\"mail\") VALUES ('****','****','user_client','*****');",
            "INSERT INTO User (\"name\",\"password\",\"role\",\"mail\") VALUES ('****','','admin','*****');",
            "INSERT INTO User (\"name\",\"password\",\"role\",\"mail\") VALUES ('****','','admin','*****');",
            "INSERT INTO Pricing (\"priceRefId\",\"price\") VALUES ('" + randomAlphabetic(10) + "','" + new Random().nextInt(12222665) + "€');",
            "INSERT INTO Pricing (\"priceRefId\",\"price\") VALUES ('" + randomAlphabetic(10) + "','" + new Random().nextInt(12222665) + "€');",
            "INSERT INTO Reporting (\"client\",\"to_follow\",\"rating\") VALUES ('" + randomAlphabetic(10) + "','yes','bad');",
            "INSERT INTO Reporting (\"client\",\"to_follow\",\"rating\") VALUES ('" + randomAlphabetic(10) + "','yes','bad');",
    };
}
