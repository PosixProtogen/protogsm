package com.protogsm.architecture.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.protogsm.architecture.exception.InvalidSearchFieldException;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

class FieldSearchTest {

	@Test
	void ignoresPaginationParametersAndBlankValues() {
		assertThat(FieldSearch.toSpecification(SearchRoot.class, Map.of(
				"page", "0",
				"size", "20",
				"sort", "localName,asc",
				"localName", "   "))).isNull();
	}

	@Test
	void acceptsSimpleFieldsAndNestedPaths() {
		assertThat(FieldSearch.toSpecification(SearchRoot.class, Map.of(
				"localName", "valheim",
				"status", "OPEN",
				"quantity", "3",
				"child.name", "admin",
				"detail.note", "local"))).isNotNull();
	}

	@Test
	void rejectsUnknownPasswordCollectionAndInvalidValues() {
		assertThatThrownBy(() -> FieldSearch.toSpecification(SearchRoot.class, Map.of("missing", "x")))
				.isInstanceOf(InvalidSearchFieldException.class);
		assertThatThrownBy(() -> FieldSearch.toSpecification(SearchRoot.class, Map.of("password", "secret")))
				.isInstanceOf(InvalidSearchFieldException.class);
		assertThatThrownBy(() -> FieldSearch.toSpecification(SearchRoot.class, Map.of("children.name", "admin")))
				.isInstanceOf(InvalidSearchFieldException.class);
		assertThatThrownBy(() -> FieldSearch.toSpecification(SearchRoot.class, Map.of("child", "x")))
				.isInstanceOf(InvalidSearchFieldException.class);
		assertThatThrownBy(() -> FieldSearch.toSpecification(SearchRoot.class, Map.of("status", "NOPE")))
				.isInstanceOf(InvalidSearchFieldException.class);
		assertThatThrownBy(() -> FieldSearch.toSpecification(SearchRoot.class, Map.of("active", "yes")))
				.isInstanceOf(InvalidSearchFieldException.class);
		assertThatThrownBy(() -> FieldSearch.toSpecification(SearchRoot.class, Map.of("quantity", "abc")))
				.isInstanceOf(InvalidSearchFieldException.class);
	}

	@Entity
	private static class SearchRoot {

		private String localName;

		private Integer quantity;

		private boolean active;

		private SearchStatus status;

		private String password;

		@ManyToOne
		private SearchChild child;

		@OneToMany
		private Set<SearchChild> children;

		@Embedded
		private SearchDetail detail;
	}

	@Entity
	private static class SearchChild {

		private String name;
	}

	@Embeddable
	private static class SearchDetail {

		private String note;
	}

	private enum SearchStatus {
		OPEN,
		CLOSED
	}
}
