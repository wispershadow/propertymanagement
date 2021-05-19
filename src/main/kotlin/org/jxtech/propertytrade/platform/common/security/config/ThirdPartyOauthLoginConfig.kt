package org.jxtech.propertytrade.platform.common.security.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider
import org.springframework.security.oauth2.client.registration.ClientRegistration

//@Configuration
//https://spring.io/blog/2018/03/06/using-spring-security-5-to-integrate-with-oauth-2-secured-services-such-as-facebook-and-github
//https://stackoverflow.com/questions/56293978/successful-spring-oauth2-login-with-empty-authorities
class ThirdPartyOauthLoginConfig: WebSecurityConfigurerAdapter() {

    //ClientRegistrationRepository:
    //OAuth2AuthorizedClientService
    override fun configure(http: HttpSecurity) {
        http.oauth2Login {oauth2LoginCustomizer ->

        }

    }

    fun getRegistration(client: String): ClientRegistration {
        val clientId = ""
        val clientSecret = ""
        return CommonOAuth2Provider.GOOGLE.getBuilder(client)
            .clientId(clientId).clientSecret(clientSecret).build()
    }
}
