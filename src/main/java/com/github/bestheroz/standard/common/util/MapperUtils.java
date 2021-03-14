package com.github.bestheroz.standard.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
@UtilityClass
public class MapperUtils {
  private final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper()
          .setDateFormat(new StdDateFormat().withColonInTimeZone(true))
          .registerModule(new JavaTimeModule())
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  public String toString(final Object source) {
    try {
      return OBJECT_MAPPER.writeValueAsString(source);
    } catch (final JsonProcessingException e) {
      log.warn(ExceptionUtils.getStackTrace(e));
      throw new RuntimeException(e);
    }
  }
}
