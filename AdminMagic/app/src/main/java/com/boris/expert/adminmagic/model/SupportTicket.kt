package com.boris.expert.adminmagic.model

import java.io.Serializable

data class SupportTicket(
    val id:String,
    val appName:String,
    val userName:String,
    val title:String,
    val message:String,
    val timeStamp:Long,
    val status:String
):Serializable{

    constructor():this("","","","","",0,"")

}