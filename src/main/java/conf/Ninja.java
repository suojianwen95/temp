package conf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import freemarker.template.TemplateModelException;
import ninja.NinjaDefault;
import ninja.template.TemplateEngineFreemarker;
import ninja.utils.NinjaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yun.models.Level;
import yun.utils.CookieUtils;

@Singleton
public class Ninja extends NinjaDefault {

    static final Logger logger = LoggerFactory.getLogger(Ninja.class);

    @Inject
    NinjaProperties ninjaProperties;

    @Inject
    TemplateEngineFreemarker templateEngineFreemarker;
    @Inject
    CookieUtils cookieUtils;

    @Override
    public void onFrameworkStart() {
        super.onFrameworkStart();
        try {
            templateEngineFreemarker.getConfiguration().setSharedVariable("isDev", ninjaProperties.isDev());
            templateEngineFreemarker.getConfiguration().setSharedVariable("isProd", ninjaProperties.isProd());

            templateEngineFreemarker.getConfiguration().setSharedVariable("adminUid",
                    cookieUtils.encryptKey(Level.UID_ADMIN));
            templateEngineFreemarker.getConfiguration().setSharedVariable("staffUid",
                    cookieUtils.encryptKey(Level.UID_STAFF));
            templateEngineFreemarker.getConfiguration().setSharedVariable("customerUid",
                    cookieUtils.encryptKey(Level.UID_CUSTOMER));
            templateEngineFreemarker.getConfiguration().setSharedVariable("levels",
                    cookieUtils.encryptKey("levels"));
            templateEngineFreemarker.getConfiguration().setSharedVariable("username",
                    cookieUtils.encryptKey("username"));
        } catch (TemplateModelException e) {
            logger.error("{} \n {}",e.getMessage(), e.getStackTrace());
        }
    }
}
