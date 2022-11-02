package eu.midnightdust.cullleaves;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;

public class CullLeavesClient {

    public static void onInitializeClient() {
        CullLeavesConfig.init("cullleaves", CullLeavesConfig.class);
    }
}
