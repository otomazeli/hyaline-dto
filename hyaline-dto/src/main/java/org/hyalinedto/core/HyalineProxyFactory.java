package org.hyalinedto.core;

import org.hyalinedto.core.exception.CannotInstantiateProxyException;
import org.hyalinedto.exception.DTODefinitionException;

public interface HyalineProxyFactory {

	<T> Object create(T entity, Object dtoTemplate, boolean resetAnnotations, String proxyClassName)
			throws CannotInstantiateProxyException, DTODefinitionException;

	ClassBuilder getClassBuilder();

	void setClassBuilder(ClassBuilder builder);

	ClassRepository<String, Class<?>> getClassRepository();
	
	void setClassRepository(ClassRepository<String, Class<?>> classRepository);

}
