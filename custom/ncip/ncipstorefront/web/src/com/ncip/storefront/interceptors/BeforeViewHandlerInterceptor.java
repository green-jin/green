/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.ncip.storefront.interceptors;

import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * A postHandle HandlerInterceptor that runs a number of BeforeViewHandlers before the view is rendered.
 */
public class BeforeViewHandlerInterceptor extends HandlerInterceptorAdapter
{
	private List<BeforeViewHandler> beforeViewHandlers;

	protected List<BeforeViewHandler> getBeforeViewHandlers()
	{
		return beforeViewHandlers;
	}

	@Required
	public void setBeforeViewHandlers(final List<BeforeViewHandler> beforeViewHandlers)
	{
		this.beforeViewHandlers = beforeViewHandlers;
	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception
	{
		if (modelAndView != null && !isIncludeRequest(request) && isSupportedView(modelAndView))
		{
			for (final BeforeViewHandler beforeViewHandler : getBeforeViewHandlers())
			{
				beforeViewHandler.beforeView(request, response, modelAndView);
			}
		}
	}

	protected boolean isIncludeRequest(final HttpServletRequest request)
	{
		return request.getAttribute("javax.servlet.include.request_uri") != null;
	}

	protected boolean isSupportedView(final ModelAndView modelAndView)
	{
		return modelAndView.getViewName() != null && !isRedirectView(modelAndView);
	}

	protected boolean isRedirectView(final ModelAndView modelAndView)
	{
		final String viewName = modelAndView.getViewName();
		return viewName != null && viewName.startsWith("redirect:");
	}
}
