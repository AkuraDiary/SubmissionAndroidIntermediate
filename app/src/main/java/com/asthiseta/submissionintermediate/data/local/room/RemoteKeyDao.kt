package com.asthiseta.submissionintermediate.data.local.room

import androidx.room.*
import com.asthiseta.submissionintermediate.data.local.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllData(remoteKey: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remotekeys WHERE id = :id")
    suspend fun getRemoteKeysId(id: Int): RemoteKeyEntity?

    @Delete
    suspend fun deleteRemoteKey(remoteKey: RemoteKeyEntity)

    @Query("DELETE FROM remotekeys")
    suspend fun deleteAllRemoteKey()
}
