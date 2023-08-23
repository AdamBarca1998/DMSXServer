package inet.dmsx.server.impl;

import com.sun.management.OperatingSystemMXBean;
import inet.dmsx.server.DMSXServer;
import inet.dmsx.server.properties.DMSXServerProperties;
import inet.dmsx.server.properties.PropertiesParserSingleton;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.logging.Logger;

public final class HealthServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(HealthServerHandler.class.getName());
    private final DMSXServer server;

    public HealthServerHandler(DMSXServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            LOGGER.info("START Health server");
            var clazz = server.getState().getClass().getName();
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

            var obj = new HealthDto(
                    clazz.substring(clazz.lastIndexOf('.') + 1),
                    osBean.getProcessCpuLoad(),
                    getMemoryDtoList()
            );

            RoutingHandlers.okJsonHandler(exchange, obj);
            LOGGER.info("END Health server");
        } catch (Exception e) {
            RoutingHandlers.exceptionHandler(exchange, e);
        }
    }

    private List<MemoryDto> getMemoryDtoList() {
        var tmpFile = new File(PropertiesParserSingleton.getInstance().getPropertyValue("tmp" + DMSXServerProperties.DIR_PATH.getText()));
        var emailFile = new File(PropertiesParserSingleton.getInstance().getPropertyValue("email" + DMSXServerProperties.DIR_PATH.getText()));
        var mntFile = new File(PropertiesParserSingleton.getInstance().getPropertyValue("mnt" + DMSXServerProperties.DIR_PATH.getText()));

        return List.of(
                new MemoryDto(
                        "heap",
                        Runtime.getRuntime().totalMemory(),
                        Runtime.getRuntime().freeMemory(),
                        Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
                ),
                new MemoryDto(
                        tmpFile.getAbsolutePath(),
                        tmpFile.getTotalSpace(),
                        tmpFile.getUsableSpace(),
                        tmpFile.getTotalSpace() - tmpFile.getUsableSpace()
                ),
                new MemoryDto(
                        emailFile.getAbsolutePath(),
                        emailFile.getTotalSpace(),
                        emailFile.getUsableSpace(),
                        emailFile.getTotalSpace() - emailFile.getUsableSpace()
                ),
                new MemoryDto(
                        mntFile.getAbsolutePath(),
                        mntFile.getTotalSpace(),
                        mntFile.getUsableSpace(),
                        mntFile.getTotalSpace() - mntFile.getUsableSpace()
                )
        );
    }
}