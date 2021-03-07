package org.jxtech.propertytrade.platform.common.security.ajax

import javax.servlet.http.HttpServletRequest

object RequestUtils {
    private const val AJAX_REQ_HEADERNAME = "X-Requested-With"
    private const val AJAX_REQ_HEADERVALUE = "XMLHttpRequest"

    fun isAjaxRequest(request: HttpServletRequest): Boolean {
        val ajaxHeader = request.getHeader(AJAX_REQ_HEADERNAME)
        return AJAX_REQ_HEADERVALUE == ajaxHeader
    }
}
