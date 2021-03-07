package org.jxtech.propertytrade.platform.common.security.ajax

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandlerImpl
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxAwareAccessDenyHandler(var myErrorPage: String, val ajaxResponseErrorCode: Int):
    AccessDeniedHandlerImpl() {

    override fun setErrorPage(errorPage: String) {
        super.setErrorPage(errorPage)
        this.myErrorPage = errorPage
    }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        if (!response.isCommitted) {
            if (RequestUtils.isAjaxRequest(request)) {
                if (myErrorPage != null) {
                    response.sendError(ajaxResponseErrorCode, myErrorPage);
                }
                else {
                    response.sendError(ajaxResponseErrorCode);
                }
            }
            else {
                super.handle(request, response, accessDeniedException);
            }
        }
    }
}
