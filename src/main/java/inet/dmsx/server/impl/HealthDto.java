package inet.dmsx.server.impl;

import java.util.List;

record HealthDto(
        String state,
        double cpuUsage,
        List<MemoryDto> memories
) {
}
