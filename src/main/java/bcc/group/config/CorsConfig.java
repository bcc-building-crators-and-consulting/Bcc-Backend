package bcc.group.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    // ─── Static Resources (Uploads) ─────────────────────
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = new java.io.File(uploadDir).toURI().toString();
        if (!location.endsWith("/")) {
            location += "/";
        }

        System.out.println(">>> UPLOAD DIR: " + location);
        System.out.println(">>> IMAGES LOCATION: " + location + "images/");

        // Fixed: added "/" separator before "images/members"
        java.io.File imagesDir = new java.io.File(uploadDir + "/images/members");
        System.out.println(">>> IMAGES/MEMBERS DIR EXISTS: " + imagesDir.exists());
        System.out.println(">>> IMAGES/MEMBERS DIR PATH: " + imagesDir.getAbsolutePath());

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);

        registry.addResourceHandler("/images/**")
                .addResourceLocations(location + "images/");
    }
}