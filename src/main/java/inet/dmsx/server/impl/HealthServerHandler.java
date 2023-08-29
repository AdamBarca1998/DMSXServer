package inet.dmsx.server.impl;

import com.sun.management.OperatingSystemMXBean;
import inet.dmsx.server.properties.DMSXServerProperties;
import inet.dmsx.server.properties.PropertiesParserSingleton;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

final class HealthServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(HealthServerHandler.class.getName());
    private static final PropertiesParserSingleton PROPERTIES_PARSER = PropertiesParserSingleton.getInstance();
    private final DMSXServer server;

    HealthServerHandler(DMSXServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            LOGGER.info("START Health server");

            var healthDto = new HealthDto(
                    server.getState().getClass().getSimpleName(),
                    ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getProcessCpuLoad(),
                    getMemoryDtoList()
            );

            RoutingHandlers.okJsonHandler(exchange, healthDto);
            LOGGER.info("END Health server");
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }

    private List<MemoryDto> getMemoryDtoList() {
        List<MemoryDto> list = new ArrayList<>();
        list.add(new MemoryDto(
                "heap",
                Runtime.getRuntime().totalMemory(),
                Runtime.getRuntime().freeMemory(),
                Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        ));

        PROPERTIES_PARSER.getStorageIds().forEach(storageId -> {
            var file = new File(PropertiesParserSingleton.getInstance().getPropertyValue(storageId + DMSXServerProperties.DIR_PATH.getText()));

            list.add(new MemoryDto(
                    file.getAbsolutePath(),
                    file.getTotalSpace(),
                    file.getUsableSpace(),
                    file.getTotalSpace() - file.getUsableSpace()
            ));
        });

        return list;
    }
}