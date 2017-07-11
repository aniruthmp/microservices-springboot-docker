package com.example.venue.config;

import com.google.common.base.Optional;
import org.springframework.core.annotation.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * Created by a.c.parthasarathy on 7/11/17.
 * This class is required to
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class SpringDataParamBuilderPlugin implements ParameterBuilderPlugin {
    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        Optional<Param> requestParam = methodParameter.findAnnotation(Param.class);
        if (requestParam.isPresent()) {
            parameterContext.parameterBuilder()
                    .parameterType("query")
                    .name(requestParam.get().value())
                    .description(requestParam.get().value());
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}


