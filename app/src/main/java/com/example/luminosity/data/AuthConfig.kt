package com.example.luminosity.data

import net.openid.appauth.ResponseTypeValues


object AuthConfig {

    const val AUTH_URI = "https://unsplash.com/oauth/authorize"
    const val TOKEN_URI = "https://unsplash.com/oauth/token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "public read_user read_photos write_likes read_collections write_collections"

    const val CLIENT_ID = "SDHc-G0UXIjsEa7PBsSRiyY6TTsm8FLsQD8KY1AAJHw"
    const val CLIENT_SECRET = "E6stL2p1u6e0jEejOBgaCW9Z1sOk55KG5jE_sCFdV0o"
    const val REDIRECT_URL = "skillbox://skillbox.ru/callback"
}
