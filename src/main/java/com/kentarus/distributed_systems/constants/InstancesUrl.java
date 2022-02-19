package com.kentarus.distributed_systems.constants;

import java.util.HashMap;
import java.util.Map;

public class InstancesUrl {
    public static final String firstInstance = "http://localhost:9000";
    public static final String secondInstance = "http://localhost:9001";
    public static final String thirdInstance = "http://localhost:9002";
    // public static final String fourthInstance = "http://localhost:9000";
    // public static final String fifthInstance = "http://localhost:9000";

    public static final Map<String, String> instances = new HashMap<String, String>() {
        {
            put("firstInstance", firstInstance);
            put("secondInstance", secondInstance);
            put("thirdInstance", thirdInstance);
            // put("fourthInstance", fourthInstance);
            // put("fifthInstance", fifthInstance);
        }
    };
}
