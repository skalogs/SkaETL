package io.skalogs.skaetl.generator.cart;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class UtilsCartData {

    private final CartProduct[] tabProduct = new CartProduct[]{
            CartProduct.builder().name("ragwear-appa-veste-legere-mocca").stock(5000).price(67).build(),
            CartProduct.builder().name("chillouts-mitch-hat-bonnet-black").stock(5000).price(88).build(),
            CartProduct.builder().name("celio-febelge-pullover").stock(5000).price(10).build(),
            CartProduct.builder().name("blundstone-boots-a-talons-rustic-brown").stock(5000).price(40).build(),
            CartProduct.builder().name("nudie-jeans-criss").stock(5000).price(30).build(),
            CartProduct.builder().name("baskets-basses-dark-cognac").stock(5000).price(79).build(),
            CartProduct.builder().name("tom-tailor-denim-veste-legere-woodland").stock(5000).price(30).build(),
            CartProduct.builder().name("rieker-mocassins-grau").stock(5000).price(24).build(),
            CartProduct.builder().name("puma-carson").stock(5000).price(48).build(),
            CartProduct.builder().name("brixton-dunns-fedora").stock(5000).price(48).build(),
            CartProduct.builder().name("calvin-klein-lunettes").stock(5000).price(35).build(),
            CartProduct.builder().name("liquor-n-poker-miami-retro-printed").stock(5000).price(35).build(),
            CartProduct.builder().name("carhartt-wip-gerald-lane-short").stock(5000).price(28).build(),
            CartProduct.builder().name("zign-mules-brown").stock(5000).price(7).build(),
            CartProduct.builder().name("volcom-broha-chemise-deep").stock(5000).price(8).build(),
            CartProduct.builder().name("minimum-eskild-pantalon").stock(5000).price(11).build(),
            CartProduct.builder().name("selected-homme-slhdavid-trainer-baskets").stock(5000).price(60).build(),
            CartProduct.builder().name("samsoe-and-samsoe-puka").stock(5000).price(12).build(),
            CartProduct.builder().name("jack-and-jones-jprdevon-resort").stock(5000).price(10).build(),
            CartProduct.builder().name("liquor-n-poker-miami-retro-printed-chemise").stock(5000).price(10).build(),
            CartProduct.builder().name("liquor-n-poker-miami-retro-chemise").stock(5000).price(10).build(),
            CartProduct.builder().name("only-and-sons-onsworker-wear-veste").stock(5000).price(10).build(),
            CartProduct.builder().name("river-island-chemise-black").stock(5000).price(25).build()
    };

    private final String[] tabCustomerFirstName = new String[]{
            "JAMES",
            "JOHN",
            "ROBERT",
            "MICHAEL",
            "WILLIAM",
            "DAVID",
            "RICHARD",
            "MARY",
            "PATRICIA",
            "LINDA",
            "BARBARA",
            "ELIZABETH",
            "JENNIFER",
            "MARIA",
            "SUSAN",
            "MARGARET",
            "DOROTHY",
            "LISA",
            "NANCY",
            "KAREN",
            "BETTY",
            "HELEN",
            "SANDRA",
            "DONNA",
            "CAROL",
            "RUTH",
            "SHARON"
    };
    private final String[] tabCustomerLastName = new String[]{
            "SMITH",
            "JOHNSON",
            "WILLIAMS",
            "BROWN",
            "JONES",
            "MILLER",
            "DAVI",
            "GARCIA",
            "RODRIGUEZ",
            "WILSON",
            "MARTINEZ",
            "ANDERSON",
            "TAYLOR",
            "THOMAS",
            "HERNANDEZ",
            "MOORE",
            "MARTIN",
            "JACKSON",
            "THOMPSON",
            "WHITE",
            "LOPEZ",
            "LEE",
            "GONZALEZ",
            "HARRIS",
            "CLARK",
            "LEWIS",
            "ROBINSON",
            "WALKER",
            "PEREZ",
            "HALL",
            "YOUNG",
            "ALLEN",
            "SANCHEZ",
            "WRIGHT",
            "KING",
            "SCOTT"
    };
    private final String[] tabDomain = new String[]{
            "yahoo.com",
            "gmail.com",
            "gmail.com",
            "outlook.com",
            "live.com",
            "yandex.com"
    };
    private final String[] tabIp = new String[]{
            "80.93.80",
            "85.192.192",
            "93.57.56",
            "77.241.80",
            "217.15.224",
            "78.159.200",
            "85.235.128"
    };

    private final Producer<String, String> producer;
    private final String topic;
    private final ObjectMapper mapper = new ObjectMapper();

    private Random RANDOM = new Random();

    public UtilsCartData(KafkaConfiguration kafkaConfiguration, KafkaUtils kafkaUtils) {
        producer = kafkaUtils.kafkaProducer();
        //topic = kafkaConfiguration.getTopic();
        topic = "demo-cart";
    }

    public String getUser(List<String> listCustomer){
        return listCustomer.get(RANDOM.nextInt(listCustomer.size()));
    }

    public CartProduct getProduct(){
        return tabProduct[RANDOM.nextInt(tabProduct.length)];
    }

    public String generateIp(){
        return tabIp[RANDOM.nextInt(tabIp.length)]+"."+RANDOM.nextInt(200);
    }

    public List<String> generateCustomer(Integer nbCustomer) {
        List<String> listCustomer = new ArrayList<>();
        for (int i = 0; i < nbCustomer; i++) {
            listCustomer.add(tabCustomerFirstName[RANDOM.nextInt(tabCustomerFirstName.length)] + "." + tabCustomerLastName[RANDOM.nextInt(tabCustomerLastName.length)] + "@" + tabDomain[RANDOM.nextInt(tabDomain.length)]);
        }
        return listCustomer;
    }
    //{type: "showProduct", name: ,customerEmail: }
    private void generateShowProduct(int minute, String customer, CartProduct cartProduct) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        sendToKafka(topic,GenerateCartData.builder()
                .type("showProduct")
                .ip(generateIp())
                .name(cartProduct.getName())
                .customerEmail(customer)
                .timestamp(df.format(newDate))
                .build());
    }
    //{type: "addToCart", quantity: , name:,customerEmail: }
    private void generateAddToCart(int minute, String customer, String ip, CartProduct cartProduct, Integer quantity) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        sendToKafka(topic,GenerateCartData.builder()
                .type("addToCart")
                .ip(ip)
                .name(cartProduct.getName())
                .customerEmail(customer)
                .quantity(quantity)
                .price(cartProduct.getPrice())
                .timestamp(df.format(newDate))
                .build());
    }
    //{type: "payment", customerEmail: ,totalItemPrice:, idPayment: , discount: discount }
    private void generatePayment(int minute, String customer, String ip, Double totalPrice, Integer discount) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        sendToKafka(topic,GenerateCartData.builder()
                .type("payment")
                .customerEmail(customer)
                .ip(ip)
                .totalItemPrice(totalPrice)
                .idPayment(UUID.randomUUID().toString())
                .discount(discount)
                .timestamp(df.format(newDate))
                .build());
    }
    //{type: "incident", customerEmail: , totalItemPrice: , idPayment:}
    private void generateIncident(int minute, String customer, String ip, Double totalPrice) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        sendToKafka(topic,GenerateCartData.builder()
                .type("incident")
                .customerEmail(customer)
                .ip(ip)
                .totalItemPrice(totalPrice)
                .idPayment(UUID.randomUUID().toString())
                .timestamp(df.format(newDate))
                .build());
    }
    //{type: "stock", name: , quantity: }
    private void generateStock(int minute, String customer, String name, Integer quantity) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        sendToKafka(topic,GenerateCartData.builder()
                .type("stock")
                .customerEmail(customer)
                .name(name)
                .quantity(quantity)
                .timestamp(df.format(newDate))
                .build());
    }

    public void generateScriptShowProduct(int nbShow, int minute, List<String> listCustomer) {
        for (int j = 0; j < nbShow; j++) {
            String customer = getUser(listCustomer);
            generateShowProduct(minute, customer, tabProduct[RANDOM.nextInt(tabProduct.length)]);;
        }
    }

    public void generateScriptAddToCart(int nbAddTocar, int minute, String customer, String ip, int nbProduct) {
        for (int j = 0; j < nbAddTocar; j++) {
            generateScenarioAddToCart(minute, customer, ip, nbProduct);
        }
    }

    public void generateScriptPaySucess(int nbPay, int minute, String customer, String ip, int nbProduct) {
        for (int j = 0; j < nbPay; j++) {
            generateScenarioPaySuccess(minute, customer, ip, nbProduct);
        }
    }

    public void generateScriptPayNotSucess(int nbPay, int minute, String customer, String ip, int nbProduct) {
        for (int j = 0; j < nbPay; j++) {
            generateScenarioPayNotSuccess(minute, customer, ip, nbProduct);
        }
    }

    public void generateScriptPaySameCustomerDifferentIp(int minute, String customer){
        generateScenarioPaySuccess(minute,customer,generateIp(),2);
        generateScenarioPayNotSuccess(minute+5,customer,generateIp(),4);
        generateScenarioPaySuccess(minute+8,customer,generateIp(),1);
    }

    private void generateScenarioAddToCart(int minute, String customer, String ip, int nbProduct) {
        for (int i = 0; i < nbProduct; i++) {
            CartProduct c = tabProduct[RANDOM.nextInt(tabProduct.length)];
            generateShowProduct(minute + i, customer, c);
            int quantity = RANDOM.nextInt(3);
            generateAddToCart(minute + i + 1, customer, ip , c, quantity);
        }
        int otherShow = RANDOM.nextInt(nbProduct);
        for (int i = 0; i < otherShow; i++) {
            CartProduct c = tabProduct[RANDOM.nextInt(tabProduct.length)];
            generateShowProduct(minute + i, customer, c);
        }
    }

    private void generateScenarioPaySuccess(int minute, String customer, String ip, int nbProduct) {
        Double totalPrice = Double.valueOf(0);
        for (int i = 0; i < nbProduct; i++) {
            CartProduct c = tabProduct[RANDOM.nextInt(tabProduct.length)];
            generateShowProduct(minute + i, customer, c);
            int quantity = RANDOM.nextInt(3);
            generateAddToCart(minute + i + 1, customer, ip, c, quantity);
            totalPrice += c.getPrice() * quantity;
            c.setStock(c.getStock()-quantity);
            generateStock(minute + i +1,customer,c.getName(),0);
        }
        int discountRandom = RANDOM.nextInt(5);
        Integer discount = null;
        if(discountRandom == 1){
            discount = 10;
        }
        if(discountRandom == 3){
            discount = 30;
        }
        generatePayment(minute+nbProduct+1,customer,ip,discount!=null ? totalPrice*discount/100 : totalPrice,discount);
        // Other search
        int otherShow = RANDOM.nextInt(nbProduct);
        for (int i = 0; i < otherShow; i++) {
            CartProduct c = tabProduct[RANDOM.nextInt(tabProduct.length)];
            generateShowProduct(minute + i, customer, c);
        }
    }

    private void generateScenarioPayNotSuccess(int minute, String customer, String ip, int nbProduct) {
        Double totalPrice = Double.valueOf(0);
        for (int i = 0; i < nbProduct; i++) {
            CartProduct c = tabProduct[RANDOM.nextInt(tabProduct.length)];
            generateShowProduct(minute + i, customer, c);
            int quantity = RANDOM.nextInt(3);
            generateAddToCart(minute + i + 1, customer, ip, c, quantity);
            totalPrice += c.getPrice() * quantity;
        }
        generateIncident(minute+nbProduct+1,customer,ip,totalPrice);
        // Other search
        int otherShow = RANDOM.nextInt(nbProduct);
        for (int i = 0; i < otherShow; i++) {
            CartProduct c = tabProduct[RANDOM.nextInt(tabProduct.length)];
            generateShowProduct(minute + i, customer, c);
        }
    }

    private Date addMinutesAndSecondsToTime(int minutesToAdd, int secondsToAdd, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.roll(Calendar.HOUR, 1);
        cal.add(Calendar.MINUTE, minutesToAdd);
        cal.add(Calendar.SECOND, secondsToAdd);
        return cal.getTime();
    }

    private void sendToKafka(String topic, GenerateCartData generateCartData) {
        try {
            String value = mapper.writeValueAsString(generateCartData);
            log.info("Sending topic {} value {}", topic, value);
            producer.send(new ProducerRecord(topic, value));
        } catch (Exception e) {
            log.error("Error sending to Kafka during generation ", e);
        }
    }

}
