package com.mudrichenkoevgeny.backend.core.database.table

object TestTable : BaseTable("test") {
    val name = varchar("name", 50)
}
