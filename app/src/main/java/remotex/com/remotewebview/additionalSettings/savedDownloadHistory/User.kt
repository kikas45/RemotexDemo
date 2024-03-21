package remotex.com.remotewebview.additionalSettings.savedDownloadHistory

import androidx.room.Entity

@Entity(tableName = "user_table", primaryKeys = ["CLO", "DEMO", "EditUrl"])
data class User(
    var id: Long = System.currentTimeMillis(),
    var CLO: String,
    var DEMO: String,
    var EditUrl: String,
)