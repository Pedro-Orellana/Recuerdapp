package com.pedroapps.recuerdapp.data

import com.pedroapps.recuerdapp.data.database.MemoRoomEntity
import com.pedroapps.recuerdapp.data.database.RecuerdappDatabase

class MemoDataRepository(
    private val database: RecuerdappDatabase
) {

    suspend fun saveNewMemo(pendingIntentID: Int, memo: String, millis: Long) {
        val entity = MemoRoomEntity(memo = memo, pendingIntentID = pendingIntentID, millis = millis)
        database.memoDao().insertNewMemo(entity)
    }

    suspend fun getAllMemos(): List<MemoUI> {
        return database.memoDao().getAllMemos().map { memoEntityToUi(it) }
    }

    suspend fun getMemoById(memoID: Int): MemoUI {
        return memoEntityToUi(database.memoDao().getMemoById(memoID = memoID))
    }

//    suspend fun updateMemo(memo: MemoUI) {
//        val entity = memoUiToEntity(memo)
//        database.memoDao().updateMemo(entity)
//    }

    suspend fun updateMemo(memoID: Int, pendingIntentID: Int, memoString: String, memoMillis: Long) {
        val entity = MemoRoomEntity(id = memoID, pendingIntentID = pendingIntentID, memo = memoString, millis = memoMillis)
        database.memoDao().updateMemo(entity)
    }

    suspend fun deleteMemo(memo: MemoUI) {
        val entity = memoUiToEntity(memo)
        database.memoDao().deleteMemo(entity)
    }


    //CONVERTER FUNCTION

    private fun memoEntityToUi(entity: MemoRoomEntity): MemoUI {
        return MemoUI(
            id = entity.id,
            pendingIntentID = entity.pendingIntentID,
            memo = entity.memo,
            millis = entity.millis
        )
    }


    private fun memoUiToEntity(memo: MemoUI): MemoRoomEntity {
        return MemoRoomEntity(
            id = memo.id,
            pendingIntentID = memo.pendingIntentID,
            memo = memo.memo,
            millis = memo.millis
        )
    }

}