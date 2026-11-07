package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

// Builds daily summaries from transaction log files.
public final class ReportGenerator {
    private ReportGenerator() {
    }

    public static Path generate(Path logsDirectory, Path reportFile) throws IOException {
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        List<String> report = new ArrayList<>();

        if (Files.exists(logsDirectory)) {
            try (var files = Files.newDirectoryStream(logsDirectory)) {
                for (Path file : files) {
                    if (!Files.isRegularFile(file)) {
                        continue;
                    }
                    BasicFileAttributes attributes =
                            Files.readAttributes(file, BasicFileAttributes.class);
                    report.add(file.getFileName() + " | size=" + attributes.size()
                            + " bytes | modified=" + attributes.lastModifiedTime());
                    try (BufferedReader reader = Files.newBufferedReader(file)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.trim().split("\\s+");
                            if (parts.length >= 2) {
                                long amount = Long.parseLong(parts[1]);
                                if ("DEPOSIT".equals(parts[0])) {
                                    totalDeposits += amount;
                                } else if ("WITHDRAW".equals(parts[0])) {
                                    totalWithdrawals += amount;
                                }
                            }
                        }
                    }
                }
            }
        }

        report.add("Total deposits: " + totalDeposits);
        report.add("Total withdrawals: " + totalWithdrawals);
        if (reportFile.getParent() != null) {
            Files.createDirectories(reportFile.getParent());
        }
        Files.write(reportFile, report);
        return reportFile;
    }
}
