package sample.application.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Value("${FRONTEND_APP_URL}")
    private String frontendAppURL;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendAppURL)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }

    /// Define the application language and thus allow error messages
    /// to be translated. Hibernate Validation error messages
    /// are translated in the ValidationMessages_pt_BR.properties file.
    /// @return
    @Bean
    public LocaleResolver localeResolver() {
        final var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.of("pt", "BR"));
        return slr;
    }
}
