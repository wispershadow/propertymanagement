package org.jxtech.propertytrade.platform.common.security.ajax

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxAwareHttpSessionRequestCache: HttpSessionRequestCache() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AjaxAwareHttpSessionRequestCache::class.java)
    }

    override fun saveRequest(request: HttpServletRequest, response: HttpServletResponse) {
        if (RequestUtils.isAjaxRequest(request)) {
            logger.debug("Skip saving ajax request ")
        }
        else {
            super.saveRequest(request, response)
        }
    }
}
