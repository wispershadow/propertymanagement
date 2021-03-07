package org.jxtech.propertytrade.platform.common.security.ajax

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxAwareAuthenticationEntryPoint(val ajaxResponseErrorCode: Int,
    loginFormUrl: String): LoginUrlAuthenticationEntryPoint(loginFormUrl) {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        if (RequestUtils.isAjaxRequest(request)) {
            response.sendError(ajaxResponseErrorCode)
        }
        else {
            super.commence(request, response, authException)
        }
    }
}
