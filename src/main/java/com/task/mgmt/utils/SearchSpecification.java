package com.task.mgmt.utils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SearchSpecification<T> {

  private static final List<String> IGNORED_FIELDS = Arrays.asList("page", "size", "sort");

  public Specification<T> getSpecification(Map<String, String> criteria) {
    List<String> fields =
        criteria.keySet().stream().filter(k -> !IGNORED_FIELDS.contains(k)).toList();

    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      for (String f : fields) {
        String value = criteria.get(f);
        if (value != null) {
          Predicate cb =
              criteriaBuilder.like(
                  criteriaBuilder.lower(root.get(f)), "%" + value.toLowerCase() + "%");
          predicates.add(cb);
        }
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
