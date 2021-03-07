package org.jxtech.propertytrade.platform.common.security.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider
import org.springframework.security.oauth2.client.registration.ClientRegistration

//@Configuration
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
