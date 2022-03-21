package de.ixsen.accsaber.database.config

import org.hibernate.boot.model.relational.Namespace
import org.hibernate.boot.model.relational.Sequence
import org.hibernate.mapping.Table
import org.hibernate.tool.schema.spi.SchemaFilter

class AccSaberSchemaFilter : SchemaFilter {
    override fun includeNamespace(namespace: Namespace): Boolean {
        return true
    }

    override fun includeTable(table: Table): Boolean {
        return !table.name.contains("acc_saber_score") && !table.name.contains("acc_saber_player")
    }

    override fun includeSequence(sequence: Sequence): Boolean {
        return true
    }

    companion object {
        val INSTANCE = AccSaberSchemaFilter()
    }
}