package wm;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.activemq.ActiveMQConnection;

import javax.jms.ConnectionFactory;

public class HelloApp {
    public static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    final static Logger logger = Logger.getLogger(HelloApp.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        logger.info("Hello log!");

        CamelContext camelContext = new DefaultCamelContext();

        // ProducerTemplate template = camelContext.createProducerTemplate();

        // ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
// Note we can explicit name the component
        //   camelContext.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

       /* for (int i = 0; i < 10; i++) {
            template.sendBody("test-jms:queue:test.queue", "Test Message: " + i);
        }
*/

        camelContext.addRoutes(new RouteBuilder() {
            public void configure() {
                from("file:/tmp/in?noop=true").to("file:/tmp/out");
/*
                        from("direct:cafe")
                                .split().method("orderSplitter").to("direct:drink");

                        from("direct:drink").recipientList().method("drinkRouter");

                        from("seda:coldDrinks?concurrentConsumers=2").to("bean:barista?method=prepareColdDrink").to("direct:deliveries");
                        from("seda:hotDrinks?concurrentConsumers=3").to("bean:barista?method=prepareHotDrink").to("direct:deliveries");

                        from("direct:deliveries")
                                .aggregate(new CafeAggregationStrategy()).method("waiter", "checkOrder").completionTimeout(5 * 1000L)
                                .to("bean:waiter?method=prepareDelivery")
                                .to("bean:waiter?method=deliverCafes");
                                */
            }
        });
        // Start the Camel route
        camelContext.start();

        HelloService helloService = context.getBean(HelloService.class);
        System.out.println(helloService.sayHello());

        // Wait five minutes, then stop
        Thread.sleep(60 * 5 * 1000);
        camelContext.stop ();


    }
}
