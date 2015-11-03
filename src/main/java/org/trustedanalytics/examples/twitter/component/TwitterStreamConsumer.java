package org.trustedanalytics.examples.twitter.component;

import com.google.common.collect.Lists;
import com.twitter.hbc.core.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;


@Component
public class TwitterStreamConsumer {
    @Value("${twitter.terms}")
    String[] terms;
    @Resource
    BlockingQueue<String> blockingQueue;
    @Autowired
    Client client;

    @Override
    public String toString() {
        return "TwitterStreamConsumer [terms=" + terms + "  rq=" + blockingQueue.remainingCapacity() + "]";
    }

    @PostConstruct
    public void initialize() throws ParseException, InterruptedException {
        System.out.println("---------- twitter stream example");
        System.out.println(this.toString());
        System.out.println(Lists.newArrayList(terms));

        this.run();
    }

    public void run() throws InterruptedException, ParseException {
        System.out.println("run");

        // Establish a connection
        client.connect();
        JSONParser parser = new JSONParser();
        // Do whatever needs to be done with messages
        for (int msgRead = 0; msgRead < 1000; msgRead++) {
            System.out.println("read");
            String msg = blockingQueue.take();
            System.out.println(msg);
            JSONObject jsonObject = (JSONObject) parser.parse(msg);
            Object geo = jsonObject.get("geo");
            Object place = jsonObject.get("place");
            if (geo != null || place != null) {
                System.out.println("-------------!!!!");
                //System.out.println(msg);
                System.out.println(geo);
                System.out.println(place);
            }
        }

        client.stop();
    }

}
