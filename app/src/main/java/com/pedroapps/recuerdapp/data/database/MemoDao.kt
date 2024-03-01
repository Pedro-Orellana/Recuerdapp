package com.pedroapps.recuerdapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface MemoDao {

    @Upsert(entity = MemoRoomEntity::class)
    suspend fun insertNewMemo(memo: MemoRoomEntity)

    @Update(entity = MemoRoomEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMemo(memo: MemoRoomEntity)

    @Query("SELECT * FROM memo_table")
    suspend fun getAllMemos() : List<MemoRoomEntity>

    @Query("SELECT * FROM memo_table WHERE memo_id LIKE :memoId")
    suspend fun getMemoById(memoId: Int) : MemoRoomEntity

    @Delete(entity = MemoRoomEntity::class)
    suspend fun deleteMemo(memo: MemoRoomEntity)
}