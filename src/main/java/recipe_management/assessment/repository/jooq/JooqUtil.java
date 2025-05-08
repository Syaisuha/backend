package recipe_management.assessment.repository.jooq;

import static org.jooq.impl.DSL.*;

import java.util.function.BiFunction;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;

public class JooqUtil {

  public static <T> Condition condition(
      Field<T> field, BiFunction<Field<T>, T, Condition> comparison, T value) {
    return comparison.apply(field, value);
  }

  public static <T> Condition andCondition(
      Condition condition, Field<T> field, BiFunction<Field<T>, T, Condition> comparison, T value) {
    if (value != null && !value.equals("")) {
      condition = condition.and(comparison.apply(field, value));
    }
    return condition;
  }

  public static <T> Condition orCondition(
      Condition condition, Field<T> field, BiFunction<Field<T>, T, Condition> comparison, T value) {
    if (value != null && !value.equals("")) {
      condition = condition.or(comparison.apply(field, value));
    }
    return condition;
  }

  public static Table<Record> tableTest(String field, String alias) {
    return table(field);
  }

  /**
   * Calls the PostgreSQL function plainto_tsquery on the given search term. plainto_tsquery parses
   * the given search term and returns a tsquery value. The tsquery value is a query string that can
   * be used in other PostgreSQL functions such as {@link #toTsVector(Field)} to search for the
   * given search term in a field.
   *
   * @param searchTerm the search term to parse into a tsquery
   * @return the Field of type Object representing the tsquery
   */
  public static Field<Object> plaintoTsQuery(String searchTerm) {
    return function("plainto_tsquery", Object.class, val(searchTerm));
  }

  /**
   * Calls the PostgreSQL function to_tsvector on the given field. to_tsvector parses the given
   * field and returns a tsvector value. The tsvector value is a sorted array of lexemes (i.e.
   * words) that are significant in the given string. The tsvector value can then be used in other
   * PostgreSQL functions such as plainto_tsquery to search the field for the given search term.
   *
   * @param field the field to parse into a tsvector
   * @return the Field of type Object representing the tsvector
   */
  public static Field<Object> toTsVector(Field<String> field) {
    return function("to_tsvector", Object.class, field);
  }
}
