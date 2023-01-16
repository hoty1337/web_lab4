package com.hoty.database;

import com.hoty.entity.Point;
import com.hoty.entity.User;
import com.hoty.handler.PasswordHandler;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.util.List;

@Singleton
public class DBService {
	private final EntityManager entManager;

	DBService() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "studs" );
		entManager = emf.createEntityManager();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveUser(User user) {
		entManager.getTransaction().begin();
		entManager.persist(user);
		entManager.getTransaction().commit();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePoint(Point point) {
		entManager.getTransaction().begin();
		entManager.persist(point);
		entManager.getTransaction().commit();
	}

	public boolean isUserExist(String username) {
		User user;
		try {
			user = (User) entManager.createQuery("SELECT u FROM User u WHERE u.username LIKE :username")
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return false;
		}
		return user != null;
	}

	public boolean isCurrentUserExist(String username, String password) {
		User user;
		try {
			user = (User) entManager.createQuery("SELECT u FROM User u WHERE u.username LIKE :username")
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return false;
		}
		return user != null && PasswordHandler.checkPassword(user, password);
	}

	public List<Point> getPoints(String username) {
		return entManager.createQuery("SELECT p FROM Point p WHERE p.username LIKE :username", Point.class)
				.setParameter("username", username).getResultList();
	}
}
