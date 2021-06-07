package cn.knet.domain.utils;

import com.google.gson.Gson;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class EnumModelConverter implements ModelConverter {
    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {

        if (chain.hasNext()) {
            Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
            List<?> l = resolvedSchema.getEnum();
            if (l != null) {
                List l2 = l.stream().map(x -> {
                    if (x instanceof String) {
                        if (!((String) x).contains("{")) return x;
                        Gson son = new Gson();
                        Map m = son.fromJson((String) x, Map.class);
                        return Optional.ofNullable(m).map(y -> y.get("value")).orElse(x);
                    }
                    return x;
                }).collect(Collectors.toList());
                resolvedSchema.setEnum(l2);
            }
            return resolvedSchema;
        }
        return null;
    }
}