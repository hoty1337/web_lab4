package com.hoty.manager;

import com.hoty.database.DBService;
import com.hoty.entity.Point;
import com.hoty.handler.RequestHandler;
import com.hoty.handler.Validator;
import com.hoty.mbeans.PointsCounter;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
@Path("/points/{username}")
public class PointManager {
	@EJB
	PointsCounter pointsCounter;
	@EJB
	DBService dbService;

	@POST
	@Consumes("multipart/form-data")
	public Response addPoint(@PathParam("username") String username,
	                         Map<String, Double> params,
	                         @Context HttpServletRequest req,
	                         @Context HttpServletResponse resp) {
		Response.Status status = Response.Status.OK;
		List<Point> message = new ArrayList<>();
		try {
			double x = params.get("x");
			double y = params.get("y");
			double r = params.get("r");

			String[] userVal = RequestHandler.authHeaderHandler(req.getHeader("Authorization"));
			if(!Validator.isValidPoint(x, y, r)) throw new NumberFormatException();
			Point point = new Point(x, y, r, userVal[0]);
			point.setResult(Validator.isHit(x, y, r));

			List<Point> points = dbService.getPoints(userVal[0]);
			dbService.savePoint(point);
			points.add(point);
			message = points;

			pointsCounter.changeCounters(point.getX(), point.getY(), point.getR(), point.getResult());
		} catch (NumberFormatException | NullPointerException e) {
			status = Response.Status.BAD_REQUEST;
		} catch (Exception e) {
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}

		return Response.status(status).entity(message).build();
	}

	@GET
	public Response getPoints(@PathParam("username") String username, @Context HttpServletRequest req, @Context HttpServletResponse resp) {
		String[] userVal = RequestHandler.authHeaderHandler(req.getHeader("Authorization"));
		List<Point> message = dbService.getPoints(userVal[0]);

		return Response.status(Response.Status.OK).entity(message).build();
	}
}
