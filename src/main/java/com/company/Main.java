package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

@Slf4j
public class Main {

  public static void main(String[] args) throws Exception {

    List<String> endpointPackages = new ArrayList<String>();
    endpointPackages.add("com.company.controller");
    endpointPackages.add("com.company.api");

    Server server = createRestServer(8080, "/", "OpenAPIGenerator02", endpointPackages, null);

    server.start();
    server.join();
  }

  private static Server createRestServer(int port, @NonNull String restBasePath,
      String swaggerTitle, @NonNull List<String> endpointPackages,
      Map<String, ServletHolder> servlets) {

    if (restBasePath == null) {
      throw new NullPointerException("restBasePath is marked non-null but is null");
    } else if (endpointPackages == null) {
      throw new NullPointerException("endpointPackages is marked non-null but is null");
    } else {
      Server jettyServer = new Server(port);
      ServletContextHandler context = new ServletContextHandler(jettyServer, restBasePath, 1);
      ResourceConfig resourceConfig = (new ResourceConfig())
          .packages((String[]) endpointPackages.toArray(new String[0]))
          .register(JacksonFeature.class)
          //.register(FormDataContentDisposition.class)
          //.register(FormDataParam.class)
          //.register(MultiPartFeature.class)
          //.register(SwaggerSerializers.class)
          //.register(new MetricsApplicationEventListener(MicroMeterMetrics.getInstance().getRegistry(), new DefaultJerseyTagsProvider(), "http.server.requests", true))
          ;

      ServletHolder mainServlet = new ServletHolder(new ServletContainer(resourceConfig));
      mainServlet.setInitOrder(1);
      context.addServlet(mainServlet, "/*");

      if (servlets != null && servlets.size() > 0) {
        servlets.forEach((key, servlet) -> {
          context.addServlet(servlet, "/" + key);
        });
      }

      HandlerList handlers = new HandlerList();
      handlers.setHandlers(new Handler[]{context});
      jettyServer.setHandler(handlers);

      return jettyServer;
    }
  }
}
