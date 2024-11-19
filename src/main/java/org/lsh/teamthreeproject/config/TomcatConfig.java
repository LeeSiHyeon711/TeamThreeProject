package org.lsh.teamthreeproject.config;

import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.catalina.connector.Connector;

@Configuration
public class TomcatConfig {
    @Bean
    public ConfigurableTomcatWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((Connector connector) -> {
            connector.setURIEncoding("UTF-8");
            connector.setUseBodyEncodingForURI(true);
        });
        return factory;
    }
}
// ISO -> UTF8 변환 설정 파일입니다. 지우지마세요!!!!!!!!!!!!!!!!!!!!!
// (한글 깨짐 문제 관련)
