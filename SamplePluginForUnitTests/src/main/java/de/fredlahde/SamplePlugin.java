package de.fredlahde;

import software.kloud.KMSPluginSDK.IKMSPlugin;
import software.kloud.KMSPluginSDK.KMSPlugin;

@KMSPlugin(author = "Fred Lahde", version = "0.1", priority = 1)
public class SamplePlugin implements IKMSPlugin {
    @Override
    public void init() {
        new Foo().bar();
    }
}
