package org.jxtech.propertytrade.platform.common.security.config

import org.jxtech.propertytrade.platform.common.security.ajax.AjaxAwareAccessDenyHandler
import org.jxtech.propertytrade.platform.common.security.ajax.AjaxAwareAuthenticationEntryPoint
import org.jxtech.propertytrade.platform.common.security.ajax.AjaxAwareAuthenticationSuccessHandler
import org.jxtech.propertytrade.platform.common.security.ajax.AjaxAwareHttpSessionRequestCache
import org.jxtech.propertytrade.platform.common.security.ajax.AjaxAwareInvalidSessionStrategy
import org.jxtech.propertytrade.platform.common.security.ajax.AjaxAwareUrlAuthFailureHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

//@Configuration
class WebSecurityConfig: WebSecurityConfigurerAdapter() {
    companion object {
        val LOGIN_PAGE = "html/login.html"
        val ACCESS_DENY_PAGE = "/static/accessDenied.html"
    }

    override fun configure(http: HttpSecurity) {
        http.anonymous().key("anonymous").authorities("ROLE_ANONYMOUS")
        http.exceptionHandling().accessDeniedHandler(ajaxAwareAccessDenyHandler())
            .authenticationEntryPoint(ajaxAwareAuthenticationEntryPoint())
        http.requestCache().requestCache(ajaxAwareHttpSessionRequestCache())
        http.sessionManagement().sessionFixation().migrateSession()
            .invalidSessionStrategy(ajaxAwareInvalidSessionStrategy())
        http.formLogin().loginPage(LOGIN_PAGE).successHandler(
            ajaxAwareUrlAuthSuccessHandler()
        ).failureHandler(
            ajaxAwareUrlAuthFailureHandler()
        )
        http.logout().logoutSuccessUrl(LOGIN_PAGE)
        http.csrf().disable()
    }

    @Bean
    fun ajaxAwareInvalidSessionStrategy(): AjaxAwareInvalidSessionStrategy {
        return AjaxAwareInvalidSessionStrategy(ajaxResponseErrorCode = 903,
            createNewSession = true, destinationUrl = LOGIN_PAGE)
    }

    @Bean
    fun ajaxAwareUrlAuthSuccessHandler(): AjaxAwareAuthenticationSuccessHandler {
        return AjaxAwareAuthenticationSuccessHandler()
    }

    @Bean
    fun ajaxAwareUrlAuthFailureHandler(): AjaxAwareUrlAuthFailureHandler {
        return AjaxAwareUrlAuthFailureHandler(myDefaultFailureUrl = LOGIN_PAGE, ajaxResponseErrorCode = 901)
    }

    @Bean
    fun ajaxAwareAuthenticationEntryPoint(): AjaxAwareAuthenticationEntryPoint {
        return AjaxAwareAuthenticationEntryPoint(ajaxResponseErrorCode = 901, loginFormUrl = LOGIN_PAGE)
    }

    @Bean
    fun ajaxAwareAccessDenyHandler(): AjaxAwareAccessDenyHandler {
        return AjaxAwareAccessDenyHandler(myErrorPage = "", ajaxResponseErrorCode = 902)
    }


    @Bean
    fun ajaxAwareHttpSessionRequestCache(): AjaxAwareHttpSessionRequestCache {
        return AjaxAwareHttpSessionRequestCache()
    }
}
