package com.pedroapps.recuerdapp.data

import com.pedroapps.recuerdapp.data.database.MemoRoomEntity
import com.pedroapps.recuerdapp.data.database.RecuerdappDatabase

class MemoDataRepository(
    private val database: RecuerdappDatabase
) {

    suspend fun saveNewMemo(memoId: Int?, memo: String, millis: Long) {
        val entity: MemoRoomEntity = when {
            (memoId != null) -> MemoRoomEntity(id = memoId, memo = memo, millis = millis)
            else -> MemoRoomEntity(memo = memo, millis = millis)
        }
        database.memoDao().insertNewMemo(entity)
    }

    suspend fun getAllMemos(): List<MemoUI> {
        return database.memoDao().getAllMemos().map { memoEntityToUi(it) }
    }

    suspend fun getMemoById(memoId: Int): MemoUI {
        return memoEntityToUi(database.memoDao().getMemoById(memoId = memoId))
    }

    suspend fun updateMemo(memo: MemoUI) {
        val entity = memoUiToEntity(memo)
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
            memo = entity.memo,
            millis = entity.millis
        )
    }


    private fun memoUiToEntity(memo: MemoUI): MemoRoomEntity {
        return MemoRoomEntity(
            id = memo.id,
            memo = memo.memo,
            millis = memo.millis

        )
    }

}