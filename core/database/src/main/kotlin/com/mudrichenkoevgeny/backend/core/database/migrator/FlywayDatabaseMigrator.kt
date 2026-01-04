package com.mudrichenkoevgeny.backend.core.database.migrator

import com.mudrichenkoevgeny.backend.core.database.di.qualifiers.DatabaseMigratorFlyway
import org.flywaydb.core.Flyway
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
@DatabaseMigratorFlyway
class FlywayDatabaseMigrator @Inject constructor(): DatabaseMigrator {

    override fun migrate(dataSource: DataSource, resources: List<String>) {
        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations(*resources.toTypedArray())
            .baselineOnMigrate(true)
            .load()

        flyway.migrate()
    }
}