package ru.sbtqa.smartly.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.log4j.Logger;
import java.net.URL;

/** Утилитный класс для работы с JSON */
public class JsonUtils {

    public static final Logger LOG = Logger.getRootLogger();

    private static final String PATTERNS_NODE_NAME = "patterns";
    private static final String GRAPH_NODE_NAME    = "graph";
    private static final String CODE_NODE_NAME     = "code";

    private JsonUtils() {
        throw new IllegalAccessError("JsonUtils is utility class!");
    }

    /**
     * Метод получения json узла содержащего элементы шаблона таблицы
     *
     * @param templateResource - имя ресурса файла json содержащего шаблоны
     * @param patternCode      - код шаблона внутри json файла
     */
    public static ArrayNode loadJsonTemplate(final String templateResource, final String patternCode) {
        try {
            URL urlTemplate = JsonUtils.class.getClassLoader().getResource(templateResource);
            JsonNode rootNode = new ObjectMapper().readTree(urlTemplate);

            return (ArrayNode) rootNode
                    .get(PATTERNS_NODE_NAME)
                    .findParents(CODE_NODE_NAME)
                    .stream()
                    .filter(jsonNode -> patternCode.equals(jsonNode.get(CODE_NODE_NAME).textValue()))
                    .findFirst()
                    .orElseThrow(() -> new NullPointerException("JsonNode node not found!"))
                    .get(GRAPH_NODE_NAME);

        } catch (Exception e) {
            LOG.error("JsonUtils loadJsonTemplate Exception", e);
            return null;
        }
    }

}
