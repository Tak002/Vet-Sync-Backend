package com.vetsync.backend.global.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class SwaggerUiInjectConfig {

    @Bean
    public SwaggerIndexTransformer indexPageTransformer(
            SwaggerUiConfigProperties swaggerUiConfig,
            SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            SwaggerWelcomeCommon swaggerWelcomeCommon,
            ObjectMapperProvider objectMapperProvider
    ) {
        return new SwaggerIndexPageTransformer(
                swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider
        ) {
            @Override
            public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain chain) throws IOException {
                Resource transformed = super.transform(request, resource, chain);
                return addCustomJs(transformed, "/swagger-token-auto.js");
            }

            private Resource addCustomJs(Resource index, String jsPath) {
                try {
                    String html = new String(index.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                    if (!html.contains(jsPath)) {
                        html = html.replace("</body>", "<script src=\"" + jsPath + "\"></script>\n</body>");
                    }
                    return new TransformedResource(index, html.getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    return index;
                }
            }
        };
    }
}
