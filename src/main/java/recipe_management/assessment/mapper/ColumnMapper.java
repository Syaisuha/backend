package recipe_management.assessment.mapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ColumnMapper {

  protected Map<String, String> COLUMN_MAP = new LinkedHashMap<>();

  public String getColumnName(Optional<String> variableName) {
    if (variableName == null || variableName.isEmpty()) {
      return COLUMN_MAP.values().iterator().next();
    }

    String variableNameStr = variableName.get();
    String result = COLUMN_MAP.get(variableNameStr);
    if (result == null) {
      return COLUMN_MAP.values().iterator().next();
    }
    return result;
  }

  public String getColumnName(String variableName) {
    if (variableName == null || variableName.isEmpty()) {
      return COLUMN_MAP.values().iterator().next();
    }

    String variableNameStr = variableName;
    String result = COLUMN_MAP.get(variableNameStr);
    if (result == null) {
      return COLUMN_MAP.values().iterator().next();
    }
    return result;
  }

  public String getMultipleColumnName(String variableName) {
    if (variableName == null || variableName.isEmpty()) {
      return null;
    }

    String variableNameStr = variableName;
    String result = COLUMN_MAP.get(variableNameStr);
    if (result == null) {
      return null;
    }
    return result;
  }
}