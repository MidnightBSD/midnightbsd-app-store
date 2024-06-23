package org.midnightbsd.appstore.model.search;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * Individual category like x11, devel, games.
 * @author Lucas Holt
 */
@ToString
@EqualsAndHashCode
public class Category implements Serializable, Comparable<Category> {
	@Serial
	private static final long serialVersionUID = 3452319081969591586L;

	@Getter
	@Setter
	private String name;

	@Override
	public int compareTo( final Category o) {
		return this.name.compareTo(o.getName());
	}
}
