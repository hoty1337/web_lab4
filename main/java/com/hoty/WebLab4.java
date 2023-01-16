package com.hoty;

import com.hoty.database.DBService;
import com.hoty.manager.PointManager;
import com.hoty.manager.UserManager;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class WebLab4 extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		return new HashSet<>(Arrays.asList(PointManager.class, UserManager.class, DBService.class));
	}
}
