package com.hoty.manager;

import com.hoty.database.DBService;
import com.hoty.entity.User;
import com.hoty.handler.PasswordHandler;
import com.hoty.handler.Validator;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Map;

@Singleton
@Path("/users")
public class UserManager {
	@EJB
	DBService dbService;

	@POST
	@Path("/register")
	@Consumes("multipart/form-data")
	public Response addUser(Map<String, String> params, @Context HttpServletRequest req, @Context HttpServletResponse resp) {
		Response.Status status = Response.Status.OK;
		boolean message = true;
		try {
			String username = params.get("username");
			String password = params.get("password");
			System.out.println("REGISTER: " + username + " " + password);
			if(Validator.isInvalidUser(username, password)) {
				status = Response.Status.UNAUTHORIZED;
			} else {
				String passwordSalt = PasswordHandler.generateSalt();
				String passwordHash = PasswordHandler.hashPassword(password, passwordSalt);
				User user = new User(username, passwordHash, passwordSalt);
				if(!dbService.isUserExist(username)) {
					dbService.saveUser(user);
				} else {
					message = false;
				}
			}
		} catch (NullPointerException e) {
			status = Response.Status.BAD_REQUEST;
		}

		return Response.status(status).entity(message).build();
	}

	@POST
	@Path("/login/{username}")
	public Response checkUser(@PathParam("username") String username, @Context HttpServletRequest req, @Context HttpServletResponse resp) {
		return Response.status(Response.Status.OK).entity(true).build();
	}
}
