package com.github.bestheroz.standard.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
@UtilityClass
public class MapperUtils {
  private final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper()
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

  //  public <T> T toObject(final Object source, final Class<T> targetType) {
  //    try {
  //      final String json = toString(source);
  //      return OBJECT_MAPPER.writeValueAsString(source);
  //    } catch (final JsonProcessingException e) {
  //      log.warn(ExceptionUtils.getStackTrace(e));
  //      throw new RuntimeException(e);
  //    }
  //  }
  //
  //  public Map<String, Object> toHashMap(final Object source) {
  //    return OBJECT_MAPPER.readValue(source, new TypeReference<HashMap<String, Object>>() {});
  //  }

  //  private final Gson GSON_INSTANCE =
  //      new GsonBuilder()
  //          .registerTypeAdapter(Instant.class, new InstantDeserializerTypeAdapter())
  //          .registerTypeAdapter(Instant.class, new InstantSerializerTypeAdapter())
  //          .registerTypeAdapter(Map.class, new MapDeserializerTypeAdapter())
  //          .registerTypeAdapter(HashMap.class, new MapDeserializerTypeAdapter())
  //          .registerTypeAdapter(LinkedTreeMap.class, new MapDeserializerTypeAdapter())
  //          .disableHtmlEscaping()
  //          .create();
  //
  //  public <T> T toObject(final Object source, final Class<T> targetType) {
  //    return GSON_INSTANCE.fromJson(toJsonElement(source), targetType);
  //  }
  //
  //  public <T> T toObject(final Object source, final Type targetType) {
  //    return GSON_INSTANCE.fromJson(toJsonElement(source), targetType);
  //  }
  //
  //  public JsonArray toJsonArray(final Object source) {
  //    return getCollectionTypeCatchException(source, JsonArray.class);
  //  }
  //
  //  public JsonObject toJsonObject(final Object source) {
  //    return getCollectionTypeCatchException(source, JsonObject.class);
  //  }
  //
  //  public Map<String, Object> toHashMap(final Object source) {
  //    return toObject(
  //        getCollectionTypeCatchException(source, JsonObject.class),
  //        new TypeToken<HashMap<String, Object>>() {}.getType());
  //  }
  //
  //  public JsonPrimitive toJsonPrimitive(final Object source) {
  //    return toJsonElement(source).getAsJsonPrimitive();
  //  }
  //
  //  public JsonElement toJsonElement(final Object source) {
  //    if (source instanceof String) {
  //      try {
  //        return JsonParser.parseString((String) source);
  //      } catch (final Throwable e) {
  //        // ignored
  //        return GSON_INSTANCE.toJsonTree(source);
  //      }
  //    } else {
  //      return GSON_INSTANCE.toJsonTree(source);
  //    }
  //  }
  //
  //  public String toString(final Object source) {
  //    return GSON_INSTANCE.toJson(source);
  //  }
  //
  //  public <T> List<T> toArrayList(final Object source, final Class<T> targetType) {
  //    final JsonArray array = MapperUtils.toObject(source, JsonArray.class);
  //    final List<T> lst = new ArrayList<>();
  //    for (final JsonElement json : array) {
  //      lst.add(MapperUtils.toObject(json, targetType));
  //    }
  //    return lst;
  //  }
  //
  //  public Gson getGsonObject() {
  //    return GSON_INSTANCE;
  //  }
  //
  //  @SuppressWarnings("unchecked")
  //  private <T> T getCollectionTypeCatchException(final Object source, final Class<T> targetType)
  // {
  //    final JsonElement jsonElement = toJsonElement(source);
  //    if (jsonElement.isJsonPrimitive()) {
  //      log.warn(jsonElement.toString());
  //      log.warn(ExceptionCode.ERROR_TRANSFORM_DATA.toString());
  //      throw new BusinessException(ExceptionCode.ERROR_TRANSFORM_DATA);
  //    } else if (jsonElement.isJsonNull()) {
  //      if (targetType == JsonObject.class) {
  //        return (T) new JsonObject();
  //      } else if (targetType == JsonArray.class) {
  //        return (T) new JsonArray();
  //      } else if (targetType == Map.class) {
  //        return (T) new HashMap<String, Object>();
  //      }
  //    }
  //
  //    return toObject(jsonElement, targetType);
  //  }
}
