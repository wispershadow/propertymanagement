package org.jxtech.propertytrade.platform.common.security.ajax

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.security.web.savedrequest.SavedRequest
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class AjaxAwareAuthenticationSuccessHandler(
    val requestCache: RequestCache = HttpSessionRequestCache()
): SavedRequestAwareAuthenticationSuccessHandler() {


    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val savedRequest: SavedRequest? = requestCache.getRequest(request, response)
        if (savedRequest == null) {
            handleAjaxAwareDefaultSuccess(request, response, authentication)
            return
        }
        val targetUrlParameter = targetUrlParameter
        if (isAlwaysUseDefaultTargetUrl || (targetUrlParameter != null &&
            StringUtils.hasText(request.getParameter(targetUrlParameter)))
        ) {
            requestCache.removeRequest(request, response)
            handleAjaxAwareDefaultSuccess(request, response, authentication)
            return
        }

        clearAuthenticationAttributes(request)

        // Use the DefaultSavedRequest URL
        val targetUrl: String = savedRequest.redirectUrl
        handleAjaxAwareSavedReqSuccess(request, response, authentication, targetUrl)
    }

    protected fun handleAjaxAwareDefaultSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse, authentication: Authentication
    ) {
        if (RequestUtils.isAjaxRequest(request)) {
            val targetUrl = determineTargetUrl(request, response)
            prepareAjaxSuccessResp(response, targetUrl)
        } else {
            super.onAuthenticationSuccess(request, response, authentication)
        }
    }

    protected fun handleAjaxAwareSavedReqSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse, authentication: Authentication, targetUrl: String
    ) {
        logger.debug("Redirecting to DefaultSavedRequest Url: $targetUrl")
        if (RequestUtils.isAjaxRequest(request)) {
            prepareAjaxSuccessResp(response, targetUrl)
        } else {
            redirectStrategy.sendRedirect(request, response, targetUrl)
        }
    }

    protected fun prepareAjaxSuccessResp(response: HttpServletResponse, targetUrl: String) {
        val jsonObj = "{\"success\":\"true\",\"targeturl\":\"$targetUrl\"}"
        response.contentType = "application/json"
        response.writer.println(jsonObj)
    }
}
