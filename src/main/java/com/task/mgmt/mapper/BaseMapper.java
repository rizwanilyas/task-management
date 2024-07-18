package com.task.mgmt.mapper;

import java.util.List;

public interface BaseMapper<D, E> {

  E toEntity(D dto);

  List<E> toEntity(List<D> dtoList);

  D toDto(E entity);

  List<D> toDto(List<E> entityList);
}
