package recipe_management.assessment.repository.jooq;

import static org.jooq.impl.DSL.*;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.springframework.stereotype.Repository;
@Repository
@Slf4j
public class CoreUtilsRepositoryJooq {
  DSLContext dsl;

  public static SortField<Object> getOrderByField(String sort, String sortDirection) {
    return sortDirection.equals("asc") ? field(name(sort)).asc() : field(name(sort)).desc();
  }

  public static List<SortField<?>> getOrderByField(
      String sort, String sortDirection, List<SortField<?>> defaultSortField) {
    List<SortField<?>> orderBy = new ArrayList<>();
    if (sort != null) {
      orderBy.add(getOrderByField(sort, sortDirection));
    }
    orderBy.addAll(defaultSortField);

    return orderBy;
  }

  public static List<SortField<?>> getOrderByField(List<SortField<?>> sortField) {
    List<SortField<?>> orderBy = new ArrayList<>();
    orderBy.addAll(sortField);
    return orderBy;
  }
}
