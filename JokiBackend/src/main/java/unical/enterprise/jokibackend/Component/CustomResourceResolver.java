package unical.enterprise.jokibackend.Component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CustomResourceResolver implements ResourceResolver {

    private final Path imagesPath = Paths.get("src/main/resources/static/images");
    private final ResourceLoader resourceLoader;

    public CustomResourceResolver(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return getResource(requestPath);
    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return chain.resolveUrlPath(resourcePath, locations);
    }

    private Resource getResource(String requestPath) {
        try {
            Path filePath = imagesPath.resolve(requestPath);
            if (Files.exists(filePath)) {
                return resourceLoader.getResource("file:" + filePath.toAbsolutePath().toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

