package com.example.hw10posts.dto

import model.*
import java.time.LocalDateTime

data class AttachmentModel(val id: String, val url: String, val type: MediaType)

data class PostModel(
        val id: Int,
        val ownerName: String,
        val content: String,
        val created: LocalDateTime,
        var likes: Int,
        val commentsCount: Int,
        val shareCount: Int,
        var likedByMe: Boolean,
        val commentedByMe: Boolean,
        val sharedByMe: Boolean,
        val address: String?,
        val location: Location?,
        val video: Video?,
        val advertising: Advertising?,
        val source: PostModel?,
        val postType: PostType,
        val isHidden: Boolean,
        val timesShown: Long
) {
    var likeActionPerforming = false
    fun updateLikes(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes
        likedByMe = updatedModel.likedByMe
    }
}


