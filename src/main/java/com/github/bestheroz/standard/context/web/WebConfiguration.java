package com.github.bestheroz.standard.context.web;

import com.github.bestheroz.standard.common.util.MapperUtils;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  @Override
  public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
    converters.clear();

    final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
        new MappingJackson2HttpMessageConverter();
    mappingJackson2HttpMessageConverter.setObjectMapper(MapperUtils.getObjectMapper());
    converters.add(mappingJackson2HttpMessageConverter);

    WebMvcConfigurer.super.configureMessageConverters(converters);
  }
}
