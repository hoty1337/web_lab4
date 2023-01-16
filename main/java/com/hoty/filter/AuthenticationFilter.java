package com.hoty.filter;

import com.hoty.database.DBService;
import com.hoty.handler.RequestHandler;
import com.hoty.handler.Validator;
import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
	@EJB
	DBService dbService;

	@Override
	public void init(FilterConfig filterConfig) {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		try {
			String[] userVal = RequestHandler.authHeaderHandler(req.getHeader("Authorization"));
			String pathInfo = req.getPathInfo();
			String[] path = pathInfo.split("/");
			String username = path[path.length - 1];

			if(userVal == null || Validator.isInvalidUser(userVal[0], userVal[1])
					|| !dbService.isCurrentUserExist(userVal[0], userVal[1])
					|| !username.equals(userVal[0])) {
				resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				filterChain.doFilter(request, response);
			}
		} catch (NullPointerException e) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	@Override
	public void destroy() {}
}
