package arjung.family.network;

import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class FamilyNetworkApplication extends ResourceConfig {
 
    public FamilyNetworkApplication() {
        // Register resources and providers using package-scanning.
        packages("my.package");
 
        // Register my custom provider - not needed if it's in my.package.
        //register(SecurityRequestFilter.class);
        // Register an instance of LoggingFilter.
        //register(new LoggingFilter(Logger.getLogger(getApplicationName()), true));
 
        // Enable Tracing support.
        //property(ServerProperties.TRACING, "ALL");
    }
}