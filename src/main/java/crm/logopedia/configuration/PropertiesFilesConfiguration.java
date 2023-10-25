package crm.logopedia.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(value = "classpath:messages.properties", encoding = "UTF-8"),
        @PropertySource(value = "classpath:params.properties", encoding = "UTF-8"),
        @PropertySource(value = "classpath:titles.properties", encoding = "UTF-8")
})
public class PropertiesFilesConfiguration {
}
