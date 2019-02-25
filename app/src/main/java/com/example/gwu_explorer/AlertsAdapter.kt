package com.example.gwu_explorer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class AlertsAdapter constructor(private val alerts: List<AlertItem>): RecyclerView.Adapter<AlertsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.alert_box, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return alerts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentAlert:AlertItem = alerts[position]
        holder.lineTextView.text = currentAlert.line
        holder.contentTextView.text = currentAlert.content
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lineTextView: TextView = view.findViewById(R.id.line)
        val contentTextView: TextView = view.findViewById(R.id.content)
    }
}