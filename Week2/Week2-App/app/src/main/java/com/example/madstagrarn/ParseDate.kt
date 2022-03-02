package com.example.madstagrarn

import java.text.SimpleDateFormat
import java.util.*

class ParseDate {
    companion object {
        fun dateConvert(date: String): String {
            val currentDateTime = Calendar.getInstance().time
            var dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
            val cY = dateFormat.split(".")[0]
            val cM = dateFormat.split(".")[1]
            val cD = dateFormat.split(".")[2].split(" ")[0]
            val cH = dateFormat.split(" ")[1].split(":")[0]
            val cm = dateFormat.split(" ")[1].split(":")[1]
            val cS = dateFormat.split(" ")[1].split(":")[2]
            val pY = date.split("-")[0]
            val pM = date.split("-")[1]
            val pD = date.split("-")[2].split("T")[0]
            val pH = date.split("T")[1].split(":")[0]
            val pm = date.split("T")[1].split(":")[1]
            val pS = date.split(":")[2].split(".")[0]

            if(cY == pY && cM == pM && cD == pD && cH == pH && cm == pm){
                return (cS.toInt() - pS.toInt()).toString() + " seconds ago"
            }
            else if(cY == pY && cM == pM && cD == pD && cH == pH){
                return (cm.toInt() - pm.toInt()).toString() + " minutes ago"
            }
            else if(cY == pY && cM == pM && cD == pD){
                return (cH.toInt() - pH.toInt()).toString() + " hours ago"
            }
            else if(cY == pY && cM == pM){
                if((cD.toInt() - pD.toInt()) == 1)    return "Yesterday"
                else return (cD.toInt() - pD.toInt()).toString() + " days ago"
            }
            else if(cY == pY){
                return (cM.toInt() - pM.toInt()).toString() + " months ago"
            }
            else{
                return (cY.toInt() - pY.toInt()).toString() + " years ago"
            }
        }
    }
}