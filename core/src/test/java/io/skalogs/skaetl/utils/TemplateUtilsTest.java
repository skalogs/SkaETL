package io.skalogs.skaetl.utils;

import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateUtilsTest {

    @Test
    public void astext() {

        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setOrder(1);
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        // TODO Cacheable or Not ?
        templateResolver.setCacheable(false);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();

        context.setVariable("mavar","cela");
        String process = templateEngine.process("ceci [[${mavar}]]", context);
        assertThat(process).isEqualTo("ceci cela");

        String notInterpreted = templateEngine.process("ceci <span th:text=\"${mavar}\"></span>", context);
        assertThat(notInterpreted).isEqualTo("ceci <span th:text=\"${mavar}\"></span>");
    }

    @Test
    public void ashtml() {

        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setOrder(1);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // TODO Cacheable or Not ?
        templateResolver.setCacheable(false);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();

        context.setVariable("mavar","cela");
        String process = templateEngine.process("ceci [[${mavar}]]", context);
        assertThat(process).isEqualTo("ceci cela");

        String variant = templateEngine.process("ceci <span th:text=\"${mavar}\"></span>", context);
        assertThat(variant).isEqualTo("ceci <span>cela</span>");
    }



}