package rmap.controller.converter;

import org.springframework.core.convert.converter.Converter;
import rmap.entity.SearchingType;

import java.util.HashMap;
import java.util.Map;

public class StringToSearchingTypeConverter implements Converter<String, SearchingType> {

    private static final Map<String, SearchingType> queryParamValueMapping = new HashMap<>();

    public StringToSearchingTypeConverter() {
        queryParamValueMapping.put("notion-folder", SearchingType.NOTION_FOLDER);
        queryParamValueMapping.put("notion", SearchingType.NOTION);
    }

    @Override
    public SearchingType convert(String source) {
        if (queryParamValueMapping.containsKey(source)) {
            return queryParamValueMapping.get(source);
        }
        throw new IllegalArgumentException("잘못된 query param value 입니다. : " + source);
    }
}
