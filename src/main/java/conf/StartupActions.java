package conf;

import com.google.inject.Inject;
import ninja.lifecycle.Start;
import ninja.utils.NinjaProperties;

import javax.inject.Singleton;

@Singleton
public class StartupActions {

    private NinjaProperties ninjaProperties;

    @Inject
    public StartupActions(NinjaProperties ninjaProperties) {
        this.ninjaProperties = ninjaProperties;
    }
    
    @Start(order=100)
    public void generateDummyDataWhenInTest() {
        
        if (!ninjaProperties.isProd()) {
            
        }
        
    }

}
