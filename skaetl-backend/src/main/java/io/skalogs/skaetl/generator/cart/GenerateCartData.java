package io.skalogs.skaetl.generator.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenerateCartData {
    private String type;
    private String ip;
    private String timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customerEmail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer quantity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double totalItemPrice;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer discount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String idPayment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer price;

}
