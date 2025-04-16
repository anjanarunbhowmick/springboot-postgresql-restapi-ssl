package com.springboot.pg.ssl.postgresql.helper;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmployeeUtils {
	
	public static <T, K> Set<T> deduplicateByField(Collection<T> collection, Function<T, K> keyExtractor) {
	    return new HashSet<>(
	        new LinkedHashMap<K, T>(
	            collection.stream().collect(Collectors.toMap(
	                keyExtractor,
	                Function.identity(),
	                (existing, replacement) -> existing
	            ))
	        ).values()
	    );
	}
}
