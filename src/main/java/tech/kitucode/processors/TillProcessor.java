package tech.kitucode.processors;

import org.apache.log4j.Logger;
import tech.meliora.common.rudisha.DataSourceManager;
import tech.meliora.common.rudisha.Processor;
import tech.meliora.common.rudisha.Service;
import tech.meliora.common.rudisha.ServiceRepository;

import java.util.Map;

public class TillProcessor implements Processor {
    @Override
    public void init(Service service, Logger logger, Map<String, String> map, ServiceRepository serviceRepository, DataSourceManager dataSourceManager) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean process(Map<String, String> map) {
        return false;
    }
}
