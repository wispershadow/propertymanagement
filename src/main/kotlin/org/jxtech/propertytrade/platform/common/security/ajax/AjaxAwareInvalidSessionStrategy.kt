package org.jxtech.propertytrade.platform.common.security.ajax

import org.slf4j.LoggerFactory
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.session.InvalidSessionStrategy
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxAwareInvalidSessionStrategy(val ajaxResponseErrorCode: Int,
    val createNewSession: Boolean, val destinationUrl: String): InvalidSessionStrategy {
    companion object {
        private val logger = LoggerFactory.getLogger(AjaxAwareInvalidSessionStrategy::class.java)
    }

    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()

    override fun onInvalidSessionDetected(request: HttpServletRequest, response: HttpServletResponse) {
        logger.debug("Starting new session (if required) and redirecting to '$destinationUrl'");
        if (createNewSession) {
            request.session;
        }
        if (RequestUtils.isAjaxRequest(request)) {
            response.sendError(ajaxResponseErrorCode, destinationUrl);
        }
        else {
            redirectStrategy.sendRedirect(request, response, destinationUrl);
        }
    }
}
