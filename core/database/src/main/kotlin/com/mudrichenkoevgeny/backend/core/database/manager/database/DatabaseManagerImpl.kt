package com.mudrichenkoevgeny.backend.core.database.manager.database

import com.mudrichenkoevgeny.backend.core.database.config.model.DatabaseConfig
import com.mudrichenkoevgeny.backend.core.database.migrator.DatabaseMigrator
import com.mudrichenkoevgeny.backend.core.database.table.BaseTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class DatabaseManagerImpl @Inject constructor(
    private val dataSource: DataSource,
    private val databaseMigrator: DatabaseMigrator,
    private val databaseTables: Set<@JvmSuppressWildcards BaseTable>,
    private val databaseConfig: DatabaseConfig
): DatabaseManager {

    override fun create(): Database {

        val database = Database.connect(dataSource)

        databaseMigrator.migrate(dataSource, databaseConfig.migrationPaths)

        createTables(databaseTables)

        return database
    }

    override fun shutdown() {
        (dataSource as? AutoCloseable)?.close()
    }

    private fun createTables(tables: Set<Table>) = transaction {
        if (tables.isNotEmpty()) {
            SchemaUtils.create(*tables.toTypedArray())
        }
    }
}