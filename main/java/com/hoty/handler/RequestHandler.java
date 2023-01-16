package com.hoty.handler;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestHandler {
	public static String[] authHeaderHandler(String header) {
		if(header != null && header.toLowerCase().startsWith("basic")) {
			String base64credentials = header.substring("Basic".length()).trim();
			byte[] decodedCredentials = Base64.getDecoder().decode(base64credentials);
			String credentials = new String(decodedCredentials, StandardCharsets.UTF_8);

			return credentials.split(":", 2);
		}

		return null;
	}
}
