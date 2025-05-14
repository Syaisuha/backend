package recipe_management.assessment.repository.jooq;

import static org.jooq.impl.DSL.*;

import java.util.function.BiFunction;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;

public class JooqUtil {

  public static <T> Condition andCondition(
      Condition condition, Field<T> field, BiFunction<Field<T>, T, Condition> operator, T value) {
    if (value != null) {
      return condition.and(operator.apply(field, value));
    }
    return condition;
  }

  public static Table<Record> tableTest(String field, String alias) {
    return table(field);
  }

  public static Field<Object> plaintoTsQuery(String searchTerm) {
    return function("plainto_tsquery", Object.class, val(searchTerm));
  }

  public static Field<Object> toTsVector(Field<String> field) {
    return function("to_tsvector", Object.class, field);
  }
}
