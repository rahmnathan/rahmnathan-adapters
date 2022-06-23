package com.github.rahmnathan.omdb.config;

import com.github.rahmnathan.omdb.data.MediaType;
import com.github.rahmnathan.omdb.processor.MediaBuilder;
import com.github.rahmnathan.omdb.processor.PosterUriExtractor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.http.common.HttpMethods;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class OmdbCamelRoutes {
    private final Logger logger = LoggerFactory.getLogger(OmdbCamelRoutes.class.getName());
    public static final String OMDB_MOVIE_ROUTE = "direct:omdbMovie";
    public static final String OMDB_SEASON_ROUTE = "direct:omdbSeason";
    public static final String OMDB_EPISODE_ROUTE = "direct:omdbEpisode";
    public static final String OMDB_BASE_ROUTE = "direct:omdbBase";

    private static final String OMDB_URL = "https://www.omdbapi.com";
    public static final String MEDIA_TITLE_PROPERTY = "movieTitle";
    public static final String OMDB_DATA_PROPERTY = "omdbData";
    public static final String NUMBER_PROPERTY = "numberProperty";
    private final CamelContext camelContext;

    public OmdbCamelRoutes(CamelContext camelContext){
        this.camelContext = camelContext;
    }

    public void initialize(){
        try {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    onException(HttpOperationFailedException.class)
                            .to("micrometer:timer:omdb-poster-timer?action=stop")
                            .to("micrometer:timer:omdb-data-timer?action=stop")
                            .onWhen(exchange -> exchange.getException(HttpOperationFailedException.class).getStatusCode() >= 500)
                            .useExponentialBackOff()
                            .redeliveryDelay(500)
                            .maximumRedeliveries(3)
                            .end();

                    from(OMDB_SEASON_ROUTE)
                            .to(OMDB_BASE_ROUTE)
                            .process(new MediaBuilder(MediaType.SEASON))
                            .end();

                    from(OMDB_EPISODE_ROUTE)
                            .to(OMDB_BASE_ROUTE)
                            .process(new MediaBuilder(MediaType.EPISODE))
                            .end();

                    from(OMDB_MOVIE_ROUTE)
                            .to(OMDB_BASE_ROUTE)
                            .process(new MediaBuilder(MediaType.MOVIE))
                            .end();

                    from(OMDB_BASE_ROUTE)
                            .to("micrometer:timer:omdb-data-timer?action=start")
                            .to(OMDB_URL)
                            .to("micrometer:timer:omdb-data-timer?action=stop")
                            .removeHeader(Exchange.HTTP_QUERY)
                            .process(new PosterUriExtractor())
                            .choice()
                                .when(exchange -> {
                                        String posterUri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
                                        return posterUri != null && !posterUri.equalsIgnoreCase("n/a");
                                    })
                                    .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                                    .to("micrometer:timer:omdb-poster-timer?action=start")
                                    .doTry()
                                        .to(OMDB_URL)
                                    .doCatch(HttpOperationFailedException.class)
                                        .log(exceptionMessage().toString())
                                    .endDoTry()
                                    .to("micrometer:timer:omdb-poster-timer?action=stop")
                            .end()
                            .end();

                    // kamel -n camel run HelloWorld.java --build-property quarkus.datasource.camel.db-kind=postgresql --config secret:localmovies-db -d mvn:io.quarkus:quarkus-jdbc-postgresql -d mvn:org.apache.camel:camel-solr:3.17.0

                    from("timer:java?period=10000")
                            .routeId("java")
                            .to("sql:select * from media")
                            .setHeader("SolrOperation", constant("INSERT"))
                            .process(exchange -> exchange.getMessage().setHeader("LIST_LENGTH", exchange.getMessage().getBody(List.class).size()))
                            .process(exchange -> exchange.setProperty("QUERY_RESULT", exchange.getMessage().getBody(List.class)))
                            .loop(header("LIST_LENGTH"))
                            .process(exchange -> exchange.getMessage().setBody(buildDocument(exchange.getProperty("QUERY_RESULT", List.class), exchange.getProperty("CamelLoopIndex", Integer.class))))
                            .to("solrCloud://solr.solr.svc.cluster.local:8983/solr?username=admin&password=redacted&collection=movies&zkHost=solr-zookeeper.solr.svc.cluster.local:2181&zkChroot=/solr")
                            //        .to("hashicorp-vault:secretsEngine?host=vault.nathanrahm.com&operation=getSecret&secretPath=localmovies/localmovie-media-manager&token=s.IG7Wm6lx4DE40eOEMZyRM482")
                            .to("log:info")
                            .end()
                            .setHeader("SolrOperation", constant("COMMIT"))
                            .to("solrCloud://solr.solr.svc.cluster.local:8983/solr?username=admin&password=redacted&collection=movies&zkHost=solr-zookeeper.solr.svc.cluster.local:2181&zkChroot=/solr")
                            .end();

                }
            });
        } catch (Exception e){
            logger.error("Failure adding routes to Camel context", e);
        }
    }

    public SolrInputDocument buildDocument(List<Map<String, Object>> queryResult, Integer index) {
        SolrInputDocument inputDocument = new SolrInputDocument();
        Map<String, Object> entry = queryResult.get(index);
        entry.entrySet().stream().forEach(thisOne -> inputDocument.setField(thisOne.getKey(), String.valueOf(thisOne.getValue())));
        return inputDocument;
    }

}
