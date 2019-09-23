package com.gustavo.bankandroid.statements.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gustavo.bankandroid.R
import com.gustavo.bankandroid.entity.UserStatementItem
import com.gustavo.bankandroid.extensions.inflate
import kotlinx.android.synthetic.main.item_statement.view.*

class StatementViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_statement)) {

    fun bind(statement: UserStatementItem) {
        itemView.titleTextView.text = statement.title
        itemView.descTextView.text = statement.desc
        itemView.dateTextView.text = statement.date
        itemView.valueTextView.text = statement.value.toString()
    }

}