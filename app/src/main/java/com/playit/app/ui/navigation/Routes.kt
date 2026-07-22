package com.playit.app.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val PROFILE_SELECT = "profile_select"
    const val NAME_PROMPT = "name_prompt"
    const val MAP = "map"
    const val PARENT_DASHBOARD = "parent_dashboard"

    const val HEAR_IT = "hear_it/{phonemeId}"
    const val SAY_IT = "say_it/{phonemeId}"
    const val FIND_IT = "find_it/{phonemeId}"
    const val BLEND_IT = "blend_it/{phonemeId}"

    fun hearIt(phonemeId: String): String = "hear_it/$phonemeId"
    fun sayIt(phonemeId: String): String = "say_it/$phonemeId"
    fun findIt(phonemeId: String): String = "find_it/$phonemeId"
    fun blendIt(phonemeId: String): String = "blend_it/$phonemeId"
}
