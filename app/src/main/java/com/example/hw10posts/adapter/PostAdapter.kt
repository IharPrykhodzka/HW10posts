package com.example.hw10posts.adapter

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hw10posts.R
import com.example.hw10posts.dto.PostModel
import kotlinx.android.synthetic.main.item_post.view.*
import splitties.activities.startActivity
import splitties.toast.toast
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

class PostAdapter(val list: List<PostModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(this, view)
    }

    override fun getItemCount() = list.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.v("test", "position is $position")
        with(holder as PostViewHolder) {
            bind(list[position])
        }
    }

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {

            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        context.toast(context.getString(R.string.like_in_progress))
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }

            shareBtn.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    itemView.context.startActivity(Intent.ACTION_SEND) {
                        putExtra(
                            Intent.EXTRA_TEXT, """
                                ${item.ownerName} (${item.created.format(DateTimeFormatter.ISO_DATE_TIME)})
    
                                ${item.content}
                            """.trimIndent()
                        )
                        type = "text/plain"
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.ownerName
            contentTv.text = post.content
            likesTv.text = post.likes.toString()
            createdTv.text = post.created.toString()

            when {
                post.likeActionPerforming -> likeBtn.setImageResource(R.drawable.ic__favorite_pending_24)
                post.likedByMe -> {
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_red_24)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
                else -> {
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
            }
        }
    }
}