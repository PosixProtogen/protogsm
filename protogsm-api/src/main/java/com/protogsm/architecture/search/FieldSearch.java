package com.protogsm.architecture.search;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.protogsm.architecture.exception.InvalidSearchFieldException;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public final class FieldSearch {

	private static final Set<String> RESERVED_PARAMETERS = Set.of("page", "size", "sort");

	private FieldSearch() {
	}

	public static <E> Specification<E> toSpecification(Class<E> entityClass, Map<String, String> parameters) {
		if (parameters == null || parameters.isEmpty()) {
			return null;
		}

		List<Filter> filters = new ArrayList<>();
		for (Map.Entry<String, String> parameter : parameters.entrySet()) {
			if (RESERVED_PARAMETERS.contains(parameter.getKey())) {
				continue;
			}
			if (parameter.getValue() == null || parameter.getValue().isBlank()) {
				continue;
			}
			filters.add(toFilter(entityClass, parameter.getKey(), parameter.getValue()));
		}

		if (filters.isEmpty()) {
			return null;
		}

		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			for (Filter filter : filters) {
				predicates.add(toPredicate(builder, path(root, filter.path()), filter.value()));
			}
			return builder.and(predicates.toArray(Predicate[]::new));
		};
	}

	private static Filter toFilter(Class<?> entityClass, String parameter, String rawValue) {
		String[] path = parameter.split("\\.", -1);
		Class<?> currentType = entityClass;

		for (int index = 0; index < path.length; index++) {
			String name = path[index];
			boolean last = index == path.length - 1;
			Field field = findField(currentType, name);

			if (name.isBlank() || field == null || Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(Transient.class)) {
				throw new InvalidSearchFieldException("Unknown search field: " + parameter);
			}
			if ("password".equals(name) || isCollection(field)) {
				throw new InvalidSearchFieldException("Field cannot be used in search: " + parameter);
			}

			boolean nested = isNested(field);
			if (!last) {
				if (!nested) {
					throw new InvalidSearchFieldException("Unknown search field: " + parameter);
				}
				currentType = field.getType();
				continue;
			}
			if (nested) {
				throw new InvalidSearchFieldException("Field cannot be used in search: " + parameter);
			}

			return new Filter(path, convert(field.getType(), rawValue, parameter));
		}

		throw new InvalidSearchFieldException("Unknown search field: " + parameter);
	}

	private static boolean isCollection(Field field) {
		return Collection.class.isAssignableFrom(field.getType())
				|| field.isAnnotationPresent(OneToMany.class)
				|| field.isAnnotationPresent(ManyToMany.class);
	}

	private static boolean isNested(Field field) {
		Class<?> type = field.getType();
		return field.isAnnotationPresent(ManyToOne.class)
				|| field.isAnnotationPresent(OneToOne.class)
				|| field.isAnnotationPresent(Embedded.class)
				|| type.isAnnotationPresent(Embeddable.class)
				|| type.isAnnotationPresent(Entity.class);
	}

	private static Field findField(Class<?> type, String name) {
		Class<?> current = type;
		while (current != null && current != Object.class) {
			try {
				return current.getDeclaredField(name);
			} catch (NoSuchFieldException exception) {
				current = current.getSuperclass();
			}
		}
		return null;
	}

	private static Object convert(Class<?> type, String rawValue, String parameter) {
		try {
			if (String.class.equals(type)) {
				return rawValue;
			}
			if (type == boolean.class || type == Boolean.class) {
				if ("true".equalsIgnoreCase(rawValue)) {
					return Boolean.TRUE;
				}
				if ("false".equalsIgnoreCase(rawValue)) {
					return Boolean.FALSE;
				}
				throw new InvalidSearchFieldException("Invalid value for field " + parameter + ": " + rawValue);
			}
			if (type.isEnum()) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Object value = Enum.valueOf((Class<? extends Enum>) type, rawValue);
				return value;
			}
			if (type == byte.class || type == Byte.class) {
				return Byte.valueOf(rawValue);
			}
			if (type == short.class || type == Short.class) {
				return Short.valueOf(rawValue);
			}
			if (type == int.class || type == Integer.class) {
				return Integer.valueOf(rawValue);
			}
			if (type == long.class || type == Long.class) {
				return Long.valueOf(rawValue);
			}
			if (type == float.class || type == Float.class) {
				return Float.valueOf(rawValue);
			}
			if (type == double.class || type == Double.class) {
				return Double.valueOf(rawValue);
			}
			if (type == BigInteger.class) {
				return new BigInteger(rawValue);
			}
			if (type == BigDecimal.class) {
				return new BigDecimal(rawValue);
			}
			if (type == UUID.class) {
				return UUID.fromString(rawValue);
			}
			if (type == LocalDate.class) {
				return LocalDate.parse(rawValue);
			}
			if (type == LocalDateTime.class) {
				return LocalDateTime.parse(rawValue);
			}
			if (type == Instant.class) {
				return Instant.parse(rawValue);
			}
			if (type == Date.class) {
				return parseDate(rawValue);
			}
		} catch (InvalidSearchFieldException exception) {
			throw exception;
		} catch (RuntimeException exception) {
			throw new InvalidSearchFieldException("Invalid value for field " + parameter + ": " + rawValue);
		}

		throw new InvalidSearchFieldException("Field cannot be used in search: " + parameter);
	}

	private static Date parseDate(String rawValue) {
		try {
			return Date.from(Instant.parse(rawValue));
		} catch (DateTimeParseException exception) {
			return Date.from(LocalDate.parse(rawValue).atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
	}

	private static Path<?> path(Root<?> root, String[] parts) {
		Path<?> path = root;
		for (String part : parts) {
			path = path.get(part);
		}
		return path;
	}

	private static Predicate toPredicate(CriteriaBuilder builder, Path<?> path, Object value) {
		if (value instanceof String text) {
			return builder.like(builder.lower(path.as(String.class)), likePattern(text), '\\');
		}
		return builder.equal(path, value);
	}

	private static String likePattern(String value) {
		String escaped = value.toLowerCase(Locale.ROOT)
				.replace("\\", "\\\\")
				.replace("%", "\\%")
				.replace("_", "\\_");
		return "%" + escaped + "%";
	}

	private record Filter(String[] path, Object value) {
	}
}
