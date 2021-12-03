package tech.kitucode;

import javassist.tools.web.Webserver;
import org.apache.log4j.Logger;
import tech.kitucode.constants.AppConstants;
import tech.kitucode.web.rest.HelloResource;
import tech.meliora.common.rudisha.Helper;
import tech.meliora.common.rudisha.Service;
import tech.meliora.common.rudisha.ServiceManager;
import tech.meliora.common.rudisha.errors.HelperNotFoundException;
import tech.meliora.common.rudisha.errors.ServiceNotFoundException;
import tech.meliora.common.rudisha.util.FileUtil;
import tech.meliora.common.rudisha.util.ProfileUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    private final static Logger logger = Logger.getLogger("root");

    private String appName = "mpesa";

    private String profileId = "dev";

    private final FileUtil fileUtil;

    private final String appConfigurationStr;
    private final String dataSourceConfigurationStr;

    private final ServiceManager serviceManager;
    private static ServiceManager finalServiceManager;

    public App() throws Exception {
        this.profileId = ProfileUtil.getInstance().getProfileId().toUpperCase();
        logger.info("system|profile:" + profileId + "|starting  "+this.appName);

        fileUtil = FileUtil.getInstance();
        logger.info("system|profile:" +profileId +"|initialized file util.");

        appConfigurationStr = loadAppConfigurations();
        logger.info("system|profile:" +profileId + "|app-configs:" + appConfigurationStr+"|starting "+this.appName);

        dataSourceConfigurationStr = loadDataSourceConfigurations();
        logger.info("system|profile:" +profileId + "|datasource-configs:" + dataSourceConfigurationStr+"|starting "+this.appName);

        serviceManager = new ServiceManager(appConfigurationStr, dataSourceConfigurationStr);
        logger.info("system|profile:" +profileId +"|initialized the service manager.");
        serviceManager.addAcceptorProviderClassName(HelloResource.class.getCanonicalName());

        logger.info("system|profile:" +profileId +"|starting the service manager. Please wait..");
        serviceManager.init();
        logger.info("system|profile:" +profileId +"|started the service manager.");

        finalServiceManager = serviceManager;

        // shut down hook
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    private String loadAppConfigurations() throws Exception {
        String configStr;

        String appConfigProperty = System.getProperty(AppConstants.CONFIG_APP_FILE_NAME_PROPERTY);

        if(appConfigProperty!=null){
            logger.info("system|callbackConfigProperty: " + appConfigProperty + "|loading config from -D" + AppConstants.CONFIG_APP_FILE_NAME_PROPERTY);
            configStr = fileUtil.loadFile(appConfigProperty);
        }else{
            logger.info("current profile "+ProfileUtil.getInstance().getProfileId().toUpperCase());
            logger.info("system|callbackConfigProperty: "+appConfigProperty+"|loading configs from profile configs inside jar");
            configStr = fileUtil.loadConfigResourceFile(AppConstants.CONFIG_APP_DEFAULT_FILE_NAME);
        }

        return configStr;
    }

    private String loadDataSourceConfigurations() throws Exception {
        String configStr;

        String appConfigProperty = System.getProperty(AppConstants.CONFIG_DATASOURCE_FILE_NAME_PROPERTY);
        if (appConfigProperty != null) {
            logger.info("system|callbackConfigProperty: " + appConfigProperty + "|loading config from -D" + AppConstants.CONFIG_DATASOURCE_FILE_NAME_PROPERTY);
            configStr = fileUtil.loadFile(appConfigProperty);
        } else {
            logger.info("system|callbackConfigProperty: " + appConfigProperty + "|loading configs from from profile configs inside jar");
            configStr = fileUtil.loadConfigResourceFile(AppConstants.CONFIG_DATASOURCE_DEFAULT_FILE_NAME);
        }

        return configStr;

    }

    public static void main(String[] args) {
        try {
            App app = new App();
        } catch (Exception e) {
            logger.error("system|fatal error",e);
            e.printStackTrace();
        }
    }

    class ShutdownHook extends Thread {

        @Override
        public void run() {
            logger.info("system|profile:" + profileId + "|about to shutdown  " + appName + ".");
            try {
                logger.info("system|profile:" + profileId + "|about to stop  " + appName + "");
                serviceManager.close();
                logger.info("system|profile:" + profileId + "|stopped the  " + appName + "");
            } catch (Exception ex) {
                logger.error("system|profile:" + profileId + "|error stopping  " + appName + "", ex);
            }

        }
    }

    public static Helper getHelper(String name) throws HelperNotFoundException {
        return finalServiceManager.getHelper(name);
    }

    public static Service getService(String name) throws ServiceNotFoundException {
        return finalServiceManager.getService(name);
    }

    public static Service getService(String remoteModule, String name) throws ServiceNotFoundException {
        return finalServiceManager.getService(remoteModule, name);
    }


}
