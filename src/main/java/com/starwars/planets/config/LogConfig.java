package com.starwars.planets.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
@Log4j2
public class LogConfig implements Filter {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

	@Override
	public void init(FilterConfig filterConfig) {
		log.info("LogContextFilter initialized.");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {

			logRequestedURL((HttpServletRequest) request);
			chain.doFilter(request, response);
			logEndOfRequest((HttpServletResponse) response);

		} catch (IOException | ServletException | RuntimeException ex) {
			log.error(ANSI_RED_BACKGROUND + ANSI_BLACK + "Finishing request on host with an unexpected error!" + ANSI_RESET, ex);
			throw ex;
		}
	}

	private void logRequestedURL(HttpServletRequest httpServletRequest) throws IOException {
		log.info(ANSI_YELLOW_BACKGROUND + ANSI_BLACK + "Requested URL: [{}] {}" + ANSI_RESET, httpServletRequest.getMethod(),
				httpServletRequest.getRequestURL());
	}

	private void logEndOfRequest(HttpServletResponse response) throws IOException {
		log.info(ANSI_GREEN_BACKGROUND + ANSI_BLACK + "Finishing request on host with status {}." + ANSI_RESET,
				response.getStatus());
	}
}
