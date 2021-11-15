package repository;

import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Most of the code copied and pasted 😂
 * With ♥ by Mike Elkabetz
 * Date: 15/11/2021
 */

@NoArgsConstructor
public class BigtableRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(BigtableRepository.class);

    private String projectID = "xdr";
    private String instanceID = "xdr";
    private String parentDataType = null;
    private String childDataType = null;
    private BigtableTableAdminClient adminClient;
    private BigtableDataClient dataClient;

    public BigtableRepository connectEmulator(String parentDataType, String childDataType, String host, int port) throws IOException {
        this.parentDataType = parentDataType;
        this.childDataType = childDataType;
        createEmulatorSettings(host, port);
        return this;
    }

    public BigtableRepository connect(String parentDataType, String childDataType) throws IOException {
        this.parentDataType = parentDataType;
        this.childDataType = childDataType;
        createGCPSettings();
        return this;
    }

    public void createEmulatorSettings(String host, int port) throws IOException {
        BigtableTableAdminSettings adminSettings;
        adminSettings = BigtableTableAdminSettings.newBuilderForEmulator(host, port)
                .setProjectId(projectID)
                .setInstanceId(instanceID)
                .build();
        adminClient = BigtableTableAdminClient.create(adminSettings);

        BigtableDataSettings settings =
                BigtableDataSettings.newBuilderForEmulator(host, port).setProjectId(projectID).setInstanceId(instanceID).build();
        dataClient = BigtableDataClient.create(settings);
        LOGGER.info("Connected successfully to Bigtable Emulator (" + host + ":" + port + ")");
    }

    public void createGCPSettings() throws IOException {
        BigtableTableAdminSettings adminSettings;
        adminSettings = BigtableTableAdminSettings.newBuilder()
                .setProjectId(projectID)
                .setInstanceId(instanceID)
                .build();
        adminClient = BigtableTableAdminClient.create(adminSettings);

        BigtableDataSettings settings =
                BigtableDataSettings.newBuilder().setProjectId(projectID).setInstanceId(instanceID).build();
        dataClient = BigtableDataClient.create(settings);
        LOGGER.info("Connected successfully to ~ Bigtable");
    }

    public void createTableForTenant(String tenantID) {
        if (!adminClient.exists(tenantID)) {
            CreateTableRequest createTableRequest =
                    CreateTableRequest.of(tenantID).addFamily(parentDataType);
            adminClient.createTable(createTableRequest);
            LOGGER.info("Table created successfully for tenantID: " + tenantID);
        } else {
            LOGGER.info("Table already exists for tenantID: " + tenantID);
        }
    }

    public void setData(String tenantID, String key, String value) {
        try {
            createTableForTenant(tenantID);
            RowMutation rowMutation = RowMutation.create(tenantID, key).setCell(parentDataType, childDataType, value);
            dataClient.mutateRow(rowMutation);
        } catch (NotFoundException e) {
            LOGGER.error("Failed to write to non-existent table: " + e.getMessage());
        }
    }

    public String getData(String tenantID, String key) {
        try {
            Row row = dataClient.readRow(tenantID, key);
            for (RowCell cell : row.getCells()) {
                if (parentDataType.equalsIgnoreCase(cell.getFamily()) && childDataType.equalsIgnoreCase(cell.getQualifier().toStringUtf8())) {
                    return cell.getValue().toStringUtf8();
                }
            }
            LOGGER.error("Unable to find data for tenantID: " + tenantID + " for key: " + key);
            return null;
        } catch (NotFoundException e) {
            LOGGER.error("Failed to read from a non-existent table: " + e.getMessage());
            return null;
        }
    }

    public void deleteTable(String tableID) {
        try {
            adminClient.deleteTable(tableID);
            LOGGER.info("Table deleted successfully: ", tableID);
        } catch (NotFoundException e) {
            LOGGER.error("Failed to delete a non-existent table: " + e.getMessage(), e);
        }
    }
}
