package org.jxtech.propertytrade.platform.common.security.ajax


import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxAwareUrlAuthFailureHandler(val myDefaultFailureUrl: String,
    val ajaxResponseErrorCode: Int): SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        if (RequestUtils.isAjaxRequest(request)) {
            response.sendError(ajaxResponseErrorCode, myDefaultFailureUrl)
        } else {
            super.onAuthenticationFailure(request, response, exception)
        }
    }
}
