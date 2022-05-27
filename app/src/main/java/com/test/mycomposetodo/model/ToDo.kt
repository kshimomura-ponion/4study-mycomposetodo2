package com.test.mycomposetodo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

// クラスに @Parcelize アノテーションを付けると、Parcelable の実装が自動的に生成されます。
// Parcelableというインターフェースはオブジェクトの内部で保持しているデータを一旦Parcelという箱に対比させ、
// 後から中身を取り出してオブジェクトを復元できるということ

@Entity
@Parcelize
class ToDo (
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val title: String,
    val detail: String,
    val created: Long,
    val modified: Long,
    val finished: Boolean
): Parcelable

const val emptyToDoId = -1
fun getEmptyToDo(): ToDo{
    return ToDo(
        _id = emptyToDoId,
        title = "",
        detail = "",
        created = 0,
        modified = 0,
        finished = false
    )
}