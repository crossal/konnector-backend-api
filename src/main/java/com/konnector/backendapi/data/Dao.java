package com.konnector.backendapi.data;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
	// TODO: cleanup
	Optional<T> get(long id);
//	List<T> getAll();
	void save(T t);
//	void update(T t, String[] params);
	void update(T t);
	void delete(T t);
}
