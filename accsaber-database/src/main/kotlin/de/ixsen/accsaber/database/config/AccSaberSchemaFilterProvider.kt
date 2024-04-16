package de.ixsen.accsaber.database.config

import org.hibernate.tool.schema.internal.DefaultSchemaFilter
import org.hibernate.tool.schema.spi.SchemaFilter
import org.hibernate.tool.schema.spi.SchemaFilterProvider

class AccSaberSchemaFilterProvider : SchemaFilterProvider {
    override fun getCreateFilter(): SchemaFilter {
        return DefaultSchemaFilter.INSTANCE
    }

    override fun getDropFilter(): SchemaFilter {
        return DefaultSchemaFilter.INSTANCE
    }

    override fun getMigrateFilter(): SchemaFilter {
        return DefaultSchemaFilter.INSTANCE
    }

    override fun getValidateFilter(): SchemaFilter {
        return AccSaberSchemaFilter.Companion.INSTANCE
    }
}
