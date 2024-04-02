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
import org.glassfish.jersey.servlet.ServletProperties;

@Slf4j
public class Main {

  public static void main(String[] args) throws Exception {

    //Server server = new Server(8080);

    List<String> endpointPackages = new ArrayList<String>();
    endpointPackages.add("com.company.controller");

    Server server = createRestServer(8080, "/", "title", endpointPackages, null, null);

    //ServletContextHandler sch = new ServletContextHandler(server, "/");
    //ServletHolder jerseyServletHolder = new ServletHolder(new ServletContainer());
    //jerseyServletHolder.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS,
    //    MainApplication.class.getCanonicalName());
    //sch.addServlet(jerseyServletHolder, "/*");

    server.start();
    server.join();
  }

  private static Server createRestServer(int port, @NonNull String restBasePath,
      String swaggerTitle, @NonNull List<String> endpointPackages,
      Map<String, ServletHolder> servlets, Integer outboundBufferSize) {

    if (restBasePath == null) {
      throw new NullPointerException("restBasePath is marked non-null but is null");
    } else if (endpointPackages == null) {
      throw new NullPointerException("endpointPackages is marked non-null but is null");
    } else {
      boolean shallDisableSwagger = true; // shallDisableSwagger();
      //registerExtraMicrometerMetrics();
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
      if (!shallDisableSwagger) {
        //resourceConfig.register(ApiListingResource.class);
      }

      if (outboundBufferSize != null) {
        log.info("Setting property jersey.config.contentLength.buffer to {}", outboundBufferSize);
        resourceConfig.property("jersey.config.contentLength.buffer", outboundBufferSize);
      }

      ServletHolder mainServlet = new ServletHolder(new ServletContainer(resourceConfig));
      mainServlet.setInitOrder(1);
      context.addServlet(mainServlet, "/*");

      if (servlets != null && servlets.size() > 0) {
        servlets.forEach((key, servlet) -> {
          context.addServlet(servlet, "/" + key);
        });
      }

      /*
      FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(
          DispatcherType.REQUEST));
      cors.setInitParameter("allowedOrigins", "*");
      cors.setInitParameter("Access-Control-Allow-Origin", "*");
      cors.setInitParameter("allowedMethods", "GET,POST,HEAD");
      cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
      FilterHolder requestFilter = context.addFilter(RequestTraceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
      requestFilter.setInitParameter("allowedOrigins", "*");
      requestFilter.setInitParameter("Access-Control-Allow-Origin", "*");
      requestFilter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,HEAD,OPTIONS");
      requestFilter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin,Authorization,x-app-token,X-UserToken");
      ResourceHandler resource_handler = new ResourceHandler();
      resource_handler.setDirectoriesListed(true);
      resource_handler.setWelcomeFiles(new String[]{"index.html"});
      resource_handler.setResourceBase("/opt/aisera/www/");
      context.addServlet(new ServletHolder(new MicroMeterMetricsServlet()), "/metrics");
      StatisticsHandler stats = new StatisticsHandler();
      stats.setHandler(context);
      (new JettyStatisticsCollector(stats)).register(MicroMeterMetrics.getInstance().getRegistry().getPrometheusRegistry());
      */

      HandlerList handlers = new HandlerList();
      handlers.setHandlers(new Handler[]{context});
      jettyServer.setHandler(handlers);

      return jettyServer;
    }
  }
}
