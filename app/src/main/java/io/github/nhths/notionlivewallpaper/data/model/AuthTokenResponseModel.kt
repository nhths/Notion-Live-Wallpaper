package io.github.nhths.notionlivewallpaper.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthTokenResponseModel (
        @field:Json(name = "access_token")
        val accessToken: String,

        @field:Json(name = "token_type")
        val tokenType: String,

        @field:Json(name = "bot_id")
        val botID: String,

        @field:Json(name = "workspace_name")
        val workspaceName: String,

        @field:Json(name = "workspace_icon")
        val workspaceIcon: Any? = null,

        @field:Json(name = "workspace_id")
        val workspaceID: String,

        @field:Json(name = "owner")
        val owner: Owner,

        @field:Json(name = "duplicated_template_id")
        val duplicatedTemplateID: String? = null
)

@JsonClass(generateAdapter = true)
data class Owner (
        @field:Json(name = "type")
        val type: String,

        @field:Json(name = "user")
        val user: User
)

@JsonClass(generateAdapter = true)
data class User (
        @Json(name = "object")
        val userObject: String,

        @field:Json(name = "id")
        val id: String,

        @field:Json(name = "name")
        val name: String,

        @Json(name = "avatar_url")
        val avatarURL: String?,

        @field:Json(name = "type")
        val type: String,

        @field:Json(name = "person")
        val person: Person
)

@JsonClass(generateAdapter = true)
data class Person (
        @field:Json(name = "email")
        val email: String
)