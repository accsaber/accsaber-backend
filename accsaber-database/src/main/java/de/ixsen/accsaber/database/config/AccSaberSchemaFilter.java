package de.ixsen.accsaber.database.config;

import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.spi.SchemaFilter;

public class AccSaberSchemaFilter implements SchemaFilter {
    public static final AccSaberSchemaFilter INSTANCE = new AccSaberSchemaFilter();

    @Override
    public boolean includeNamespace(Namespace namespace) {
        return true;
    }

    @Override
    public boolean includeTable(Table table) {
        return !table.getName().contains("acc_saber_score") && !table.getName().contains("acc_saber_player");
    }

    @Override
    public boolean includeSequence(Sequence sequence) {
        return true;
    }
}
