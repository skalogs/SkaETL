package io.skalogs.skaetl.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Iterators;
import io.skalogs.skaetl.service.parser.NitroParser;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class NitroParserTest {
    @Test
    public void should_convert_intoJson() {
        String nitro = "McAfeeWG|time_stamp=[01/Jan/2015:02:12:31 +0800]|auth_user=jsmith|src_ip=10.10.69.1|server_ip=172.224.247.54|host=www.mcafee.com|url_port=80|status_code=301|bytes_from_client=279|bytes_to_client=1149|categories=Business, Software/Hardware|rep_level=Minimal Risk|method=GET|url=http://www.mcafee.com/|media_type=text/html|application_name=|user_agent=Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)|block_res=0|block_reason=|virus_name=|hash=|filename=|filesize=753|";
        NitroParser n = new NitroParser();
        String result = n.process(nitro, null).getResult();
        JsonNode json = JSONUtils.getInstance().parseObj(result);

        assertThat(json.path("auth_user").asText()).isEqualTo("jsmith");
        assertThat(json.path("src_ip").asText()).isEqualTo("10.10.69.1");
        assertThat(json.path("server_ip").asText()).isEqualTo("172.224.247.54");
        assertThat(Iterators.size(json.fieldNames())).isEqualTo(17);
    }

    @Test
    public void should_null_noRootNitro() {
        String nitro = "time_stamp=[01/Jan/2015:02:12:31 +0800]|auth_user=jsmith|src_ip=10.10.69.1|server_ip=172.224.247.54|host=www.mcafee.com|url_port=80|status_code=301|bytes_from_client=279|bytes_to_client=1149|categories=Business, Software/Hardware|rep_level=Minimal Risk|method=GET|url=http://www.mcafee.com/|media_type=text/html|application_name=|user_agent=Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)|block_res=0|block_reason=|virus_name=|hash=|filename=|filesize=753|";
        NitroParser n = new NitroParser();
        String result = n.process(nitro, null).getMessageFailParse();
        assertThat(result).contains("Parse Process Nitro Exception ");
    }

    @Test
    public void should_no_attribute() {
        String nitro = "McAfeeWG|block_reason=|virus_name=|hash=||||filename=||";
        NitroParser n = new NitroParser();
        String result = n.process(nitro, null).getResult();
        assertThat(result).contains("{}");
    }

    @Test
    public void should_nokey() {
        String nitro = "McAfeeWG|=100|filename=toto.html|";
        NitroParser n = new NitroParser();
        String result = n.process(nitro, null).getResult();
        JsonNode json = JSONUtils.getInstance().parseObj(result);
        assertThat(json.path("filename").asText()).isEqualTo("toto.html");
        assertThat(Iterators.size(json.fieldNames())).isEqualTo(1);
    }
}
