package com.kentarus.distributed_systems.constants;

import java.util.HashMap;
import java.util.Map;

public class InstancesUrl {
    // public static final String firstInstance =
    // "http://ec2-44-202-110-91.compute-1.amazonaws.com:8080";
    public static final String firstInstance = "http://localhost:9000";

    public static final String secondInstance = "http://localhost:9001";
    public static final String thirdInstance = "http://localhost:9002";
    // public static final String fourthInstance = "http://localhost:9000";
    // public static final String fifthInstance = "http://localhost:9000";

    public static final Map<Integer, String> instances = new HashMap<Integer, String>() {
        {
            put(1, firstInstance);
            // put(2, secondInstance);
            // put(3, thirdInstance);
            // put("4", fourthInstance);
            // put("5", fifthInstance);
        }
    };
}
