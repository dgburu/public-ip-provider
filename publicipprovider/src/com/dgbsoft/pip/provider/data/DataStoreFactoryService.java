package com.dgbsoft.pip.provider.data;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class DataStoreFactoryService {

	private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");

	private DataStoreFactoryService() {
	}

	public static EntityManagerFactory get() {
		return emfInstance;
	}
		  
}
